package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.domain.mapper.CartMapper
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.presentation.state.ActiveCartItemUi
import com.example.foodapp.presentation.state.CartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var cartJob: Job? = null

    private val _uiCartState = MutableStateFlow(CartUiState())
    val uiCartState = _uiCartState.asStateFlow()

    init {
        observeCart()
    }

    private fun observeCart() {
        cartJob?.cancel()
        cartJob = viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            cartRepository.getCart(userId).collectLatest { response ->
                _uiCartState.update {
                    it.copy(cart = response.getDataOrNull())
                }
            }
        }
    }

    //food ko co variation
    fun addSimpleItem(food: Food, restaurant: Restaurant) {
        Log.d("Check Food", food.toString())
//        if (food.variations.isNotEmpty()) { //flow đoạn này nên tạo 1 hàm để kt food có var opttion ko, rồi mới nên gọi addSimpleItem hay addEditingItem, khi user click item hàm trung gian đó chỉ cần gọi ra thôi
//            startEditing(food)
//            return
//        }
        val baseItem = ActiveCartItemUi(food)
        val key = buildKey(baseItem)

        if (key in _uiCartState.value.loadingFoodIds) return //chawn user bam nhieu lan

//        viewModelScope.launch {
//            setFoodLoading(food.foodId, true) //k su dung cai nay o editing item vi co the gay ra loi, api chưa kịp trả về mà người dùng back, loading kẹt mãi gây lỗi UI
//            val userId = authRepository.currentUserId() ?: return@launch
//            val item = ActiveCartItemUi(food, 1 )
//            handleCartResult(cartRepository.addItem(userId, CartMapper.toDomain(item), restaurant))
//            setFoodLoading(food.foodId, false)
//        }
        viewModelScope.launch {
            try {
                setItemLoading(food.foodId, true)
                val userId = authRepository.currentUserId() ?: return@launch
                val currentQty = _uiCartState.value.cart?.getQuantityOf(food.foodId) ?: 0
                val item = ActiveCartItemUi(food, (currentQty + 1).coerceIn(1, 20))
                handleCartResult(
                    cartRepository.addItem(
                        userId,
                        CartMapper.toDomain(item),
                        restaurant
                    )
                )
            } finally {
                setItemLoading(key = key, false)
            }
        }
    }

    //fun addSimpleItem(food: Food, restaurant: Restaurant) {
    //    if (food.foodId in _uiCartState.value.loadingFoodIds) return
    //
    //    viewModelScope.launch {
    //        try {
    //            setFoodLoading(food.foodId, true)
    //            val userId = authRepository.currentUserId() ?: return@launch
    //            val currentQty = _uiCartState.value.cart?.getQuantityOf(food.foodId) ?: 0
    //            val item = ActiveCartItemUi(food, (currentQty + 1).coerceIn(1, 20))
    //            handleCartResult(cartRepository.addItem(userId, CartMapper.toDomain(item), restaurant))
    //        } finally {
    //            setFoodLoading(food.foodId, false) // luôn được gọi dù có lỗi hay return
    //        }
    //    }

    //food co variation
    fun addEditingItem(restaurant: Restaurant) {
        val missing = getInvalidVariation()
        if (missing.isNotEmpty()) {
            _uiCartState.update {
                it.copy(error = "Vui lòng chọn: ${missing.joinToString(", ")}")
            }
            return
        }

        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            val item = _uiCartState.value.currentEditingItem ?: return@launch
            handleCartResult(cartRepository.addItem(userId, CartMapper.toDomain(item), restaurant))
            _uiCartState.update { it.copy(currentEditingItem = null) }

        }
    }

//    val cartSummary: StateFlow<CartSummary> = _uiCartState
//        .map { state ->
//            val cartQty = state.cart?.getTotalItemCount() ?: 0
//            val cartPrice = state.cart?.calculateTotalPrice() ?: 0.0
//
//            val editingQty = state.currentEditingItem?.quantity ?: 0
//            val editingPrice = state.currentEditingItem?.calculatePrice() ?: 0.0
//
//            CartSummary(
//                totalQuantity = cartQty + editingQty,
//                totalPrice = cartPrice + editingPrice
//            )
//    }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartSummary())


    //thoong bao cho UI biet co required ko
//    fun getMissingRequiredVariations(): List<String> {
//        val item = _uiCartState.value.currentEditingItem ?: return emptyList()
//        return item.food.variations
//            .filter { variation ->
//                variation.required &&
//                        item.variations[variation.id].isNullOrEmpty()
//            }
//            .map { it.name }
//    }
//    private fun validateCurrentItem(): Boolean {
//        val item = _uiCartState.value.currentEditingItem ?: return false
//
//        return item.food.variations.all { variation ->
//            val selected = item.variations[variation.id].orEmpty()
//
//            if (!variation.required && selected.isEmpty()) return@all true //kt required co bat buoc k
//            if (variation.required && selected.isEmpty()) return@all false
//
//            when (variation.type) {
//                Variation.VariationType.SINGLE -> selected.size == 1
//                Variation.VariationType.MULTI -> selected.isNotEmpty()
//            }
//        }
//    }

    //dựa vào id food để loading trạng thái, để tránh user spam
    private fun setItemLoading  (key: String, loading: Boolean) {
//        val current = _uiCartState.value.currentEditingItem ?: return
        _uiCartState.update { state ->
            val current = state.loadingFoodIds.toMutableSet()
             if (loading) current.add(key) else current.remove(key)
            state.copy(loadingFoodIds = current)
        }
    }

    private fun getInvalidVariation(): List<String> {
        val item = _uiCartState.value.currentEditingItem ?: return emptyList()
        return item.food.variations.mapNotNull { variation ->  //kt xem food var co bat buoc chon ko
            val selected = item.variations[variation.id].orEmpty()
            val isValid = when {
                !variation.required && selected.isEmpty() -> false
                variation.required && selected.isEmpty() -> true
                variation.type == Variation.VariationType.SINGLE -> selected.size == 1
                variation.type == Variation.VariationType.MULTI -> selected.isNotEmpty()
                else -> true
            }
            if (isValid) null else variation.name // trả về danh sách trống nếu ko có lỗi, và ngược lại
        }
    }


    //wrapper api
    private fun handleCartResult(result: ApiResponse<Cart>, onSuccess: (() -> Unit)? = null) {
        when (result) {
            is ApiResponse.Success -> {
                _uiCartState.update {
                    it.copy(cart = result.data)
                }
                onSuccess?.invoke() //gọi lại callback nếu ko null -> success


            }

            is ApiResponse.Error -> _uiCartState.update { it.copy(error = result.message) }
            is ApiResponse.Loading -> {}
            else -> {}
        }
    }


    //set state co variation
    fun startEditing(food: Food) {
        _uiCartState.update {
            it.copy(
                currentEditingItem = ActiveCartItemUi(food = food)
            )
        }
    }

    fun selectVariation(optionId: String, variation: Variation) {
        _uiCartState.update { state ->
            val item = state.currentEditingItem ?: return@update state

            val current = item.variations.toMutableMap()
            val currentOptions = current[variation.id]?.toMutableList() ?: mutableListOf()

            val option = variation.getOptionById(optionId) ?: return@update state //kt var có tồn tại trong food k

            when (variation.type) {
                Variation.VariationType.MULTI -> { //kt mode multi, toggle bật tắt nếu user click 2 lần
                    val existing = currentOptions.find { it.id == optionId }
                    if (existing != null) currentOptions.remove(existing)
                    else currentOptions.add(option)
                }

                Variation.VariationType.SINGLE -> {
                    currentOptions.clear()
                    currentOptions.add(option)
                }
            }

            current[variation.id] = currentOptions

            state.copy(
                currentEditingItem = item.copy(variations = current)
            )
        }
    }

    //change quantity in detail food
    fun changeQuantity(by: Int) {
        _uiCartState.update { state ->
            val item = state.currentEditingItem ?: return@update state

            val newQty = (item.quantity + by).coerceIn(1, 20)

            state.copy(
                currentEditingItem = item.copy(quantity = newQty)
            )
        }
    }

    //update note in detail food
    fun updateNote(note: String) {
        _uiCartState.update { state ->
            val item = state.currentEditingItem ?: return@update state
            state.copy(
                currentEditingItem = item.copy(note = note)
            )
        }
    }


    fun removeItem(key: String) {
        if (key in _uiCartState.value.loadingFoodIds) return
        try {
            viewModelScope.launch {
                setItemLoading(key, true)
                val userId = authRepository.currentUserId() ?: return@launch
//             handleCartResult(cartRepository.removeItem(userId, food = food.foodId)) {
                handleUnitResult(cartRepository.removeItem(userId, key)) {
                    checkAndClearCartIfEmpty(userId) //nếu api succes gọi tiếp hàm này
                }
            }
        } finally {
            setItemLoading(key, false)
        }

    }

    //edit quantity ko co var, va xoa cart neu quantity < 0
    fun changeSimpleQuantity(food: Food, by: Int) {
        val restaurant = _uiCartState.value.restaurant ?: return
        val baseItem = ActiveCartItemUi(food)
        val key = buildKey(baseItem)
        if (key in _uiCartState.value.loadingFoodIds) return

        viewModelScope.launch {
            try {
                setItemLoading(key, true)
                val userId = authRepository.currentUserId() ?:return@launch
                val currentQty = _uiCartState.value.cart?.getQuantityOf(key) ?: 0
                val newQty = currentQty + by //+ qty moi voi cu
                when {
                    newQty <= 0 -> { //neu newqty < 0, thi xoa item, va kiem tra cart co trong ko, neu co xoa luon
                        handleUnitResult(
                            cartRepository.removeItem(userId = userId, key = key)
                        ) {
                            checkAndClearCartIfEmpty(userId = userId)
                        } //wrapper response api thanh cong
                    }
                    else -> { //goi addItem de ghi de item moi da + food
                        val item = ActiveCartItemUi(food = food, quantity = newQty.coerceIn(1, 20))
                        handleCartResult(
                            cartRepository.addItem(
                                userId, CartMapper.toDomain(item), restaurant = restaurant))
                    }
                }
            } finally {
                setItemLoading(key, false)
            }
        }
    }

    private fun checkAndClearCartIfEmpty(userId: String) {
        viewModelScope.launch {
            val cart = _uiCartState.value.cart ?: return@launch //cart trống xóa cart
            if (cart.isEmpty()) {
                cartRepository.clearCart(userId)
                _uiCartState.update { it.copy(cart = null) }
            }

        }
    }

    private fun handleUnitResult(result: ApiResponse<Unit>, onSuccess: (() -> Unit)? = null) {
        when (result) {
            is ApiResponse.Success -> onSuccess?.invoke()
            is ApiResponse.Error -> _uiCartState.update { it.copy(error = result.message) }
            else -> {}
        }
    }


    // ADD TO ACTIVE CART (LOCAL)

//    fun confirmAddItem() {
//        if (!validateCurrentItem()) return
//        addActiveItem()
//    }

//    private fun addActiveItem() {
//        _uiCartState.update { state ->
//            val item = state.currentEditingItem ?: return@update state //có kiểu  ActiveCartItemUi? = null
//            val current = state.activeItems.toMutableMap() //là value nên cũng có kiểu  ActiveCartItemUi
//
//            val key = buildKey(item) //tạo key để ko trùng
//            val existing = current[key] //kiểm tra có dữ liệu trước đó ko
//
//            current[key] = if (existing != null) { //nếu có tức đã tồn tại +1 quality lên
//                existing.copy(quantity = existing.quantity + item.quantity)
//            } else {
//                    item //đoạn này có nghĩa là tạo mới, nhưng tôi ko hiểu do item bị trống do lúc đầu chưa có giá trị, và dc mặc định bằng null, tức là return
//            }
//
//            state.copy(
//                activeItems = current, //đoạn này tôi cũng ko hiểu vì tôi vẫn chưa thấy set giá trị mới. ví dụ user chọn food item thì hàm này phải nhận giá trị đó, hoặc state nhưng tôi ko thấy gì hết
//                currentEditingItem = null
//            )
//        }
//    }
//    fun addItemDirectly(food: Food) {
//        val item = ActiveCartItemUi(food = food, quantity = 1)
//        _uiCartState.update { state ->
//            val current = state.activeItems.toMutableMap()
//            val key = buildKey(item)
//            val existing = current[key]
//            current[key] = if (existing != null) {
//                existing.copy(quantity = existing.quantity + 1)
//            } else {
//                item
//            }
//            state.copy(activeItems = current)
//        }
//    }


    private fun buildKey(item: ActiveCartItemUi): String {
        val variationKey = item.variations
            .toSortedMap()
            .map { (k, v) ->
                val sortedIds = v.map { it.id }.sorted()
                "$k:${sortedIds.joinToString()}"
            }
            .joinToString("|")

        return "${item.food.foodId}#$variationKey"
    }


    fun getCartTotalPrice(): Double {
        return _uiCartState.value.cart?.calculateTotalPrice() ?: 0.0
    }
}
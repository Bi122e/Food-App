package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.domain.mapper.CartMapper
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.presentation.state.ActiveCartItemUi
import com.example.foodapp.presentation.state.CartUiState
import com.example.foodapp.presentation.state.ConflictData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository,
    private val restaurantRepository: RestaurantRepository,
) : ViewModel() {

    private var cartJob: Job? = null

    private val _uiCartState = MutableStateFlow(CartUiState())
    val uiCartState = _uiCartState.asStateFlow()

    init {
        observeCart()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCart() {
        cartJob?.cancel()
        cartJob = viewModelScope.launch {
            val userId = authRepository.currentUserId()
            if (userId == null) {
                Log.d("CartState", "current userId null")
                return@launch
            }
            cartRepository.getCart(userId)
                .flatMapLatest { response ->
                    if (response is ApiResponse.Success) {
                        val cart = response.data
                        _uiCartState.update { it.copy(cart = cart) }
                        restaurantRepository.getRestaurantById(cart.restaurantId)
                    } else {
                        emptyFlow()
                    }
                }
                .collectLatest { restaurant ->
                    if (restaurant is ApiResponse.Success) {
                        _uiCartState.update { it.copy(restaurant = restaurant.data) }
                        Log.d("CartViewModel_cart", "res state = ${_uiCartState.value.restaurant}")
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
                setItemLoading(key, true)
                val userId = authRepository.currentUserId() ?: return@launch
                val currentQty = _uiCartState.value.cart?.getQuantityOf(key) ?: 0
                val item = ActiveCartItemUi(food, (currentQty + 1).coerceIn(1, 20))
                handleCartResult(
                    cartRepository.addItem(
                        userId,
                        CartMapper.toDomain(item),
                        restaurant,
                        false
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
//    fun addEditingItem(restaurant: Restaurant) {
//        val missing = getInvalidVariation()
//        if (missing.isNotEmpty()) {
//            _uiCartState.update {
//                it.copy(error = "Vui lòng chọn: ${missing.joinToString(", ")}")
//            }
//            return
//        }
//
//        viewModelScope.launch {
//            val userId = authRepository.currentUserId() ?: return@launch
//            val item = _uiCartState.value.currentEditingItem ?: return@launch
//
//
//        }
//
//    }

    //food co variation
    fun addEditingItem() {
        val missing = getInvalidVariation()
        if (missing.isNotEmpty()) {
            _uiCartState.update { it.copy(error = "Vui lòng chọn: ${missing.joinToString(", ")}") }
        }
        viewModelScope.launch {
            val restaurant = _uiCartState.value.restaurant ?: return@launch
            val userId = authRepository.currentUserId() ?: return@launch
            val item = _uiCartState.value.currentEditingItem ?: return@launch
            handleCartResult(
                cartRepository.addItem(
                    userId,
                    CartMapper.toDomain(item),
                    restaurant,
                    forceClear = false
                )
            )
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
    //flow dựa vào true hay false để add hay remove, khi sử dụng gọi loading state có chứa key ko, nếu có (true) thì dừng hàm, để lệnh loading trước đó chạy xong
    private fun setItemLoading(key: String, loading: Boolean) {
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
            val allSelected = item.variations[variation.id].orEmpty() //
            // Lọc chỉ những option thuộc về variation hiện tại (trường hợp trùng ID)
            val selected =
                allSelected.filter { opt -> variation.options.any { it.id == opt.id } } //
//            val isValid = when {
//                !variation.required && selected.isEmpty() -> false             -> required mà chưa chọn → lỗi
//                variation.required && selected.isEmpty() -> true  -> SINGLE mà chọn > 1 → lỗi
//                variation.type == Variation.VariationType.SINGLE -> selected.size == 1    MULTI mà required nhưng chưa chọn → lỗi
//                variation.type == Variation.VariationType.MULTI -> selected.isNotEmpty()
//                else -> true
//            }
            val isValid = when (variation.type) {
                Variation.VariationType.SINGLE -> {
                    if (variation.required) selected.size == 1
                    else selected.size <= 1
                }

                Variation.VariationType.MULTI -> {
                    if (variation.required) selected.isNotEmpty()
                    else true
                }
            }
            if (isValid) null else variation.name // trả về danh sách trống nếu ko có lỗi, và ngược lại
        }
    }

    //clear item cu neu user bam dong y xoa nha hang khi them mon an
    fun forceAddItem(restaurant: Restaurant) {
        val pending = _uiCartState.value.pending ?: return

        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch

            handleCartResult(
                cartRepository.addItem(
                    userId,
                    CartMapper.toDomain(pending),
                    restaurant,
                    forceClear = true
                )
            )

            _uiCartState.update {
                it.copy(
                    showConfirmDialog = false,
                    conflictData = null,
                    pending = null,
                    currentEditingItem = null
                )
            }
        }
    }

    //wrapper api
    private fun handleCartResult(result: ApiResponse<Cart>, onSuccess: (() -> Unit)? = null) {
        when (result) {
            is ApiResponse.Success -> {
                //snapshot se tu ban (emit) lai nen ko can set
                onSuccess?.invoke() //gọi lại callback nếu ko null -> success
            }

            is ApiResponse.Error -> _uiCartState.update { it.copy(error = result.message) }
            is ApiResponse.Loading -> {}
            is ApiResponse.Conflict -> _uiCartState.update {
                it.copy(
                    showConfirmDialog = true,
                    conflictData = ConflictData(
                        message = result.message,
                        oldRestaurantName = result.oldRestaurantName,
                        newRestaurantName = result.newRestaurantName
                    ),
                    pending = it.currentEditingItem //tai sao lai can cai nay trong khi da co current edit truoc do roi, tao 2 cai current ?
                )
            }

            is ApiResponse.Empty -> {

            }
        }
    }


    //set state co variation
    fun startEditing(food: Food) {
        viewModelScope.launch {
            restaurantRepository.getRestaurantById(food.restaurantId).collectLatest { response ->
                if (response is ApiResponse.Success)
                    _uiCartState.update {
                        it.copy(
                            restaurant = response.data,
                            currentEditingItem = ActiveCartItemUi(food = food)
                        )
                    }
            }
        }

    }


    fun selectVariation(optionId: String, variation: Variation, isChecked: Boolean) {
        Log.d(
            "DEBUG_CART",
            "selectVariation: opt=$optionId, var=${variation.id}, type=${variation.type}, checked=$isChecked"
        )
        _uiCartState.update { state ->
            val item = state.currentEditingItem ?: run {
                Log.d("DEBUG_CART", "selectVariation: currentEditingItem is NULL")
                return@update state
            }

            Log.d("DEBUG_CART", "current variations pre-update: ${item.variations.keys}")
            val current = item.variations.toMutableMap()
            val currentOptions = current[variation.id]?.toMutableList() ?: mutableListOf()
            Log.d(
                "DEBUG_CART",
                "options for ${variation.id} before: ${currentOptions.map { it.id }}"
            )

            val option = variation.getOptionById(optionId) ?: run {
                Log.d("DEBUG_CART", "selectVariation: option NOT found")
                return@update state
            }

            when (variation.type) {
                Variation.VariationType.MULTI -> { //kt mode multi, toggle bật tắt nếu user click 2 lần
//                    val existing = currentOptions.find { it.id == optionId }
//                    if (existing != null) currentOptions.remove(existing)
//   state                 else currentOptions.add(option)
                    if (isChecked) {
                        if (!currentOptions.any { it.id == optionId }) {
                            currentOptions.add(option)
                        }
                    } else {
                        currentOptions.removeAll { it.id == optionId }

                    }
                }

                Variation.VariationType.SINGLE -> {
                    if (isChecked) {
                        // Chỉ xóa các option thuộc về variation block này (tránh reset các block khác cùng ID)
                        currentOptions.removeAll { existing -> variation.options.any { it.id == existing.id } }
                        currentOptions.add(option)
                    } else {
                        //uncheck nếu k required
                        if (!variation.required) {
                            currentOptions.removeAll { existing -> variation.options.any { it.id == existing.id } }
                        }
                    }
                }
            }

            current[variation.id] = currentOptions
            Log.d(
                "DEBUG_CART",
                "options for ${variation.id} after: ${currentOptions.map { it.id }}"
            )
            Log.d("DEBUG_CART", "final variations: ${current.keys}")

            val newVariations = current
            state.copy(
                currentEditingItem = item.copy(variations = newVariations)
            )
        }
    }

    fun selectedMulti(
        variation: Variation,
        optionId: String,
        isSelected: Boolean
    ) {
        _uiCartState.update { state ->
            val item = state.currentEditingItem
            val current = item?.variations?.toMutableMap() ?: return@update state
            val currentOptions = current[variation.id]?.toMutableList() ?: return@update state
            val option = variation.getOptionById(optionId) ?: return@update state
            if (!isSelected) {
                currentOptions.removeAll { existing -> variation.options.any { it.id == existing.id } }
            } else {
                currentOptions.add(option)
            }
            current[variation.id] = currentOptions
            state.copy(currentEditingItem = state.currentEditingItem.copy(variations = current))
        }
    }


    //minhf can kt option id co trong state ko, hoac option id co trong var ko
    fun selectedSingle(variation: Variation, optionId: String) {
        //cach 1
        _uiCartState.update { state ->
            val item = state.currentEditingItem
            val current = item?.variations?.toMutableMap() ?: return@update state
            val currentOption = current[variation.id]?.toMutableList() ?: mutableListOf()
            val option = variation.getOptionById(optionId) ?: return@update state
            currentOption.removeAll { existing -> variation.options.any { it.id == existing.id } }
            currentOption.add(option)
            current[variation.id] = currentOption
            state.copy(currentEditingItem = item.copy(variations = current))

        }
        //cach 2
//        _uiCartState.update { state ->
//            val item = state.currentEditingItem
//            val current = item?.variations?.toMutableMap() ?: return@update state
//            val option = variation.getOptionById(optionId) ?: return@update state
//             current[variation.id] = listOf(option)
//            state.copy(
//                currentEditingItem = item.copy(variations = current )
//            )
//        }

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

    fun increaseQtyDetail(max: Int = 20) {
        _uiCartState.update { cartUiState ->
            val current = cartUiState.currentEditingItem ?: return
            var quantity = current.quantity
            if (quantity < max) {
                quantity += 1
            }
            cartUiState.copy(currentEditingItem = current.copy(quantity = quantity))
        }
    }

    fun decreaseQtyDetail(min: Int = 1) {
        _uiCartState.update { cartUiState ->
            val current = cartUiState.currentEditingItem ?: return
            var quantity = current.quantity
            if (quantity > min) {
                quantity -= 1
            }

            cartUiState.copy(currentEditingItem = current.copy(quantity = quantity))
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
        viewModelScope.launch {

            try {
                setItemLoading(key, true)
                val userId = authRepository.currentUserId() ?: return@launch
//             handleCartResult(cartRepository.removeItem(userId, food = food.foodId)) {
                handleUnitResult(cartRepository.removeItem(userId, key)) {
                    checkAndClearCartIfEmpty(userId) //nếu api succes gọi tiếp hàm này
                }
            } finally {
                setItemLoading(key, false)
            }
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
                val userId = authRepository.currentUserId() ?: return@launch
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
                        val item =
                            ActiveCartItemUi(food = food, quantity = newQty.coerceIn(1, 20))
                        handleCartResult(
                            cartRepository.addItem(
                                userId, CartMapper.toDomain(item),
                                restaurant = restaurant,
                                false
                            )
                        )
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

    fun calculateWithVariation(): Int {
        val current = _uiCartState.value.currentEditingItem ?: return 0
        val variation = current.variations
        return current.food.getPriceWithVariation(variation)
    }


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

    fun clearCart() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            val response = cartRepository.clearCart(userId)
            when (response) {
                is ApiResponse.Error -> {
                    _uiCartState.update { it.copy(error = response.message) }
                    Log.d("Cart ViewModel Clear Cart", "Failed")
                }
                is ApiResponse.Success -> {
                    _uiCartState.update { it.copy(cart = null) }
                    Log.d("Cart ViewModel Clear Cart", "Success")
                }
                else -> {}
            }
        }
    }
}

//lưu bằng variation.id
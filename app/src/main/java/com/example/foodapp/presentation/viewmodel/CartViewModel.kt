package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.domain.model.toCartItem
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

//     private val _cartState = MutableStateFlow<UiState<Cart>>(UiState.Loading)
//    val cartState: StateFlow<UiState<Cart>> = _cartState.asStateFlow()
//
//     private val _selectedQuantity = MutableStateFlow(1)
//    val selectedQuantity: StateFlow<Int> = _selectedQuantity.asStateFlow()
//
//    private val _selectedVariations = MutableStateFlow<Map<String, List<VariationOption>>>(emptyMap())
//    val selectedVariations = _selectedVariations.asStateFlow()
//
//    private val _specialInstructions = MutableStateFlow("")
//    val specialInstruction: StateFlow<String> = _specialInstructions.asStateFlow()
//
//    private val _addToCartState = MutableStateFlow<UiState<Cart>>(UiState.Idle)
//    val addToCartState = _addToCartState.asStateFlow()
//
//    private val _activeItems = MutableStateFlow<Map<String, ActiveCartItem>>(emptyMap())
//    val activeItem = _activeItems.asStateFlow()

    private val _uiCartState = MutableStateFlow<CartUiState>(CartUiState())
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

    //   Active Item Operations


    fun selectVariation(optionId: String, variation: Variation) {
        val current = _uiCartState.value.selectedVariations.toMutableMap()
        val currentOptions = current[variation.id]?.toMutableList()
            ?: mutableListOf() //lấy option hiện tại nếu id ko khớp, -> trống user chưa chọn tạo mới
        val option = variation.getOptionById(optionId)
            ?: return //kiểm tra option usr chọn có khớp với option dữ liệu thật tế ko

        when (variation.type) { //kiểm tra mode option food
            Variation.VariationType.SINGLE -> {  //single thì xóa cái cũ thêm cái mới, chỉ được phép chứa 1 option
                currentOptions.clear()
                currentOptions.add(option)
            }

            Variation.VariationType.MULTI -> { //multi kiểm tra để xóa cái đã tồn tại, cơ chế toggle -> user bấm lại cái đã thêm trước đó, xóa
                val existing = currentOptions.find { it.id == optionId }
                if (existing != null) {
                    currentOptions.remove(existing)
                } else {
                    currentOptions.add(option)
                }
            }
        }
        current[variation.id] = currentOptions

    }

    fun updateSpecialInstructions(instructions: String) {
        _uiCartState.update { it.copy(specialInstructions = instructions) }
    }

    fun calculateItemPreviewPrice(food: Food): Int {
        return buildCartItem(food).getTotalPrice()
    }


    fun changeQuantity(by: Int) {
        val newQty = (_uiCartState.value.selectedQuantity + by).coerceIn(1, 20)
        _uiCartState.update { it.copy(selectedQuantity = newQty) }
    }

    //real price
    fun getCartTotalPrice(): Double {
        return _uiCartState.value.cart?.calculateTotalPrice() ?: 0.0
    }

    //fake price when render UI
    fun previewCartTotal(food: Food): Double {
        val currentCart = _uiCartState.value.cart ?: return 0.0
        val newItem = buildCartItem(food)
        val newCartItems = currentCart.cartItems.toMutableList()

        // check nếu item giống thì cộng quantity (optional - advanced)
        val existingIndex = newCartItems.indexOfFirst {
            it.foodId == newItem.foodId &&
                    it.variation == newItem.variation
        }

        if (existingIndex != -1) {
            val existing = newCartItems[existingIndex]
            newCartItems[existingIndex] =
                existing.copy(quantity = existing.quantity + newItem.quantity)
        } else {
            newCartItems.add(newItem)
        }
        val previewSubTotal = newCartItems.sumOf { it.getTotalPrice() }
        return previewSubTotal + currentCart.deliveryFee.toDouble()
    }

    fun addToCart(food: Food, restaurant: Restaurant) {
        Log.d("ADDSTATE", "run")
        viewModelScope.launch {
            if (_uiCartState.value.isLoading) return@launch //tranh user spam add btn
            val userId = authRepository.currentUserId() ?: return@launch

            if (!validateVariation(food)) {
                _uiCartState.update { it.copy(error = true) }
                return@launch
            }

            _uiCartState.update { it.copy(isLoading = true) }
            val cartItem = buildCartItem(food) //set state selected item


            when (val result =
                cartRepository.addItem(item = cartItem, userId = userId, restaurant = restaurant)) {
                is ApiResponse.Success -> {
                    _uiCartState.update { it.copy(cart = result.data) }
                    Log.d("ADDSTATE", "thanh cong")
                    resetSelection()
                }

                is ApiResponse.Error -> {
                    _uiCartState.update { it.copy(error = true) }
                    Log.d("CartViewModel", "Failed to add to cart: ${result.message}")
                }

                else -> Unit
            }
        }
    }

    //ham nay
    private fun validateVariation(food: Food): Boolean {
        //all return true/false neu thoa man dieu kien trong danh sách user đã chọn, nếu 1 item false thì trả về tất cả false, muc dich de kiem tra variation co hop le k
        return food.variations.all { variation ->
            val selected =
                _uiCartState.value.selectedVariations[variation.id].orEmpty()     // lay current variation hiện tại, nếu current ko có varId thì null
//            if (!variation.required && (selected == null || selected.isEmpty())) return@all true // kiểm tra variation tồn tại - trống và "ko bắt buộc chọn option = false" -> vì user ko chọn gì và variation ko bắt buộc chọn nếu kt tiếp sẽ vô nghĩa, nên return true
            if (!variation.required && selected.isEmpty()) return@all true
            if (variation.required && selected.isEmpty()) return@all false
//            selected ?: return false //variation = true mà user chưa chọn selected == null, return

            when (variation.type) { //và trường hợp ngược lại nếu user đã chọn, selected != null, kt type mode
                Variation.VariationType.SINGLE -> selected.size == 1 // selected, option phải bằng 1 c
                // ho single mode
                Variation.VariationType.MULTI -> selected.isNotEmpty() //multi thế nào cũng được
            }
        }
    }

    private fun buildCartItem(food: Food): CartItem {
        return food.toCartItem(
            quantity = _uiCartState.value.selectedQuantity,
            selectedVariations = _uiCartState.value.selectedVariations,
            specialInstructions = _uiCartState.value.specialInstructions
        )
    }

    fun resetSelection() {
        _uiCartState.update { it.copy(selectedQuantity = 1) }
        _uiCartState.update { it.copy(selectedVariations = emptyMap()) }
        _uiCartState.update { it.copy(specialInstructions = "") }

    }


    fun onQuickAdd(food: Food) {
        _uiCartState.update { state ->

            val current =
                state.activeItem.toMutableMap() //map <k, v>,
            val existing = current[food.foodId] //map[v]

            if (existing != null) {
//                cartItem.values.map { it.copy(quantity = it.quantity + 1) }
                current[food.foodId] = existing.copy(quantity = existing.quantity + 1)
            } else {
                // tai sao current = thi loi 'val' cannot be reassigned., du mutableMap
                current[food.foodId] = ActiveCartItemUi(food, quantity = 1, emptyMap())
            }

            state.copy(activeItem = current)
        }
    }

    //cach viet gon hon
    fun onQuickAdd2(food: Food) {
        _uiCartState.update { state ->
            val item = state.activeItem[food.foodId]

            val updated = item?.copy(quantity = item.quantity + 1)
                ?: ActiveCartItemUi(food = food, quantity = 1, variations = emptyMap())
            state.copy(activeItem = state.activeItem + mapOf(food.foodId to updated))

        }
    }

        fun onQuantityChange(foodId: String, delta: Int) {
            _uiCartState.update { state ->
                val currentMap = state.activeItem.toMutableMap()
                val item = currentMap[foodId] ?: return@update state

                val newQty = item.quantity + delta

                when {
                    newQty <= 0 -> {
                        // remove item
                        currentMap.remove(foodId)
                    }

                    newQty > 20 -> {
                        // giữ nguyên (không update)
                        return@update state
                    }

                    else -> {
                        currentMap[foodId] = item.copy(quantity = newQty)
                    }
                }

                state.copy(activeItem = currentMap)
            }
        }
    fun increase(foodId: String) = onQuantityChange(foodId, +1)
    fun decrease(foodId: String) = onQuantityChange(foodId, -1)
}

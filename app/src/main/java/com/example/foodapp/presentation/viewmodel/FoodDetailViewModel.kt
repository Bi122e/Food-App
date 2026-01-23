package com.example.foodapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.FavoriteRepository
import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.Favorite
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.Variation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val restaurantRepository: RestaurantRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val _foodDetailState = MutableStateFlow<UiState<Food>>(UiState.Loading)
    val foodDetailState: StateFlow<UiState<Food>> = _foodDetailState.asStateFlow()

    private val _foodState = MutableStateFlow<UiState<Food>>(UiState.Idle)
    val foodState = _foodState.asStateFlow()

    private val _restaurantState = MutableStateFlow<UiState<Restaurant>>(UiState.Idle)
    val restaurantState: StateFlow<UiState<Restaurant>> = _restaurantState.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _selectedVariations = MutableStateFlow<Map<String, Set<String>>>(emptyMap())
    val selectedVariations: StateFlow<Map<String, Set<String>>> = _selectedVariations.asStateFlow()

    private val _specialInstructions = MutableStateFlow("")
    val specialInstruction: StateFlow<String> = _specialInstructions.asStateFlow()

    private val _addToCartState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addToCartState: StateFlow<UiState<Unit>> = _addToCartState.asStateFlow()

    private val _favorite = MutableStateFlow<Favorite?>(null)
    val favorite: StateFlow<Favorite?> = _favorite.asStateFlow()


    //cach nayf Không auto cancel request cũ
    //Nếu foodId đổi liên tục → có thể call thừa API
    //Không “reactive thuần”
//    fun loadDetailFood(foodId: String) {
//        viewModelScope.launch {
//            _foodState.value = UiState.Loading
//            val response = foodRepository.getFoodById(foodId)
//            val uiState = response.toUiState()
//            _foodState.value = uiState
//            if (uiState is UiState.Success) {
//                loadRestaurant(uiState.data.restaurantId)
//            }
//        }
//    }

    fun loadDetailFood(foodId: String) {
        viewModelScope.launch {
            _foodState.value = UiState.Loading
            val foodState = foodRepository.getFoodById(foodId).toUiState()
            _foodState.value = foodState
            if (foodState is UiState.Success) {
                _restaurantState.value = UiState.Loading
                val restaurant = restaurantRepository.getRestaurantById(foodState.data.restaurantId)
                    .first()
                    .toUiState()
                _restaurantState.value = restaurant
            }
        }
    }

    fun loadRestaurant(restaurantId: String) {
        viewModelScope.launch {
            val response = restaurantRepository.getRestaurantById(restaurantId)
                .first()
                .toUiState()
            _restaurantState.value = response
        }
    }

    fun incrementQuantity() {
        if (_quantity.value <= 20)
            _quantity.value++
    }


    fun decrementQuantity() {
        if (_quantity.value > 1) {
            _quantity.value--
        }
    }
    fun setQuantity(quantity: Int) {
        if (quantity > 1) {
            _quantity.value = quantity
        }
    }


//    fun selectVariation(variation: Variation, optionId: String) {
//        val current = _selectedVariations.value.toMutableMap()
//        val selectOptions = current[variation.id]?.toMutableSet() ?: mutableSetOf()
//
//        when (variation.type) {
//            Variation.VariationType.SINGLE -> {
//                selectOptions.clear()
//                selectOptions.add(optionId)
//            }
//
//            Variation.VariationType.MULTI -> {
//                if (!selectOptions.add(optionId))
//                //kt ko cho trung phan tu, add se that bai neu trung phan tu
//                //dung !de phu dinh false -> true, vi neu false dk se ko xay ra
//                {
//                    selectOptions.remove(optionId)
//                }
//            }
//        }
//        current[variation.id] = selectOptions
//        _selectedVariations.value = current
//    }

        fun selectVariation(optionId: String, variation: Variation) {
            val current = _selectedVariations.value.toMutableMap()
            val selectOption = current[variation.id]?.toMutableSet() ?: mutableSetOf()
            when (variation.type) {
                Variation.VariationType.SINGLE -> {
                    selectOption.clear()
                    selectOption.add(optionId)
                }

                Variation.VariationType.MULTI -> {
                    if (!selectOption.add(optionId)) {
                        selectOption.remove(optionId)
                    }
                }
            }
            current[variation.id] = selectOption
            _selectedVariations.value = current
        }

//        private fun calculateTotal(variation: Variation): Int {
//            val selectedOptions = _selectedVariations.value[variation.id]?.toList() ?: emptyList()
//            return variation.calculatePrice(selectedOptions)
//
//        }

        fun calculateTotalPrice(): Int {
            val food = _foodState.value.getDataOrNull() ?: return 0

            val basePrice = food.price
            val variationPrice = calculateVariationPrice(
                _selectedVariations.value, food
            )
            return (basePrice + variationPrice) * _quantity.value
        }


        //food = pizza() - selectVariation =  map("size" set("lard), "more" set(".."))
        // Variation(id = )
//    return food.variations.sumOf { variation ->
//        val selectedOptions = selected[variation.id].orEmpty()
//        variation.options
//            .filter { it.id in selectedOptions }
//            .sumOf { it.price }
//    }
        private fun calculateVariationPrice(
            selectVariation: Map<String, Set<String>>,
            food: Food
        ): Int {
            var count = 0
            food.variations.forEach { item ->
                val selectOption = selectVariation[item.id] ?: emptyList()
                item.options.forEach {
                    if (selectOption.contains(it.id)) {
                        count += it.price
                    }
                }
            }
            return count
        }

        fun resetSelection() {
            _quantity.value = 1
            _selectedVariations.value = emptyMap()
            _specialInstructions.value = ""
        }


        fun toggleFavorite(userId: String, foodId: String) {
            viewModelScope.launch {
                val current = _favorite.value

                if (current == null) {
                    val favorite = Favorite(userId = userId, foodId = foodId)
                    favoriteRepository.addFavorite(favorite)
                    _favorite.value = favorite
                } else {
                    favoriteRepository.removeFavorite(current.id)
                    _favorite.value = null
                }
            }
        }

//    fun addToCart(userId: String) {
//        viewModelScope.launch {
//            val food = _foodState.value.getDataOrNull()
//            val restaurant = _restaurantState.value.getDataOrNull()
//
//            if (food == null || restaurant == null) {
//                _addToCartState.value = UiState.Error("Not found food ")
//                return@launch
//            }
//            if (!validateVariation(food)) {
//                _addToCartState.value = UiState.Error("validate field")
//                return@launch
//            }
//
//            _addToCartState.value = UiState.Loading
//            val totalPrice = calculateTotalPrice()
//            val cartItems = CartItem(
//                foodId = food.foodId,
//                name = food.name,
//                price = totalPrice,
//                quantity = _quantity.value,
//                imgUrls = food.imgUrl,
//                restaurantId = food.restaurantId,
//                notes = _specialInstructions.value
//            )
//
//            when (val response = cartRepository.addItem(userId, cartItems)) {
//                is ApiResponse.Success -> {
//                    _addToCartState.value = UiState.Success(true)
//                }
//
//                is ApiResponse.Error -> {
//                    _addToCartState.value = UiState.Error(response.message)
//                }
//
//                else -> Unit
//            }
//        }
//    }

        fun addToCard(userId: String) {
            viewModelScope.launch {
                val food = _foodState.value.getDataOrNull()
                    ?: return@launch
//            emitError("Không tìm thấy món ăn")
                if (!validateVariation(food)) {
                    return@launch
                }

                _addToCartState.value = UiState.Loading

                val cartItem = CartItem(
                    foodId = food.foodId,
                    name = food.name,
                    price = calculateTotalPrice(),
                    quantity = _quantity.value,
                    imgUrls = food.imgUrl,
                    restaurantId = food.restaurantId,
                    notes = _specialInstructions.value,
                    variation = _selectedVariations.value
                )

                when (val result = cartRepository.addItem(userId, cartItem)) {
                    is ApiResponse.Success -> _addToCartState.value = UiState.Success(Unit)
                    is ApiResponse.Error -> emitError(result.message)
                    else -> Unit
                }
            }
        }

        private fun emitError(msg: String) {
            _addToCartState.value = UiState.Error(msg)
        }

        private fun validateVariation(food: Food): Boolean {
            return food.variations.all { variation ->
                val selected = _selectedVariations.value[variation.id] ?: return false
                when (variation.type) {
                    Variation.VariationType.SINGLE -> selected.size == 1
                    Variation.VariationType.MULTI -> selected.isNotEmpty()
                }
            }
        }

        fun resetAddToCartState() {
            _addToCartState.value = UiState.Idle
        }

    }


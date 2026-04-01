package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.FavoriteRepository
import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.Favorite
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.Review
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.domain.model.VariationOption
import com.example.foodapp.domain.model.toCartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val restaurantRepository: RestaurantRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _foodState = MutableStateFlow<UiState<Food>>(UiState.Loading)
    val foodState = _foodState.asStateFlow()

    private val _foodsState = MutableStateFlow<UiState<List<Food>>>(UiState.Idle)
    val foodsState = _foodsState.asStateFlow()

    private val _restaurantState = MutableStateFlow<UiState<Restaurant>>(UiState.Idle)
    val restaurantState: StateFlow<UiState<Restaurant>> = _restaurantState.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    //    private val _selectedVariations = MutableStateFlow<Map<String, Set<String>>>(emptyMap())
//    val selectedVariations: StateFlow<Map<String, Set<String>>> = _selectedVariations.asStateFlow()
    private val _selectedVariations =
        MutableStateFlow<Map<String, List<VariationOption>>>(emptyMap())
    val selectedVariations = _selectedVariations.asStateFlow()

    private val _specialInstructions = MutableStateFlow("")
    val specialInstruction: StateFlow<String> = _specialInstructions.asStateFlow()

    private val _addToCartState = MutableStateFlow<UiState<CartItem>>(UiState.Idle)
    val addToCartState: StateFlow<UiState<CartItem>> = _addToCartState.asStateFlow()

    private val _favorite = MutableStateFlow<Favorite?>(null)
    val favorite: StateFlow<Favorite?> = _favorite.asStateFlow()

    private val _reviewState = MutableStateFlow<UiState<List<Review>>>(UiState.Idle)
    val reviewState = _reviewState.asStateFlow()

    private val _addReviewState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addReviewState = _addReviewState.asStateFlow()

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

    fun loadRestaurantById(restaurantId: String) {
        viewModelScope.launch {
//            val response = restaurantRepository.getRestaurantById(restaurantId)
//                .first()
//                .toUiState()
            restaurantRepository.getRestaurantById(restaurantId).collectLatest { response ->
                _restaurantState.value = response.toUiState()
                Log.d("FoodDetailViewModel", "restaurant = ${restaurantState.value}")
            }
        }
    }

    fun loadFoodByRestaurant(restaurantId: String) {
        viewModelScope.launch {
            foodRepository.getFoodsByRestaurant(restaurantId).collectLatest { response ->
                _foodsState.value = response.toUiState()

                Log.d("add cart", "data food by restaurant ${foodsState.value}")

            }
        }

    }

    fun loadFoodById(foodId: String) {
        viewModelScope.launch {
            _foodState.value = foodRepository.getFoodById(foodId).toUiState()
            Log.d("FoodDetailViewModel", "FOOD DETAIL: ${foodState.value}")
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
        val currentOptions = current[variation.id]?.toMutableList() ?: mutableListOf()
        val option = variation.getOptionById(optionId) ?: return

        when (variation.type) {
            Variation.VariationType.SINGLE -> {
                currentOptions.clear()
                currentOptions.add(option)
            }

            Variation.VariationType.MULTI -> {
                val existing = currentOptions.find { it.id == optionId }
                if (existing != null) {
                    currentOptions.remove(existing)
                } else {
                    currentOptions.add(option)
                }
            }

        }
        current[variation.id] = currentOptions
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

    //chua toi uu
    private fun calculateVariationPrice(
        selected: Map<String, List<VariationOption>>,
        food: Food
    ): Int {
        return food.variations.sumOf { variation ->
            val selectedOptions = selected[variation.id].orEmpty()
            selectedOptions.sumOf { it.price }
        }
    }

    fun resetSelection() {
        _quantity.value = 1
        _selectedVariations.value = emptyMap()
        _specialInstructions.value = ""
    }


    fun toggleFavorite(foodId: String) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            val currentState = _favorite.value


            if (currentState == null) {
                val favorite = Favorite(userId = userId, foodId = foodId)
                favoriteRepository.addFavorite(favorite)
                _favorite.value = favorite
            } else {
                favoriteRepository.removeFavorite(currentState.favoriteId)
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

    fun addReview(foodId: String, review: Review) {
        viewModelScope.launch {
            _addReviewState.value = UiState.Loading
            val response = foodRepository.addReview(foodId, review)
            val uiState = response.toUiState()
            _addReviewState.value = uiState

            if (uiState.isSuccess()) {
                getReviews(foodId) //reload review
                loadDetailFood(foodId) //reload rating
            }
        }
    }


    //cách này tối ưu hơn cho addReview để ko gọi 3 lần firestore
//    fun addReview(foodId: String, review: Review) {
//        viewModelScope.launch {
//
//            _addReviewState.value = UiState.Loading
//
//            val response = foodRepository.addReview(foodId, review)
//            val uiState = response.toUiState()
//
//            _addReviewState.value = uiState
//
//            if (uiState is UiState.Success) {
//
//                // 1️⃣ Update review list local
//                val currentReviews =
//                    _reviewState.value.getDataOrNull().orEmpty()
//
//                _reviewState.value =
//                    UiState.Success(listOf(review) + currentReviews)
//
//                // 2️⃣ Update rating local
//                val currentFood =
//                    _foodState.value.getDataOrNull() ?: return@launch
//                val currentReview =
//                    _reviewState.value.getDataOrNull() ?: return@launch
//
//                val newCount = currentFood.reviewCount + 1
//                val newAverage =
//                    ((currentFood.reviewCount * currentFood.reviewCount)
//                            + review.rating) / newCount
//
//                val updatedFood = currentFood.copy(
//                    reviewCount = newCount,
//                    averageRate = newAverage
//                )
//
//                _foodState.value = UiState.Success(updatedFood)
//            }
//        }
//    }

    fun getReviews(foodId: String) {
        viewModelScope.launch {
//            _reviewState.value = UiState.Loading
//            val getReviews = foodRepository.getReviews(foodId)
//            val uiState = getReviews.toUiState()
//            _reviewState.value = uiState
            _reviewState.value = UiState.Loading
            _reviewState.value = foodRepository.getReviews(foodId).toUiState()
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            val food = _foodState.value.getDataOrNull() ?: return@launch
            val restaurantState = _restaurantState.value
            val restaurant = restaurantState.getDataOrNull() ?: return@launch

            if (!validateVariation(food)) {
                emitError("Vui lòng chọn đầy đủ tùy chọn")
                return@launch
            }
            _addToCartState.value = UiState.Loading
            val cartItem = food.toCartItem(
                _quantity.value,
                specialInstructions = _specialInstructions.value,
                selectedVariations = _selectedVariations.value
            )

            when (val result =
                cartRepository.addItem(item = cartItem, userId = userId, restaurant = restaurant)) {
                is ApiResponse.Success -> _addToCartState.value = UiState.Success(cartItem)
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
            val selected = _selectedVariations.value[variation.id]
            if (!variation.required && (selected == null || selected.isEmpty())) return@all true
            selected ?: return false

            when (variation.type) {
                Variation.VariationType.SINGLE -> selected.size == 1
                Variation.VariationType.MULTI -> selected.isNotEmpty()
            }
        }
    }


    fun resetAddToCartState() {
        _addToCartState.value = UiState.Idle
    }

    fun selectedFood(food: Food) {
        _foodState.value = UiState.Success(food)
    }
}


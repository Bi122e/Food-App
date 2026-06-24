package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.FavoriteRepository
import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.domain.model.Favorite
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val restaurantRepository: RestaurantRepository,
    private val favoriteRepository: FavoriteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _event =
        MutableSharedFlow<FoodAction>() //lang nge user click food, dua vao do de xu ly tiep
    val event = _event.asSharedFlow()

    private val _foodState = MutableStateFlow<UiState<Food>>(UiState.Loading)
    val foodState = _foodState.asStateFlow()

    private val _foodsState = MutableStateFlow<UiState<List<Food>>>(UiState.Idle)
    val foodsState = _foodsState.asStateFlow()

    private val _restaurantState = MutableStateFlow<UiState<Restaurant>>(UiState.Idle)
    val restaurantState: StateFlow<UiState<Restaurant>> = _restaurantState.asStateFlow()

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

    //    fun loadDetailFood(foodId: String) {
//        viewModelScope.launch {
//            _foodState.value = UiState.Loading
//            val foodState = foodRepository.getFoodById(foodId).toUiState()
//            _foodState.value = foodState
//            if (foodState is UiState.Success) {
//                _restaurantState.value = UiState.Loading
//                val restaurant = restaurantRepository.getRestaurantById(foodState.data.restaurantId)
//                    .first()
//                    .toUiState()
//                _restaurantState.value = restaurant
//            }
//        }
//    }
    fun loadDetailFood(foodId: String) {
        viewModelScope.launch {
            _foodState.value = UiState.Loading
            val food = foodRepository.getFoodById(foodId).toUiState()
            _foodState.value = food
        }
    }

    fun loadRestaurantById(restaurantId: String) {
        viewModelScope.launch {
//            val response = restaurantRepository.getRestaurantById(restaurantId)
//                .first()
//                .toUiState()
            val response = restaurantRepository.getRestaurantById(restaurantId)
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("check_VMrestaurantDetail", "success ${response.data}")
                    _restaurantState.value = UiState.Success(response.data)
                }

                is ApiResponse.Error -> {
                    Log.d("check_VMrestaurantDetail", "error ${response.message}")
                    _restaurantState.value = UiState.Error(response.message)
                }
                else -> {
                    Log.d("check_VMrestaurantDetail", "else")
                }
            }
            Log.d("check_VMrestaurantDetail", "state = ${restaurantState.value}")
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


    fun resetSelection() {
        // Keeping it for potential other resets, but it's currently mostly empty
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
    //                val currentReviews =
//                    _reviewState.value.getDataOrNull().orEmpty()
//
//                _reviewState.value =
//                    UiState.Success(listOf(review) + currentReviews)
//
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

    fun resetAddToCartState() {
        // Placeholder if needed, but the state was moved to CartViewModel
    }

    // logic xử lý khi user click chọn món ăn, mỗi lần user click là emit tự phát để UI collect
    fun selectedFood(food: Food) {
        val restaurant = (_restaurantState.value as? UiState.Success)?.data ?: return
        _foodState.value = UiState.Success(food)
        Log.d("selectedFood", "selected = ${_foodState.value}")
        Log.d("selectedFood", "selected = ${restaurant}")

        viewModelScope.launch {
            if (food.variations.isNotEmpty()) {
                loadDetailFood(food.foodId)
                _event.emit(FoodAction.OpenDetail(food.foodId))
                Log.d("selectedFood", "open")
            } else {
                _event.emit(FoodAction.AddToCart(food, restaurant))
                Log.d("selectedFood", "ko open =  ")

            }
//            _event.emit(FoodAction.ShowMessage(_foodState.value.toString()))
        }
        Log.d("selectedFood", "selected = ${_foodState.value}")
    }

}

sealed class FoodAction {
    data class AddToCart(val food: Food, val restaurant: Restaurant) : FoodAction()
    data class OpenDetail(val foodId: String) : FoodAction()
//    data class ShowMessage(val message: String) : FoodAction()
}


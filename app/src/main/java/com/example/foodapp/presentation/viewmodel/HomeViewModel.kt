package com.example.foodapp.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Notification
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.CategoryRepository
import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.data.repository.NotificationRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.domain.model.Category
import com.example.foodapp.domain.model.CategoryType
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.presentation.state.HomeUiState
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val restaurantRepository: RestaurantRepository,
    private val categoryRepository: CategoryRepository,
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categories: StateFlow<UiState<List<Category>>> = _categories.asStateFlow()

    private val _foods = MutableStateFlow<UiState<List<Food>>>(UiState.Loading)
    val foods = _foods.asStateFlow()

    private val _featureFoods = MutableStateFlow<UiState<List<Food>>>(UiState.Loading)
    val featureFoods: StateFlow<UiState<List<Food>>> = _featureFoods.asStateFlow()

    private val _popularFoods = MutableStateFlow<UiState<List<Food>>>(UiState.Loading)
    val popularFood: StateFlow<UiState<List<Food>>> = _popularFoods.asStateFlow()

    private val _restaurants = MutableStateFlow<UiState<List<Restaurant>>>(UiState.Loading)
    val restaurants: StateFlow<UiState<List<Restaurant>>> = _restaurants.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()
    private val _foodByCategory = MutableStateFlow<UiState<List<Food>>>(UiState.Loading)
    val foodByCategory: StateFlow<UiState<List<Food>>> = _foodByCategory.asStateFlow()

    private val _foodByRestaurant = MutableStateFlow<UiState<List<Food>>>(UiState.Loading)
    val foodByRestaurant = _foodByRestaurant.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResult = MutableStateFlow<UiState<List<Food>>>(UiState.Loading)
    val searchResult: StateFlow<UiState<List<Food>>> = _searchResult.asStateFlow()

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()
    private var searchJob: Job? = null
    private var loadRestaurantsJob: Job? = null
    private var loadRestaurantsByCateJob: Job? = null
    private var loadRestaurantsByRandomJob: Job? = null




    init {
        Log.e("VM_INSTANCE", this.toString())
    }

    init {

        try {

            Log.d("INITFILE", "1")
            loadCategories()

            Log.d("INITFILE", "2")
            loadFeaturedFood()

            Log.d("INITFILE", "3")
            loadPopularFood()

            Log.d("INITFILE", "4")

            Log.d("INITFILE", "5")
//            observeOrders()

            Log.d("INITFILE", "6")
            observeResByCategory()

            Log.d("INITFILE", "7")
            loadResByRandom()

            loadInitialRestaurants()
            observeOrders()

            observeBadge()

        } catch (e: Exception) {

            Log.e("INITFILE", "CRASH INIT", e)
        }
    }
    private fun observeBadge() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            notificationRepository.getUnreadCount(userId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        Log.d("checkVM_observeBadge", "error ${response.message}")
                    }
                    is ApiResponse.Success -> {
                        Log.d("checkVM_observeBadge", "success ${response.data}")
                        _homeUiState.update { it.copy(badgeCount = response.data) }
                    }
                    else ->  {
                        Log.d("checkVM_observeBadge", "else")
                    }
                }
                Log.d("checkVM_observeBadge", "state ${_homeUiState.value.badgeCount}")
            }
        }
    }

    private fun loadInitialRestaurants() {


        viewModelScope.launch {


            _homeUiState.update {
                it.copy(
                    restaurants = UiState.Loading,
                 )
            }

            when (val response = restaurantRepository.getRestaurants(null)) {

                is ApiResponse.Success -> {

                    _homeUiState.update {
                        it.copy(
                            restaurants = UiState.Success(response.data.data),
                         )
                    }
                    Log.d("loadInitialRestaurants", "${response.data}")
                    Log.d("loadInitialRestaurants", "${response.data.data.size}")
                    Log.d("loadInitialRestaurants", "${_homeUiState.value.restaurants}")
                }
                is ApiResponse.Error -> {

                    _homeUiState.update {
                        it.copy(
                            restaurants = UiState.Error(
                                response.message
                            )
                        )
                    }
                }
                else -> {}
            }
        }

    }





    fun loadResByRandom() {
//        if (_homeUiState.value.isLoadingMoreRandom) return
        viewModelScope.launch {
//            _homeUiState.update { it.copy(isLoadingMoreRandom = true) }

            val response = restaurantRepository.getRandomRestaurantsByDifferentTags()

            Log.d("HomeViewModelState", "loadResByRandom response = $response")

            val result = response.toUiState()

            _homeUiState.update {
//                val old = it.restaurantByRandom.getDataOrNull().orEmpty()
//                val new = response.getDataOrNull().orEmpty()
                it.copy(restaurantByRandom = result)
            }
//            _homeUiState.update { it.copy(isLoadingMoreRandom = false) }
            Log.d(
                "HomeViewModelState",
                "loadResByRandom response = ${homeUiState.value.restaurantByRandom}"
            )
        }
    }

    fun observeResByCategory() {
        loadRestaurantsByCateJob?.cancel()
        loadRestaurantsByCateJob = restaurantRepository.getRestaurantsByCategory(category = "com")
            .onEach { response ->
                _homeUiState.update {
                    it.copy(restaurantsByCategory = response.toUiState())
                }
                Log.d("HomeViewModelState", "Restaurant category: ${response}")
            }
            .launchIn(viewModelScope)
    }

    //HomeViewModelState
    @SuppressLint("SuspiciousIndentation")

    private fun observeOrders() {
        val userId = authRepository.currentUserId() ?: return
        Log.d("observeOrders", "run")
        orderRepository.getOrderIncomplete(userId)
            .onEach { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _homeUiState.update {
                            it.copy(
                                oder = response.data
                            )
                        }
                        Log.d("check_show_progress_flow", "VM SS: ${homeUiState.value.oder}")
                        Log.d("observeOrders", "succes ${_homeUiState.value.oder.size}")
                    }

                    else -> {
                        Log.d("check_show_progress_flow", "VM error: ${homeUiState.value.oder}")
                        Log.d("observeOrders", "error ${_homeUiState.value.oder.toString()}")

                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { response ->
                val uiState = response.toUiState()
                _categories.value = uiState
                Log.d("HomeViewModel", "Categories state: ${_categories.value}")
            }
        }
    }


    private fun loadPopularFood() {
        viewModelScope.launch {
            foodRepository.getPopularFoods(10).collect { response ->
                _popularFoods.value = response.toUiState()
            }
        }
    }


    private fun loadFeaturedFood() {
        viewModelScope.launch {
            Log.d("Featured", "loadFeaturedFood CALLED")
            foodRepository.getFeaturedFood(10).collect { response ->
                Log.d("Featured", "Featured emit: $response")

                _featureFoods.value = response.toUiState()

                Log.d("Featured", "Updated state ${_featureFoods.value}")
            }
        }
    }


    fun selectedCategory(category: Category) {
        _selectedCategory.value = category
        if (category.type == CategoryType.ALL) {
            loadPopularFood()
        } else {
            loadFoodByCategory(category.categoryId)
        }
    }

    fun loadFoodByRestaurant(restaurantId: String) {
        viewModelScope.launch {
            Log.d("HomeViewModel", "run run --- ${foodByRestaurant.value}")
            foodRepository.getFoodsByRestaurant(restaurantId).collect { response ->
                _foodByRestaurant.value = response.toUiState()
            }
            Log.d("HomeViewModel", "list food === ${foodByRestaurant.value}")
        }
        Log.d("HomeViewModel", "list food === ${foodByRestaurant.value}")

    }

    private fun loadFoodByCategory(categoryId: String) {
        viewModelScope.launch {
            val response = categoryRepository.getCategoryById(categoryId)
            response.toUiState()
        }
    }


    fun updateSearchQuery(query: String) {
        _searchResult.value = UiState.Loading

        _searchQuery.value = query
//        if (query.isNotEmpty()) {
//            searchFood(query)
//        } else {
//            _searchResult.value = UiState.Success(emptyList())
//        }
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchResult.value = UiState.Success(emptyList())
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)
            searchFood(query)
        }
    }

    private fun searchFood(query: String) {
        viewModelScope.launch {
            _searchResult.value = UiState.Loading

            val response = foodRepository.searchFoods(query)
            _searchResult.value = response.toUiState()
        }
    }

    fun refreshRandomRestaurants() {

    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResult.value = UiState.Success(emptyList())
    }


}
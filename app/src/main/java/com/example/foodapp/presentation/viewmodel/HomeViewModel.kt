package com.example.foodapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.CategoryRepository
import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.domain.model.Category
import com.example.foodapp.domain.model.CategoryType
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val restaurantRepository: RestaurantRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categories: StateFlow<UiState<List<Category>>> =_categories.asStateFlow()

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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResult = MutableStateFlow<UiState<List<Food>>>(UiState.Loading)
    val searchResult: StateFlow<UiState<List<Food>>> = _searchResult.asStateFlow()

    init {
        loadCategories()
        loadFeaturedFood()
        loadPopularFood()
        loadRestaurants()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { response ->
                _categories.value = response.toUiState()
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
            foodRepository.getFeaturedFood(10).collect { response ->
                _featureFoods.value = response.toUiState()
            }
        }
    }

    private fun loadRestaurants() {
        viewModelScope.launch {
            restaurantRepository.getRestaurants().collect { response ->
                _restaurants.value = response.toUiState()
            }
        }
    }

    fun selectedCategory(category: Category) {
        _selectedCategory.value = category
        if (category.type == CategoryType.ALL) {
            loadPopularFood()
        } else {
            loadFoodByCategory(category.id)
        }
    }

    private fun loadFoodByCategory(categoryId: String) {
        viewModelScope.launch {
            val response = categoryRepository.getCategoryById(categoryId)
            response.toUiState()
        }
    }


    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            searchFood(query)
        } else {
            _searchResult.value = UiState.Success(emptyList())
        }
    }

    private fun searchFood(query: String) {
        viewModelScope.launch {
            var search = _searchResult.value
            search = UiState.Loading
            val response = foodRepository.searchFoods(query)
            search = response.toUiState()

        }
    }
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResult.value = UiState.Success(emptyList())
    }


}
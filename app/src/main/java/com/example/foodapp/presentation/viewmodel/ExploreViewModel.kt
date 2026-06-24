package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.presentation.state.ExploreUiState
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExploreViewModel @Inject constructor(
    val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val _exploreUiState = MutableStateFlow<ExploreUiState>(ExploreUiState())
    val exploreUiState = _exploreUiState.asStateFlow()
    private val _restaurantsSnapshot = MutableStateFlow<DocumentSnapshot?>(null)
     //snapshot lai item cuoi, de goi lai bat dau tu snapshot nay
    private var currentTag: String? = null

    fun searchRestaurants() {
        val query = _exploreUiState.value.text
        if (query.isEmpty()) return
        viewModelScope.launch {
            when (val response = restaurantRepository.searchRestaurants(query)) {
                is ApiResponse.Success -> {
                    _exploreUiState.update { it.copy(restaurants = UiState.Success(response.data.data)) }
                    _restaurantsSnapshot.value = response.data.lastDoc
                    Log.d("searchRestaurants", "searchRestaurants ${response.getDataOrNull().toString()}")

                }

                is ApiResponse.Empty -> {
                    _exploreUiState.update { it.copy(restaurants = UiState.Empty()) }
                }

                is ApiResponse.Error -> {
                    Log.d("searchRestaurants", "searchRestaurants ${ response.message }")
                }

                else -> {
                }
            }
        }
    }

    fun loadInitialRestaurants(tag: String?) {
        Log.d("getRestaurants", "tag $tag")
        Log.d("getRestaurants", "currentTag $currentTag")

        currentTag = tag
        Log.d("check_InitialRestaurants", "loadInitialRestaurants tag = ${tag} currentTag = $currentTag ")
        Log.d("Check_getAllRestaurants", "${tag} currentTag = $currentTag ")

        viewModelScope.launch {


            _exploreUiState.update {
                it.copy(
                    restaurants = UiState.Loading,
                )
            }

            when (val response = restaurantRepository.getRestaurants(tag)) {

                is ApiResponse.Success -> {
                    _restaurantsSnapshot.value = response.data.lastDoc
                    _exploreUiState.update {
                        it.copy(
                            restaurants = UiState.Success(response.data.data),
                        )
                    }
                    Log.d("loadInitialRestaurants", "${response.data}")
                    Log.d("loadInitialRestaurants", "${response.data.data.size}")
                    Log.d("loadInitialRestaurants", "${_exploreUiState.value.restaurants}")
                    Log.d("check_InitialRestaurants", "loadInitialRestaurants and $tag ${_exploreUiState.value.restaurants}")

                }

                is ApiResponse.Error -> {

                    _exploreUiState.update {
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

    fun loadMoreRestaurants() {

        // isEndReached = restaurants.size < 3, tư duy là isEndReachedRestaurants load 1,2,3
        // do chỉ chạy 1-3 nên return ko update dc
        if (
            _exploreUiState.value.isLoadingRestaurant ||
            _exploreUiState.value.isEndReachedRestaurants  //cần kt còn dữ liệu trên fb ko, nếu ko kt thì hết data sẽ trả về null -> sai kiến trúc
        ) return
        viewModelScope.launch {
            _exploreUiState.update { it.copy(isLoadingRestaurant = true) }
            val response =
                restaurantRepository.getAllRestaurants(
                    lastDoc = _restaurantsSnapshot.value,
                    category = _exploreUiState.value.text
                )

            when (response) {
                is ApiResponse.Success -> {
                    val oldRes = _exploreUiState.value.restaurants.getDataOrNull() ?: emptyList()
                    val newRes = response.data.data
                    Log.d("searchRestaurants", "loadMoreRestaurants ${response.data}")
                    Log.d("loadMoreRestaurants", "data = ${response.data}, lastDoc = ${_restaurantsSnapshot.value}")
                    _restaurantsSnapshot.value = response.data.lastDoc
                    _exploreUiState.update {
                        it.copy(
                            restaurants = UiState.Success(oldRes + newRes),
                            isEndReachedRestaurants = response.data.isEndReached
                        )
                    }
                    Log.d(
                        "loadMoreRestaurants",
                        "sucess ${_exploreUiState.value.restaurants.getDataOrNull()?.size}"
                    )

                }

                is ApiResponse.Error -> {
                    _exploreUiState.update {
                        it.copy(
                            restaurants = UiState.Error(response.message)
                        )
                    }
                }

                else -> {}
            }
            _exploreUiState.update { it.copy(isLoadingRestaurant = false) }

        }
    }

    fun loadSuggestionRestaurant() {

        viewModelScope.launch {
            val response =
                restaurantRepository.getAllRestaurants(lastDoc = null, category = "banh-mi")

            when (response) {
                is ApiResponse.Success -> {
                    Log.d("searchRestaurants", "loadSuggestionRestaurant ${response.data}")

                    Log.d("loadMoreRestaurants", "${response.data}")
                    _exploreUiState.update {
                        it.copy(
                            restaurantSuggestion = UiState.Success(response.data.data)
                        )
                    }
                    Log.d(
                        "loadMoreRestaurants",
                        "sucess ${_exploreUiState.value.restaurants.getDataOrNull()?.size}"
                    )

                }

                is ApiResponse.Error -> {
                    _exploreUiState.update {
                        it.copy(
                            restaurantSuggestion = UiState.Error(response.message)
                        )
                    }
                }

                else -> {}
            }

        }
    }


    fun resetRestaurants() {
        _exploreUiState.update {
            it.copy(restaurants = UiState.Empty())
        }
        Log.d("searchRestaurants", "resetRestaurants ${_exploreUiState.value.restaurants}")

    }

    fun setQueryOrTag(text: String) {
        _exploreUiState.update {
            it.copy(text = text)
        }
    }

    fun getCurrentQuery(): String {
        return _exploreUiState.value.text
    }


}


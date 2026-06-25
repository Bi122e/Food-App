package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.data.repository.PreviewRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.presentation.state.PreviewRestaurantUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PreviewRestaurantViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    private val previewRepository: PreviewRepository,
) : ViewModel() {

    private val _previewRestaurantUiState = MutableStateFlow(PreviewRestaurantUiState())
    val previewRestaurantUiState = _previewRestaurantUiState.asStateFlow()

    fun observePreviews(restaurantId: String) {
        viewModelScope.launch {
            previewRepository.observePreviews(restaurantId).collectLatest { response ->

                when (response) {
                    is ApiResponse.Success -> {
                        Log.d("checkVM_observePreviews", "Success")
                        _previewRestaurantUiState.update {
                            it.copy(
                                previews = UiState.Success(response.data)
                            )
                        }
                    }

                    is ApiResponse.Error -> {
                        Log.d("checkVM_observePreviews", "error: ${response.message}")
                        _previewRestaurantUiState.update {
                            it.copy(
                                previews = UiState.Error(response.message)
                            )
                        }
                    }
                    else -> {
                        Log.d("checkVM_observePreviews", "else")
                    }
                }
                Log.d(
                    "checkVM_observePreviews",
                    "state ${_previewRestaurantUiState.value.previews}"
                )
            }
        }
    }

    fun observeRestaurant(restaurantId: String) {
        viewModelScope.launch {
            restaurantRepository.observeRestaurants(restaurantId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        Log.d("checkVM_obverseRestaurant", "sucess")
                        _previewRestaurantUiState.update {
                            it.copy(
                                restaurants = UiState.Success(response.data)
                            )
                        }
                    }

                    is ApiResponse.Error -> {
                        Log.d(
                            "checkVM_obverseRestaurant", "error: ${
                                response.message
                            }"
                        )
                        _previewRestaurantUiState.update {
                            it.copy(
                                restaurants = UiState.Error(response.message)
                            )
                        }
                    }

                    is ApiResponse.Empty -> {
                        Log.d("checkVM_obverseRestaurant", "empty")
                        _previewRestaurantUiState.update {
                            it.copy(
                                restaurants = UiState.Loading
                            )
                        }
                    }

                    else -> {
                        Log.d("checkVM_obverseRestaurant", "else")
                    }
                }
                Log.d(
                    "checkVM_obverseRestaurant",
                    "state: ${_previewRestaurantUiState.value.restaurants}"
                )

            }
        }
    }
}
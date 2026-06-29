package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.FavoriteRepository
import com.example.foodapp.domain.model.Favorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val favoriteRepository: FavoriteRepository,
): ViewModel() {
    private val _favorites = MutableStateFlow<UiState<Map<String, Favorite>>>(UiState.Idle)
    val favorites = _favorites.asStateFlow()

    init {
        loadFavorites()
        Log.d("Favorite", "Init run ${_favorites.value}")
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            if (userId.isNullOrEmpty()) {
                Log.d("Favorite", "Cannot toggle favorite")
                return@launch
            } else {
                Log.d("Favorite", "userid = $userId")

            }
            favoriteRepository.observeFavorites(userId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _favorites.value = UiState.Success(response.data.associateBy{ it.foodId })
                        Log.d("Favorite", "Load Favorites ${_favorites.value}")
                    }
                    is ApiResponse.Error -> {
                        _favorites.value = UiState.Error(response.message)
                    }
                    else -> {}
                }
            }
            Log.d("Favorite", "Load Favorites ${_favorites.value}")
        }
    }

    fun toggleFavorite(foodId: String) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            val currentState = _favorites.value
            //cần kiểm tra food id có tồn tại trong state hay k
            val currentMap = (currentState as? UiState.Success)?.data ?: emptyMap()
            val existing = currentMap[foodId]
            if (existing != null) {
                _favorites.value = UiState.Success(currentMap - foodId)
                favoriteRepository.removeFavorite(existing.favoriteId)

            } else {
                val favorite = Favorite(foodId = foodId, userId = userId)
                _favorites.value = UiState.Success(currentMap + (foodId to favorite))
                favoriteRepository.addFavorite(favorite)
            }
        }
        Log.d("Favorite", "toggle Favorites ${_favorites.value}")

    }
}
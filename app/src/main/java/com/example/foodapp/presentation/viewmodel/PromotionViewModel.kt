package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.PromotionRepository
import com.example.foodapp.domain.model.Promotion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PromotionViewModel @Inject constructor(
    val promotionRepository: PromotionRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Promotion>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Promotion>>> = _uiState.asStateFlow()

    init {
        loadPromotions()
    }
    fun loadPromotions() {
        viewModelScope.launch {
            val response = promotionRepository.getPromotions().toUiState()
            _uiState.value = response
            Log.d("PromotionViewModel", "${_uiState.value}")
        }
    }


}
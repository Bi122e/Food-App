package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.NotificationRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.PreviewRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.domain.model.RestaurantPreview
import com.example.foodapp.presentation.state.CompleteEventState
import com.example.foodapp.presentation.state.CompleteUiState
import com.example.foodapp.presentation.state.toMappingRatingCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class CompleteViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val previewRepository: PreviewRepository,
    private val authRepository: AuthRepository,
    private val restaurantRepository: RestaurantRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {


    private val _completeUiState = MutableStateFlow(CompleteUiState())
    val completeUiState = _completeUiState.asStateFlow()

    private val _event = MutableSharedFlow<CompleteEventState>()
    val event = _event.asSharedFlow()

    fun loadRestaurant(restaurantId: String) {
        Log.d("checkCompleteVM_loadRestaurant", "run")
        viewModelScope.launch {
            val response = restaurantRepository.getRestaurantById(restaurantId)

            when (response) {
                is ApiResponse.Success -> {
                    Log.d("checkCompleteVM_loadRestaurant", "success ${response.data}")
                    _completeUiState.update {
                        it.copy(
                            restaurantName = response.data.restaurantName,
                            restaurantImgUrls = response.data.coverImage,
                        )
                    }
                }

                is ApiResponse.Error -> {
                    Log.d("checkCompleteVM_loadRestaurant", "error ${response.message}")
                }

                else -> {
                    Log.d("checkCompleteVM_loadRestaurant", "else")
                }
            }

        }
    }

    fun loadOrder(orderId: String) {
        Log.d("checkCompleteVM_loadOrder", "run")
        viewModelScope.launch {
            _completeUiState.update { it.copy(isOrderLoading = true) }
            val response =
                orderRepository.observeOrderById(orderId = orderId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _completeUiState.update { it.copy(isOrderError = true) }
                            Log.d("checkCompleteVM_loadOrder", "error ${response.message}")
                        }

                        is ApiResponse.Success -> {
                            _completeUiState.update {
                                it.copy(
                                    restaurantId = response.data.restaurantId,
                                    userName = response.data.userName,
                                )
                            }
                            Log.d("checkCompleteVM_loadOrder", "success: data = ${response.data}")
                            loadRestaurant(restaurantId = _completeUiState.value.restaurantId)
                            resetOrderState()
                        }

                        else -> {
                            Log.d("checkCompleteVM_loadOrder", "else")
                        }
                    }
                    Log.d("checkCompleteVM_loadOrder", "check state: ${_completeUiState.value}")
                    _completeUiState.update { it.copy(isOrderLoading = false) }
                }
        }

    }

    fun createComplete(orderId: String, notificationId: String) {
        _completeUiState.update { it.copy(isCreateLoading = true) }

        viewModelScope.launch {

            Log.d("checkCompleteVM_loadOrder", "run")

            val userId = authRepository.currentUserId() ?: return@launch

            val preview = RestaurantPreview(
                previewId = "",
                userName = if (_completeUiState.value.isPrivateName)
                    privateName()
                else
                    _completeUiState.value.userName,
                userId = userId,
                orderId = orderId,
                restaurantId = _completeUiState.value.restaurantId,
                rating = _completeUiState.value.rating ?: 0,
                message = _completeUiState.value.message,
                imageUrls = "", //de xu ly sau
                avatarUrls = "", //de xu ly sau
                previewTags = _completeUiState.value.previewTags
            )
            val response = previewRepository.createPreview(orderId, preview)

            when (response) {
                is ApiResponse.Error -> {
                    Log.d("checkCompleteVM_createComplete", "error ${response.message}")
                    _event.emit(CompleteEventState.Error("Có lỗi xảy ra, hãy thử lại"))
//                    resetCreateState()
                }

                is ApiResponse.Success -> {
                    Log.d("checkCompleteVM_createComplete", "success ${response.data}")

                    val responseDeactivate =
                        notificationRepository.deactivateNotification(notificationId)
                    when (responseDeactivate) {
                        is ApiResponse.Success -> {
                            Log.d("checkVM_responseDeactivate", "success")

                            val restaurantId = _completeUiState.value.restaurantId

                            val rating = _completeUiState.value.rating.toMappingRatingCount()

                            if (rating == null) {
                                Log.d(
                                    "checkVM_updateRatingCount",
                                    "null: ${completeUiState.value.rating}"
                                )
                                return@launch
                            }
                            val responseRatingCount =
                                restaurantRepository.updateRatingCount(restaurantId, rating)

                            when (responseRatingCount) {
                                is ApiResponse.Success -> {
                                    _event.emit(CompleteEventState.Success)
                                    Log.d("checkVM_updateRatingCount", "success")
                                }

                                is ApiResponse.Error -> {
                                    _event.emit(CompleteEventState.Error("Có lỗi xảy ra, hãy thử lại"))
                                    Log.d(
                                        "checkVM_updateRatingCount",
                                        "error: ${responseRatingCount.message}"
                                    )
                                }

                                else -> {
                                    Log.d("checkVM_updateRatingCount", "else")

                                }
                            }
                        }

                        is ApiResponse.Error -> {
                            _event.emit(CompleteEventState.Error("Có lỗi xảy ra, hãy thử lại"))
                            Log.d(
                                "checkVM_responseDeactivate",
                                "error ${responseDeactivate.message}"
                            )
                        }

                        else -> {
                            _event.emit(CompleteEventState.Error("Có lỗi xảy ra, hãy thử lại"))
                            Log.d("checkVM_responseDeactivate", "else")
                        }
                    }

                }

                else -> {
                    _event.emit(CompleteEventState.Error("Có lỗi xảy ra, hãy thử lại"))
                    Log.d("checkCompleteVM_createComplete", "else")
                }
            }
        }
        _completeUiState.update { it.copy(isCreateLoading = false) }
    }

//
//    private fun resetCreateState() {
//        _completeUiState.update {
//            it.copy(
//                isCreateError = false
//            )
//        }
//    }

    private fun resetOrderState() {
        _completeUiState.update {
            it.copy(
                isOrderError = false
            )
        }
    }


    fun setRating(value: Int) {
        _completeUiState.update {
            it.copy(
                rating = value
            )
        }
    }

    fun setMessage(value: String) {
        _completeUiState.update {
            it.copy(
                message = value
            )
        }
    }

    fun addPreviewTag(value: String) {
        _completeUiState.update {
            it.copy(
                previewTags = it.previewTags + value
            )
        }
    }

    fun removePreviewTag(value: String) {
        _completeUiState.update {
            it.copy(
                previewTags = it.previewTags - value
            )
        }
    }

    fun setPrivate(value: Boolean) {
        _completeUiState.update {
            it.copy(
                isPrivateName = value
            )
        }
    }

    private fun privateName(): String {
        val random = Random.nextInt(1000)
            .toString()
            .padStart(3)
        return "Người dùng ẩn danh $random"
    }
}

package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.UserProfileCombineRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.presentation.state.toEditProfile
import com.example.foodapp.presentation.state.toUserProfileCombine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val userProfileCombineRepository: UserProfileCombineRepository,
    private val restaurantRepository: UserRepository

) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            _uiState.update { it.copy(isLoading = true) }
            userProfileCombineRepository.getUserProfile(userId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        Log.d("UserProfileViewModel", "api success")

                        val data = response.data

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                user = data,
                                editProfile = data.toEditProfile(),
                                profileCompleteness =
                                    if (data.isProfileComplete()) ProfileCompleteness.COMPLETE
                                    else ProfileCompleteness.INCOMPLETE

                            )

                        }
                        Log.d("UserProfileViewModel", "get user profile| User combine: $response")
                    }


                    is ApiResponse.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = mapOf("ApiResponse" to response.message)
                            )
                        }
                    }

                    else -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = mapOf("ApiResponse" to "UNKNOW")) }

                    }
                }
            }
        }
    }

    fun updateUserProfile() {
        val userProfile = _uiState.value.toUserProfileCombine() ?: return
        val phone = _uiState.value.editProfile.phone
        if (phone.length < 12)  {
            _uiState.update {
                it.copy(
                    errorMessage = mapOf(
                        "phone" to "Số điện thoại phải lớn hơn 10 và nhỏ hơn 11"))
            }
            return
        }


        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val response = userProfileCombineRepository.updateCustomerProfile(userProfile)
            if (response is ApiResponse.Success) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditMode = false,
                        user = userProfile,
                        isClickedUpdate = false,
                        isEnable = false,
                        editProfile = userProfile.toEditProfile(),
                        profileCompleteness =
                            if (userProfile.isProfileComplete()) ProfileCompleteness.COMPLETE
                            else ProfileCompleteness.COMPLETE,
                        successMessage = "Profile updated successfully",

                    )
                }
            }
            if (response is ApiResponse.Error) {
                _uiState.update { it.copy(isLoading = false, errorMessage = mapOf("ApiResponse" to response.message)) }

            }
            else {
                _uiState.update { it.copy(isLoading = false, errorMessage = mapOf("ApiResponse" to "UNKNOW")) }
            }
        }
    }

//    fun onFieldChangeT(field: String, value: String) {
//        _uiState.update {
//            it.copy(
//                isLoading = false,
//                isEditMode = true,
//                editProfile = when (field) {
//                    "name" -> {it.editProfile.copy(name = value)}
//                    "address" -> {it.editProfile.copy(address = value)}
//                    "phone" -> {it.editProfile.copy(phone = value)}
//                    else -> it.editProfile
//                }
//            )
//        }
//    }

    fun onFieldChange(field: String, value: String) {

        _uiState.update { current ->

            //gán gias trị new input cho biến edit
            val editedProfile = when (field) {
                "name" -> current.editProfile.copy(name = value)
                "address" -> {
                     current.editProfile.copy(address = value)
                }
                "phone" -> {

                    val isValid = value.matches(Regex("^[0-9]*$"))
                    Log.d("PHONE_INPUT", "value = $value, isValid = $isValid")
                    val editProfile = _uiState.value.editProfile
                    if (isValid) current.editProfile.copy(phone = value)
                    else current.editProfile

                }
                else -> current.editProfile
            }

            // so sánh giá trị mới với giá trị cũ (giá trị cũ sẽ ko cập nhật nếu chỉ đổi state)
            val isEditing = editedProfile != current.user?.toEditProfile()
            //current -> ui combine map sang edit profile state

            //cập nhật state
            current.copy(

                editProfile = editedProfile,
                isEditMode = isEditing // true khi đang gõ
            )
        }
    }

    fun onCheckedChange() {
        val reverse = !_uiState.value.isClickedUpdate
        _uiState.update {
            it.copy(isClickedUpdate = reverse)
        }
    }

    fun setEnable(value: Boolean = false): Boolean {
         _uiState.update { it.copy(isEnable = value) }
        return value
    }



}
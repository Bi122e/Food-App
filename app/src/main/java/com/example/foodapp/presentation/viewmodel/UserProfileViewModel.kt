package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.presentation.state.toEditProfile
import com.example.foodapp.presentation.state.toUser
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
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    private val phoneRegex = Regex("^[0-9]*$")

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            _uiState.update { it.copy(isLoading = true) }
            userRepository.getCurrentUser(userId).collectLatest { response ->
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
                                    if (data.isComplete()) ProfileCompleteness.COMPLETE
                                    else ProfileCompleteness.INCOMPLETE

                            )

                        }
                        Log.d("UserProfileViewModel", "get user profile| User: $response")
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
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = mapOf("ApiResponse" to "UNKNOW")
                            )
                        }

                    }
                }
            }
        }
    }

    fun updateUserProfile() {
        Log.d("UserProfileViewModel", "ApiResponse Run: ${_uiState.value}")

        val phone = _uiState.value.editProfile.phone
        //validate phone input 10 - 11
        if (phone.length < 10 || phone.length > 11) {
            _uiState.update {
                it.copy(
                    errorMessage = mapOf(
                        "phone" to "Số điện thoại phải lớn hơn 10 và nhỏ hơn 11"
                    )
                )
            }
            Log.d("UserProfileViewModel", "ApiResponse Error valid: ${_uiState.value}")
            return
        }
        val userProfile = _uiState.value.toUser() ?: return
        Log.d("UserProfileViewModel", "ApiResponse checked success: ${_uiState.value}")

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            if (!userProfile.isComplete()) {
                _uiState.update {
                    it.copy(
                        profileCompleteness = ProfileCompleteness.INCOMPLETE,
                        errorMessage = mapOf("profile" to "Vui lòng điền đầy đủ thông tin profile"),
                        isLoading = false,
                    )
                }
                return@launch
            }
            val response = userRepository.updateUser(userProfile)

            when (response) {
                is ApiResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEditMode = false,
                            user = userProfile,
                            isClickedUpdate = false,
                            isEnable = false,
                            editProfile = userProfile.toEditProfile(),
                            profileCompleteness =
                                if (userProfile.isComplete()) ProfileCompleteness.COMPLETE
                                else ProfileCompleteness.INCOMPLETE,
                            successMessage = "Profile updated successfully",

                            )
                    }
                }

                is ApiResponse.Error -> {
                    Log.d("UserProfileViewModel", "ApiResponse Error: ${_uiState.value}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = mapOf("ApiResponse" to response.message)
                        )
                    }
                }

                is ApiResponse.Loading -> {
                }

                is ApiResponse.Empty -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = mapOf("ApiResponse" to "Empty")
                        )
                    }

                }
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

            //gán gias trị new input cho biến state edit
            val editedProfile = when (field) {
                "name" -> current.editProfile.copy(name = value)
                "address" -> {
                    current.editProfile.copy(address = value)
                }

                "phone" -> {
                    val isValid = phoneRegex.matches(value) //dùng regex đã khởi tạo
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

    //toggle click update
    fun onCheckedChange() {
        _uiState.update {
            it.copy(isClickedUpdate = !it.isClickedUpdate)
        }
    }

    //enable can edit
    fun setEnable(value: Boolean = false) {
        _uiState.update { it.copy(isEnable = value) }
    }


}
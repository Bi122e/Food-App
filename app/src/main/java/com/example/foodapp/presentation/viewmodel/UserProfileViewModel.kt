package com.example.foodapp.presentation.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.animation.expandIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.domain.model.User
import com.example.foodapp.presentation.state.EditProfileState
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.presentation.state.toEditProfile
import com.example.foodapp.presentation.state.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {


    private val _eventSuccess = MutableSharedFlow<String>()
    val eventSuccess = _eventSuccess.asSharedFlow()
    private val _uiStateProfile = MutableStateFlow(ProfileUiState())
    val uiStateProfile = _uiStateProfile.asStateFlow()
    private val phoneRegex = Regex("^[0-9]*$")

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            _uiStateProfile.update { it.copy(isLoading = true) }
            userRepository.getCurrentUser(userId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        Log.d("UserProfileViewModel", "api success")
                        val data = response.data
                        _uiStateProfile.update {
                            it.copy(
                                isLoading = false,
                                user = data,
                                editProfile = data.toEditProfile(),
                                profileCompleteness =
                                    if (data.isComplete()) ProfileCompleteness.COMPLETE
                                    else ProfileCompleteness.INCOMPLETE

                            )

                        }
                        setOriginalProfile(_uiStateProfile.value.user) //can click update btn (profile tab)
                        Log.d("UserProfileViewModel", "get user profile| User: $response")
                    }


                    is ApiResponse.Error -> {
                        _uiStateProfile.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = mapOf("loadCurrentUser" to response.message)
                            )
                        }
                    }

                    else -> {
                        _uiStateProfile.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = mapOf("ApiResponse" to "UNKNOW")
                            )
                        }

                    }
                }
            }
            resetLoading()
        }
    }

    fun validateProfile(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        val user = _uiStateProfile.value.editProfile


        if (user.email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
            errors["email"] = "Email không hợp lệ"
        }

        if (user.phone.length != 10) {
            errors["phone"] = "Số điện thoại không hợp lệ"
        }



        if (user.name.isEmpty()) {
            Log.d("TestFlowName", "ket qua empty: ${_uiStateProfile.value.errorMessage}")
            errors["name"] = "Tên không được để trống"

        } else if (user.name.length !in 5..20) {
            errors["name"] = "Tên chỉ hợp lệ 5 đến 11 ký tự"
        } else if (!user.name.matches(Regex("^[a-zA-ZÀ-ỹ\\s]+$"))) {
            Log.d("TestFlowName", "ket qua ky tu:  ")
            errors["name"] = "Tên không được chứa ký tự đặc biệt"
        }

//        if (!Patterns.EMAIL_ADDRESS.matcher(user.email.trim())
//                .matches() && user.email.substringAfterLast(
//                ".",
//                ""
//            ).length > 2
//        ) {
//            errors["email"] = "Email không hợp lệ"
//        }


        _uiStateProfile.update {
            it.copy(
                errorMessage =  errors
            )
        }



        Log.d("Check_error_type", "$errors")
        Log.d("test_flow_updated_", "ket qua empty: ${_uiStateProfile.value.errorMessage}")

        return errors
    }

    fun validatePhone(): Boolean {

        val phone = _uiStateProfile.value.editProfile.phone

        return if (phone.length != 10) {
            _uiStateProfile.update {
                it.copy(
                    errorMessage = mapOf(
                        "phone" to "Số điện thoại không hợp lệ"
                    )
                )
            }
            false
        } else {
            true
        }
    }


    fun validateName(): Boolean {
        val name = _uiStateProfile.value.editProfile.name

        val error = when {
            name.isEmpty() -> {
                Log.d("TestFlowName", "ket qua empty: ${_uiStateProfile.value.errorMessage}")
                "Tên không được để trống"

            }

            !name.matches(Regex("^[a-zA-ZÀ-ỹ\\s]+$")) -> {
                Log.d("TestFlowName", "ket qua ky tu:  ")

                "Tên không được chứa ký tự đặc biệt"
            }

            name.length !in 5..20 -> {
                Log.d("TestFlowName", "ket qua 5 .. 20:  ")

                "Tên chỉ hợp lệ 5 đến 11 ký tự"
            }

            else -> {
                Log.d("TestFlowName", "else ")
                null
            }
        }

        return if (error != null) {
            Log.d("TestFlowName", "!= null: ${_uiStateProfile.value.errorMessage}  ")
            _uiStateProfile.update { it.copy(errorMessage = it.errorMessage + mapOf("name" to error)) }
            false
        } else {
            Log.d("TestFlowName", "ket qua: true")
            true
        }
    }


    fun updateUserProfile() {

        val userProfile = _uiStateProfile.value.toUser() ?: return
        Log.d("updateUserProfile", "ApiResponse checked success: ${_uiStateProfile.value}")
        Log.d("test_flow_updated_", "start updated: ${_uiStateProfile.value.editProfile}")

        viewModelScope.launch {
            _uiStateProfile.update {
                it.copy(isLoading = true)
            }
            val response = userRepository.updateUser(userProfile)

            when (response) {
                is ApiResponse.Success -> {
                    Log.d("updateUser_check", "sucess VM ${_uiStateProfile.value.editProfile}")
                    _uiStateProfile.update {
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

                    _eventSuccess.emit("Cập nhật thành công")

                    Log.d(
                        "check_can_click_btn",
                        "Profile updated successfully - update current edit user"
                    )
                    Log.d(
                        "test_flow_updated_",
                        "sucess updated: ${_uiStateProfile.value.editProfile}"
                    )

                    setOriginalProfile(_uiStateProfile.value.user)
                }

                is ApiResponse.Error -> {
                    Log.d(
                        "test_flow_updated_",
                        "error updated: ${_uiStateProfile.value.editProfile}"
                    )

                    Log.d("updateUser_check", "error VM ${_uiStateProfile.value.editProfile}")
                    _uiStateProfile.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = mapOf("ApiResponse" to response.message)
                        )
                    }
                }

                is ApiResponse.Loading -> {
                    Log.d("updateUser_check", "loading VM ${_uiStateProfile.value.editProfile}")
                }

                is ApiResponse.Empty -> {
                    Log.d("updateUser_check", "empty VM ${_uiStateProfile.value.editProfile}")
                    _uiStateProfile.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = mapOf("ApiResponse" to "Empty")
                        )
                    }

                }

                is ApiResponse.Conflict -> {}
            }
        }
    }


    fun onFieldEditProfileChange(field: String, value: String) {

        _uiStateProfile.update { current ->

            //gán gias trị new input cho biến state edit
            val editedProfile = when (field) {
                "name" -> {
                    Log.d("test_flow_updated_", "on field name: ${field} $value")
                    current.editProfile.copy(name = value)
                }

                "address" -> {
                    Log.d("test_flow_updated_", "on field address: ${field} $value")
                    current.editProfile.copy(address = value)
                }

                "phone" -> {
                    val isValid = phoneRegex.matches(value) //dùng regex đã khởi tạo
                    Log.d("test_flow_updated_", "on field phone: ${field} $value")
                    if (isValid) current.editProfile.copy(phone = value)
                    else current.editProfile
                }

                "gender" -> {
                    Log.d("test_flow_updated_", "on field gender: ${field} $value")
                    current.editProfile.copy(gender = value)
                }

                "email" -> {
                    Log.d("test_flow_updated_", "on field email: ${field} $value")

                    current.editProfile.copy(email = value)
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
        _uiStateProfile.update {
            it.copy(isClickedUpdate = !it.isClickedUpdate)
        }
    }

    fun setOriginalProfile(user: User?) {
        user ?: return
        val currentProfile = EditProfileState(
            name = user.name,
            phone = user.phone,
            address = user.address,
            email = user.email,
            gender = user.gender
        )
        Log.d("check_can_click_btn", "set origint: user $user")
        Log.d("check_can_click_btn", "set origint: user $currentProfile")
        _uiStateProfile.update { it.copy(originalProfile = currentProfile) }
    }

    //enable can edit
    fun setEnable(value: Boolean = false) {
        _uiStateProfile.update { it.copy(isEnable = value) }
    }

    fun nextStep() {
        _uiStateProfile.update { it.copy(currentStep = it.currentStep + 1) }
    }

    fun previousStep() {
        if (_uiStateProfile.value.currentStep > 0) {
            _uiStateProfile.update { it.copy(currentStep = it.currentStep - 1) }
        }
    }

    fun resetClickedUpdate() {
        _uiStateProfile.update { it.copy(isClickedUpdate = false) }
    }

    fun setClickedUpdate() {
        _uiStateProfile.update { it.copy(isClickedUpdate = true) }
    }

    fun setGender(gender: String) {
        _uiStateProfile.update { profile ->
            profile.copy(
                editProfile = profile
                    .editProfile.copy(gender = gender)
            )
        }

    }

    fun resetLoading() {
        _uiStateProfile.update { profile ->
            profile.copy(isLoading = false)
        }
    }


    fun onExpandedChange() {
        _uiStateProfile.update {
            it.copy(expanded = !it.expanded)
        }
    }

    fun setExpandedChange() {
        _uiStateProfile.update {
            it.copy(expanded = false)
        }
    }
}
package com.example.foodapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.FavoriteRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.presentation.state.EditProfileState
import com.example.foodapp.presentation.state.ProfileStatistics
import com.example.foodapp.presentation.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Profile @Inject constructor(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val favoriteRepository: FavoriteRepository,

): ViewModel() {

//    AuthViewModel (hoặc AccountViewModel)
//    ├─ changePassword
//    └─ logout
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    private var observeFavoritesJob: Job? = null

    private var currentUserId: String? = null

    fun init(userId: String) {
        if (currentUserId == userId) return
        currentUserId = userId

        observeFavorites(userId)
        loadProfile(userId)
    }

    private fun observeFavorites(userId: String) {
        observeFavoritesJob?.cancel()

        viewModelScope.launch {
            favoriteRepository.observeFavorites(userId)
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    favoriteFoods = response.data,
                                    statistics = it.statistics?.copy(
                                        favoriteFoodsCount = response.data.size
                                    )
                                )
                            }
                        }
                        is ApiResponse.Error -> showError(response.message)
                        else -> Unit
                    }
                }
        }
    }


    fun loadProfile(userId: String) {
        viewModelScope.launch {
            setLoading(true)

            loadUser(userId)
            loadStatistics(userId)

            setLoading(false)
        }
    }

    private suspend fun loadUser(userId: String) {
            when (val response = userRepository.getUserById(userId)) {
                is ApiResponse.Success -> {
                    //nen dung update thay cho .value moi truong hop, de tranh coroutine ghi de
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = response.data,
                            editProfile = EditProfileState(
                                response.data.name,
                                phone = response.data.phone,
                                address = response.data.address,
                            ),
                        )
                    }
                }

                is ApiResponse.Error -> showError(response.message)
                else -> Unit
            }
    }

//    private fun loadFavoriteFoods(userId: String) {
//        viewModelScope.launch {
//            favoriteRepository.observeFavorites(userId).take(1).collect { response ->
//                when (response) {
//                    is ApiResponse.Success -> {
//                        _uiState.update {
//                            it.copy(favoriteFoods = response.data)
//                        }
//                    }
//
//                    is ApiResponse.Error -> showError(response.message)
//                    else -> {}
//
//                }
//            }
//        }
//    }

    private fun loadStatistics(userId: String) {
        viewModelScope.launch {
            val orderRes = orderRepository.getTotalOrdersCount(userId = userId)
            val spentRes = orderRepository.getTotalSpent(userId)
            if (orderRes is ApiResponse.Success &&
                spentRes is ApiResponse.Success
            ) {
                _uiState.update {
                    it.copy(
                        statistics = ProfileStatistics(
                            totalOrders = orderRes.data,
                            totalSpent = spentRes.data.toDouble(),
                            favoriteFoodsCount = it.favoriteFoods.size
                        )
                    )
                }
            } else {
                val error = when {
                    orderRes is ApiResponse.Error -> orderRes.message
                    spentRes is ApiResponse.Error -> spentRes.message
                    else -> "Unknow"
                }

                showError(error)
            }
        }
    }

    //edit mode
    fun toggleEditMode() {
        _uiState.update {
            val enable = !it.isEditMode //bat
            it.copy(
                isLoading = true,
                isEditMode = enable,
                editProfile = if (!enable && it.user != null) {
                    EditProfileState(
                        name = it.user.name,
                        phone = it.user.phone,
                        address = it.user.address
                    )
                } else {
                    it.editProfile
                }
            )
        }
    }

    fun updateName(name: String) {
        _uiState.update {
            it.copy(editProfile = it.editProfile.copy(name = name))
        }
    }

    fun updatePhone(phone: String) {
        _uiState.update {
            it.copy(editProfile = it.editProfile.copy(phone = phone))
        }
    }

    fun updateAddress(address: String) {
        _uiState.update {
            it.copy(
                editProfile = it.editProfile.copy(address = address)
            )
        }
    }

    fun saveProfile() {
        val state = uiState.value
        val user = state.user ?: return
        val edit = state.editProfile

        val updatedUser = user.copy(
            name = edit.name,
            phone = edit.phone,
            address = edit.address
        )

        if (!updatedUser.isProfileComplete() || !updatedUser.isPhoneValid()) {
            showError("Thong tin khong hop le")
            return
        }

        viewModelScope.launch {
            when (val response = userRepository.updateUser(updatedUser)) {
                is ApiResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = updatedUser,
                            isEditMode = false,
                            successMessage = "Cap nhat thanh cong"
                            )
                    }
                }
                is ApiResponse.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = response.message) }
                }
                else -> Unit
            }
        }
    }

    fun changePassword(
        oldPassword: String,
        newPassword: String
    ) {
        if (newPassword.length < 6) {
            showError("Mat khau phai >= 6 ky ty")
            return
        }
        viewModelScope.launch {
            setLoading(true)

            when (val response = userRepository.changePassword(oldPassword, newPassword))
            {
                is ApiResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Doi mat khau thanh cong"
                        )
                    }
                }
                is ApiResponse.Error -> showError(response.message)
                else -> Unit
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            setLoading(true)
            userRepository.logout()
            setLoading(false)
        }
    }

    private fun setLoading(loading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = loading,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = message
            )
        }

    }

    fun clearMessage() {
        _uiState.update {
            it.copy(errorMessage = null,
                successMessage = null)
        }
    }
}


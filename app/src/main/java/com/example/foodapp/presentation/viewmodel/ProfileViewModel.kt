package com.example.foodapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.FavoriteRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.ProfileRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.CustomerProfile
import com.example.foodapp.presentation.state.EditProfileState
import com.example.foodapp.presentation.state.ProfileStatistics
import com.example.foodapp.presentation.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val favoriteRepository: FavoriteRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    private var observeFavoritesJob: Job? = null

    private var currentUserId: String? = null
    // Temporary storage for profile
    private var currentProfile: CustomerProfile? = null

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

            loadUserAndProfile(userId)
            loadStatistics(userId)

            setLoading(false)
        }
    }

    private suspend fun loadUserAndProfile(userId: String) {
            // First load User to get auth info (e.g. role, though we assume Customer here for now)
            when (val userResponse = userRepository.getUserById(userId)) {
                is ApiResponse.Success -> {
                     // Load Profile
                     when (val profileResponse = profileRepository.getCustomerProfile(userId)) {
                         is ApiResponse.Success -> {
                             currentProfile = profileResponse.data
                             _uiState.update {
                                 it.copy(
                                     isLoading = false,
                                     user = userResponse.data,
                                     editProfile = EditProfileState(
                                         name = profileResponse.data.name,
                                         phone = profileResponse.data.phone,
                                         address = profileResponse.data.address,
                                     ),
                                 )
                             }
                         }
                         is ApiResponse.Error -> {
                             // Profile might not exist yet? Create default or show error?
                             // Assuming error for now, or handle empty profile
                             showError(profileResponse.message)
                         }
                         else -> Unit
                     }
                }

                is ApiResponse.Error -> showError(userResponse.message)
                else -> Unit
            }
    }

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
                    else -> "Unknown"
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
                editProfile = if (!enable && currentProfile != null) {
                    EditProfileState(
                        name = currentProfile!!.name,
                        phone = currentProfile!!.phone,
                        address = currentProfile!!.address
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
        val currentP = currentProfile ?: CustomerProfile(uid = user.uid)

        // Validate
        if (edit.name.isBlank() || edit.address.isBlank()) {
             showError("Thong tin khong hop le")
             return
        }
        // Simplified validation for now

        val updatedProfile = currentP.copy(
            name = edit.name,
            phone = edit.phone,
            address = edit.address
        )

        viewModelScope.launch {
            setLoading(true)
            when (val response = profileRepository.updateCustomerProfile(updatedProfile)) {
                is ApiResponse.Success -> {
                    currentProfile = updatedProfile
                    _uiState.update {
                        it.copy(
                            isLoading = false,
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
             setLoading(false)
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


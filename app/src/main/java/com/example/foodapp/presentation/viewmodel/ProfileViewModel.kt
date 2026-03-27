//package com.example.foodapp.presentation.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.foodapp.core.ApiResponse
//import com.example.foodapp.data.repository.FavoriteRepository
//import com.example.foodapp.data.repository.OrderRepository
//import com.example.foodapp.data.repository.ProfileRepository
//import com.example.foodapp.data.repository.UserRepository
//import com.example.foodapp.domain.model.CustomerProfile
//import com.example.foodapp.presentation.state.EditProfileState
//import com.example.foodapp.presentation.state.ProfileStatistics
//import com.example.foodapp.presentation.state.ProfileUiState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.async
//import kotlinx.coroutines.awaitAll
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//@HiltViewModel
//class ProfileViewModel @Inject constructor(
//    private val userRepository: UserRepository,
//    private val orderRepository: OrderRepository,
//    private val favoriteRepository: FavoriteRepository,
//    private val profileRepository: ProfileRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(ProfileUiState())
//    val uiState = _uiState.asStateFlow()
//
//    private var observeFavoritesJob: Job? = null
//    private var loadProfileJob: Job? = null
//
//    private var currentUserId: String? = null
//    private var currentProfile: CustomerProfile? = null
//
//    // ================= INIT =================
//
//    fun init(userId: String) {
//        if (currentUserId == userId) return
//        currentUserId = userId
//
//        observeFavorites(userId)
//        loadProfile(userId)
//    }
//
//    // ================= LOAD PROFILE =================
//
//    fun loadProfile(userId: String) {
//        loadProfileJob?.cancel()
//
//        loadProfileJob = viewModelScope.launch {
//            setLoading(true)
//            try {
//                coroutineScope {
//                    awaitAll(
//                        async { loadUserAndProfile(userId) },
//                        async { loadStatistics(userId) }
//                    )
//                }
//            } finally {
//                setLoading(false)
//            }
//        }
//    }
//
//    private suspend fun loadUserAndProfile(userId: String) {
//
//        val userRes = userRepository.getUserById(userId)
//        if (userRes !is ApiResponse.Success) {
//            if (userRes is ApiResponse.Error) showError(userRes.message)
//            return
//        }
//
//        val profileRes = profileRepository.getCustomerProfile(userId)
//        if (profileRes !is ApiResponse.Success) {
//            if (profileRes is ApiResponse.Error) showError(profileRes.message)
//            return
//        }
//
//        val profile = profileRes.data
//        currentProfile = profile
//
//        _uiState.update {
//            it.copy(
//                user = userRes.data,
//                editProfile = EditProfileState(
//                    name = profile.name,
//                    phone = profile.phone,
//                    address = profile.address
//                )
//            )
//        }
//    }
//
//    private suspend fun loadStatistics(userId: String) {
//
//        val orderRes = orderRepository.getTotalOrdersCount(userId)
//        val spentRes = orderRepository.getTotalSpent(userId)
//
//        if (orderRes is ApiResponse.Success &&
//            spentRes is ApiResponse.Success
//        ) {
//            _uiState.update {
//                it.copy(
//                    statistics = ProfileStatistics(
//                        totalOrders = orderRes.data,
//                        totalSpent = spentRes.data.toDouble(),
//                        favoriteFoodsCount = it.favoriteFoods.size
//                    )
//                )
//            }
//        }
//    }
//
//    // ================= OBSERVE FAVORITES =================
//
//    private fun observeFavorites(userId: String) {
//        observeFavoritesJob?.cancel()
//
//        observeFavoritesJob = viewModelScope.launch {
//            favoriteRepository.observeFavorites(userId)
//                .collect { response ->
//                    when (response) {
//                        is ApiResponse.Success -> {
//                            _uiState.update {
//                                it.copy(
//                                    favoriteFoods = response.data,
//                                    statistics = it.statistics?.copy(
//                                        favoriteFoodsCount = response.data.size
//                                    )
//                                )
//                            }
//                        }
//
//                        is ApiResponse.Error -> showError(response.message)
//                        else -> Unit
//                    }
//                }
//        }
//    }
//
//    // ================= EDIT MODE =================
//
//    fun toggleEditMode() {
//        _uiState.update { state ->
//            val enable = !state.isEditMode
//
//            state.copy(
//                isEditMode = enable,
//                editProfile = if (!enable && currentProfile != null) {
//                    EditProfileState(
//                        name = currentProfile!!.name,
//                        phone = currentProfile!!.phone,
//                        address = currentProfile!!.address
//                    )
//                } else state.editProfile
//            )
//        }
//    }
//
//    fun updateName(name: String) {
//        _uiState.update {
//            it.copy(editProfile = it.editProfile.copy(name = name))
//        }
//    }
//
//    fun updatePhone(phone: String) {
//        _uiState.update {
//            it.copy(editProfile = it.editProfile.copy(phone = phone))
//        }
//    }
//
//    fun updateAddress(address: String) {
//        _uiState.update {
//            it.copy(editProfile = it.editProfile.copy(address = address))
//        }
//    }
//
//    // ================= SAVE PROFILE =================
//
//    fun saveProfile() {
//
//        val state = uiState.value
//        val user = state.user ?: return
//        val edit = state.editProfile
//
//        if (edit.name.isBlank() || edit.address.isBlank()) {
//            showError("Thong tin khong hop le")
//            return
//        }
//
//        val baseProfile = currentProfile ?: CustomerProfile(uid = user.uid)
//
//        val updatedProfile = baseProfile.copy(
//            name = edit.name,
//            phone = edit.phone,
//            address = edit.address
//        )
//
//        viewModelScope.launch {
//            setLoading(true)
//            try {
//                when (val response =
//                    profileRepository.updateCustomerProfile(updatedProfile)
//                ) {
//                    is ApiResponse.Success -> {
//                        currentProfile = updatedProfile
//                        _uiState.update {
//                            it.copy(
//                                isEditMode = false,
//                                successMessage = "Cap nhat thanh cong"
//                            )
//                        }
//                    }
//
//                    is ApiResponse.Error -> showError(response.message)
//                    else -> Unit
//                }
//            } finally {
//                setLoading(false)
//            }
//        }
//    }
//
//    // ================= CHANGE PASSWORD =================
//
//    fun changePassword(oldPassword: String, newPassword: String) {
//
//        if (newPassword.length < 6) {
//            showError("Mat khau phai >= 6 ky tu")
//            return
//        }
//
//        viewModelScope.launch {
//            setLoading(true)
//            try {
//                when (val response =
//                    userRepository.changePassword(oldPassword, newPassword)
//                ) {
//                    is ApiResponse.Success -> {
//                        _uiState.update {
//                            it.copy(successMessage = "Doi mat khau thanh cong")
//                        }
//                    }
//
//                    is ApiResponse.Error -> showError(response.message)
//                    else -> Unit
//                }
//            } finally {
//                setLoading(false)
//            }
//        }
//    }
//
//    // ================= LOGOUT =================
//
//    fun logout() {
//        viewModelScope.launch {
//            userRepository.logout()
//        }
//    }
//
//    // ================= STATE HELPERS =================
//
//    private fun setLoading(loading: Boolean) {
//        _uiState.update {
//            it.copy(
//                isLoading = loading,
//                errorMessage = null,
//                successMessage = null
//            )
//        }
//    }
//
//    private fun showError(message: String) {
//        _uiState.update {
//            it.copy(
//                isLoading = false,
//                errorMessage = message
//            )
//        }
//    }
//
//    fun clearMessage() {
//        _uiState.update {
//            it.copy(
//                errorMessage = null,
//                successMessage = null
//            )
//        }
//    }
//}
//
//

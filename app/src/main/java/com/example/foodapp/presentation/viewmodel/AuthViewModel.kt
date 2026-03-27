package com.example.foodapp.presentation.viewmodel

 import AuthStatus
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.AuthResult
import com.example.foodapp.core.UiState
import com.example.foodapp.core.flatMap
import com.example.foodapp.core.map
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.ProfileRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.CustomerProfile
import com.example.foodapp.presentation.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<AuthResult>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()
    private val _authStatus = MutableStateFlow<AuthStatus>(AuthStatus.Loading)
    val authStatus = _authStatus.asStateFlow()
    private val _appState = MutableStateFlow<AppState>(AppState.Loading)
    val appState = _appState.asStateFlow()


    init {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Init: Waiting for Compose to render")
            delay(700)
            checkInitialAuth()
        }
    }

    //    private fun checkInitialAuth() {
//        val currentId = authRepository.currentUserId()
//        if (currentId == null) {
//            _authStatus.value = AuthStatus.Unauthenticated
//            return
//        }
//        viewModelScope.launch {
//            val response = userRepository.getUserById(currentId)
//            if (response is ApiResponse.Success) {
//                _authStatus.value = AuthStatus.Authenticated
//            } else {
//                _authStatus.value = AuthStatus.Unauthenticated
//            }
//        }
//    }
//    private fun checkInitialAuth() {
//        val currentId = authRepository.currentUserId() // Lệnh này chạy cục bộ, cực nhanh (10ms)
//
//        if (currentId == null) {
//            _authStatus.value = AuthStatus.Unauthenticated
//        } else {
//            // Vào Home luôn! Đừng launch coroutine đợi lấy Profile ở đây.
//            _authStatus.value = AuthStatus.Authenticated
//        }
//    }

    //tu goi ham nay, neu dang o loading thi tu nhay sang guest, neu da dang nhap thi lay uid cua user va xu ly
    private fun checkInitialAuth() {
//        val currentId = authRepository.currentUserId()
//        _authStatus.value =
//            if (currentId == null)
//                AuthStatus.Unauthenticated
//            else
//                AuthStatus.Authenticated
        Log.d("AuthViewModel", "checkInitialAuth: Starting check")
        val uid = authRepository.currentUserId()
        if (uid == null) {
            Log.d("AuthViewModel", "checkInitialAuth: No UID found, setting Guest")
            _appState.value = AppState.Guest
            _authStatus.value = AuthStatus.Unauthenticated
        } else {
            Log.d("AuthViewModel", "checkInitialAuth: UID found: $uid, fetching user profile")
            viewModelScope.launch {
                val response = userRepository.getUserById(uid)
                if (response is com.example.foodapp.core.ApiResponse.Success) {
                    val user = response.data
                    Log.d("AuthViewModel", "checkInitialAuth: Success fetching user. Role: ${user.role}")
                    _appState.value = AppState.LoggedIn(user)
                    _authStatus.value = AuthStatus.Authenticated
                } else {
                    Log.e("AuthViewModel", "checkInitialAuth: Failed fetching user: ${if (response is com.example.foodapp.core.ApiResponse.Error) response.message else "Unknown"}")
                    _appState.value = AppState.Guest
                    _authStatus.value = AuthStatus.Unauthenticated
                }
            }
        }
    }

//    private fun checkAuthStatus() {
//        viewModelScope.launch {
//            val currentId = authRepository.currentUserId()
//
//            if (currentId == null) {
//                // Không có user - đổi state ngay lập tức
////                _uiState.value = UiState.Success(AuthResult.NotLoggedIn)
//                _authStatus.value = AuthStatus.Unauthenticated
//                return@launch
//            }
//
//            // Có user - fetch data
//            try {
//                val response = userRepository.getUserById(currentId)
//                _authStatus.value = AuthStatus.Authenticated
////                _uiState.value = response.toUiState { AuthResult.LoggedIn(it) }
//            } catch (e: Exception) {
//                _uiState.value = UiState.Error(e.message ?: "Unknown error")
//            }
//        }
//    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
//            val response = authRepository.login(email, password)
//            val state = response.toUiState()
//            _uiState.value = state
//            Log.d("AuthViewModel", "Login = $state")

            val finalResponse = authRepository.login(email, password)
                .flatMap { uid ->
                    userRepository.updateLastLoginAndToken(uid).map { uid }
                }
                .flatMap { uid ->
                    userRepository.getUserById(uid)
                }

            val state = finalResponse.toUiState() { user ->
                AuthResult.LoggedIn(user)
            }
            _uiState.value = state

            if (finalResponse is com.example.foodapp.core.ApiResponse.Success) {
                val user = finalResponse.data
                Log.d("AuthViewModel", "Login Success. Navigating...")
                _appState.value = AppState.LoggedIn(user)
                _authStatus.value = AuthStatus.Authenticated
            }

            Log.d("AuthViewModel", "Current UI State: ${_uiState.value}")
        }

    }

    fun logout() {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Logging out...")
            authRepository.logOut()
            _appState.value = AppState.Guest
            _authStatus.value = AuthStatus.Unauthenticated
            _uiState.value = UiState.Idle
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val finalResponse = authRepository.register(email, password)
                .flatMap { uid ->
                    userRepository.createUser(uid, email)
                        .flatMap {
                            // Create default CustomerProfile
                            profileRepository.createCustomerProfile(
                                CustomerProfile(uid = uid, name = "", phone = "", address = "", profileUrl = "")
                            ).map { uid }
                        }
                        .flatMap { uid ->
                            userRepository.getUserById(uid)
                        }
                }
            val state = finalResponse.toUiState() { data ->
                AuthResult.LoggedIn(data)
            }
            _uiState.value = state

            if (finalResponse is com.example.foodapp.core.ApiResponse.Success) {
                val user = finalResponse.data
                Log.d("AuthViewModel", "Register Success. Navigating...")
                _appState.value = AppState.LoggedIn(user)
                _authStatus.value = AuthStatus.Authenticated
            }
            Log.d("AuthViewModel", "Current UI State: ${_uiState.value}")
        }
    }
//    fun register(name: String, email: String, password: String) {
//        viewModelScope.launch {
//            _uiState.emit(UiState.Loading)
//            _uiState.emit(
//                authRepository.register(name, email, password).toUiState()
//            )
//        }
//    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val response = authRepository.changePassword(oldPassword, newPassword)
            _uiState.value = response.toUiState() {
                AuthResult.PasswordChange
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val finalResponse = authRepository.loginWithGoogle(idToken)
                .flatMap { uid ->
                    userRepository.updateLastLoginAndToken(uid).map { uid }
                }
                .flatMap { uid ->
                    userRepository.getUserById(uid)
                }
            val state = finalResponse.toUiState() { data ->
                AuthResult.LoggedIn(data)
            }
            _uiState.value = state

            if (finalResponse is com.example.foodapp.core.ApiResponse.Success) {
                val user = finalResponse.data
                Log.d("AuthViewModel", "Google Login Success. Navigating...")
                _appState.value = AppState.LoggedIn(user)
                _authStatus.value = AuthStatus.Authenticated
            }
            Log.d("AuthViewModel", "Current UI State: ${_uiState.value}")
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }

    fun isUserLoggedIn(): Boolean {
        return authRepository.currentUserId() != null
    }

}
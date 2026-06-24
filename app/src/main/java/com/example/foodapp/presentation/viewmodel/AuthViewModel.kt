package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.AuthResult
import com.example.foodapp.core.UiState
import com.example.foodapp.core.flatMap
import com.example.foodapp.core.map
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.User
import com.example.foodapp.presentation.state.AppState
import com.example.foodapp.presentation.state.AuthUiState
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
) : ViewModel() {
    //    private val _uiState = MutableStateFlow<UiState<AuthResult>>(UiState.Idle)
//    val uiState = _uiState.asStateFlow()
//    private val _authStatus = MutableStateFlow<AuthStatus>(AuthStatus.Loading)
//    val authStatus = _authStatus.asStateFlow()
    private val _appState = MutableStateFlow<AppState>(AppState.Loading)
    val appState = _appState.asStateFlow()


    //dùng để hiện state btn ở riêng màn regis,
    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState())
    val authUiState = _authUiState.asStateFlow()


    init {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Init: Waiting for Compose to render")
            delay(700)
            checkInitialAuth()
        }
    }


    /*
    tu goi ham nay  Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .coloredShadow(
                        color = Color.Red,
                        alpha = 0.4f,
                        borderRadius = 20.dp,
                        blurRadius = 20.dp
                    )
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(20.dp),
                        ROun
                    )
            ) { }
        }, neu dang o loading thi tu nhay sang guest,
     neu da dang nhap thi lay uid cua user va xu ly
     */
    private fun checkInitialAuth() {
        _appState.value = AppState.Loading //fix loading cho state

        Log.d("AuthViewModel", "checkInitialAuth: Starting check")
        val uid = authRepository.currentUserId()
        if (uid == null) {
            Log.d("AuthViewModel", "checkInitialAuth: No UID found, setting Guest")
            _appState.value = AppState.Guest
        } else {
            Log.d("AuthViewModel", "checkInitialAuth: UID found: $uid, fetching user profile")
            viewModelScope.launch {
                val response = userRepository.getUserById(uid)
                if (response is ApiResponse.Success) {
                    val user = response.data

                    //kt co complte chua
                    if (user.isComplete()) {
                        Log.d("AuthViewModel", "checkInitialAuth: Success fetching user. Role: ")
                        _appState.value = AppState.LoggedIn(user)
                    } else {
                        _appState.value = AppState.NeedCompleteProfile(user)
                    }

                } else {
                    Log.e(
                        "AuthViewModel",
                        "checkInitialAuth: Failed fetching user: ${if (response is com.example.foodapp.core.ApiResponse.Error) response.message else "Unknown"}"
                    )
                    _appState.value = AppState.Guest
                }
            }
        }
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            /* flatmap
            thay vì viết, 2 when xử lý lồng nhau cho result api
            when (api result) {
            is ApiSuccess -> {
                gọi tiếp api result khác, tốn 2 bước

            thay vào đó api result.flat { result -> api khác (result)
             */
            _authUiState.value = AuthUiState(isLoadingLogin = true)

            val finalResponse = authRepository.login(email, password)
                .flatMap { uid ->
                    userRepository.updateLastLoginAndToken(uid).map { uid }
                } // thành công mới chạy xuống flat 2, ko thì flat 2 bắt lỗi result 1, nối lỗi 2 bắt 2
                .flatMap { uid ->
                    userRepository.getUserById(uid)
                }

//            val state = finalResponse.toUiState() { user ->
//                AuthResult.LoggedIn(user)
//            }

            if (finalResponse is ApiResponse.Success) {
                val user = finalResponse.data
                resetAuthState()
                Log.d("LoginScreenLogic", "VM: Login Success. Navigating...")
                if (user.isComplete()) {
                    _appState.value = AppState.LoggedIn(user)
                } else {
                    _appState.value = AppState.NeedCompleteProfile(user)
                }
            }
            if (finalResponse is ApiResponse.Error) {
                _authUiState.value = AuthUiState(errorLogin = finalResponse.message)
                Log.d("LoginScreenLogic", "VM: ERROR: email = $email, pass = $password - ${finalResponse.message} Login failed")
             }
            _authUiState.value = AuthUiState(isLoadingLogin = false)
        }

    }

    fun logout() {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Logging out...")
            authRepository.logOut()
            _appState.value = AppState.Guest
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState(isLoadingRegister = true)
            val finalResponse = authRepository.register(email, password)
                .flatMap { uid ->
                    userRepository.createUser(uid, email)
                        .flatMap { uid ->
                            userRepository.getUserById(uid)
                        }
                }
            finalResponse.toUiState() { data ->
                AuthResult.LoggedIn(data)
            }

            when (finalResponse) {
                is ApiResponse.Success -> {
                    val user = finalResponse.data
                    resetAuthState()
                    Log.d("AuthViewModel", "email: ${email}, pass: ${password}, register sucess ${finalResponse.data}")
                    if (user.isComplete()) {
                        _appState.value = AppState.LoggedIn(user)
                    } else {
                        _appState.value = AppState.NeedCompleteProfile(user)
                    }
                }

                is ApiResponse.Error -> {
                    _authUiState.value = AuthUiState(errorRegister = finalResponse.message)
                    _appState.value = AppState.Error(finalResponse.message)
                }
                else -> {}
            }
            _authUiState.value = AuthUiState(isLoadingRegister = true)
        }
    }


    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            authRepository.changePassword(oldPassword, newPassword)

        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            val finalResponse = authRepository.loginWithGoogle(idToken)
                .flatMap { uid ->
                    userRepository.updateLastLoginAndToken(uid).map { uid }
                }
                .flatMap { uid ->
                    userRepository.getUserById(uid)
                }
            finalResponse.toUiState() { data ->
                AuthResult.LoggedIn(data)
            }

            if (finalResponse is ApiResponse.Success) {
                val user = finalResponse.data
                Log.d("AuthViewModel", "Google Login Success. Navigating...")
                if (user.isComplete()) {
                    _appState.value = AppState.LoggedIn(user)
                } else {
                    _appState.value = AppState.NeedCompleteProfile(user)
                }
            }
        }
    }


    fun setLoggedIn(updatedUser: User) {
        _appState.value = AppState.LoggedIn(user = updatedUser)
    }

    fun resetState() {
        _appState.value = AppState.Guest
    }


    fun resetAuthState() {
        _authUiState.value = AuthUiState(
            errorLogin = null,
            errorRegister = null
        )
    }

}
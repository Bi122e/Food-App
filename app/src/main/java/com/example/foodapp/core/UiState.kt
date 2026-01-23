package com.example.foodapp.core

sealed class UiState<out T> {

    data object Idle: UiState<Nothing>()
    data object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(
        val message: String,
        val type: UiErrorType = UiErrorType.GENERAL
    ): UiState<Nothing>()
    data class Empty(val message: String = "Khong co du lieu"): UiState<Nothing>()


    fun isIdle(): Boolean = this is Idle
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isEmpty(): Boolean = this is Empty
    fun isLoading(): Boolean = this is Loading

    fun getDataOrNull(): T? = (this as? Success)?.data
    fun getErrorOrNull(): String? = (this as? Error)?.message

}
enum class UiErrorType {

    GENERAL,
    SYSTEM,
    NETWORK,
    VALIDATION,
    AUTHENTICATION,
    NOT_FOUND,
    SEVER,
    PERMISSION,
}

//fun <T> ApiResponse<T>.toUiState(): UiState<T> {
//    return when (this) {
//        is ApiResponse.Success -> UiState.Success(data)
//        is ApiResponse.Error -> UiState.Error(
//            message = message,
//            type = code.toUiErrorType()
//        )
//        is ApiResponse.Loading -> UiState.Loading
//        is ApiResponse.Empty -> UiState.Empty()
//    }
//}

fun <T> ApiResponse<T>.toUiState(): UiState<T> {
    return when(this) {
        is ApiResponse.Success -> UiState.Success(data)
        is ApiResponse.Error -> UiState.Error(
            message,
            code.toUiErrorType()
        )
        is ApiResponse.Empty -> UiState.Empty()
        is ApiResponse.Loading -> UiState.Loading
    }
}

private fun ErrorCode.toUiErrorType(): UiErrorType {
    return when(this) {
        ErrorCode.NETWORK_ERROR -> UiErrorType.NETWORK
        ErrorCode.UNAUTHORIZED -> UiErrorType.AUTHENTICATION
        ErrorCode.PERMISSION_DENIED -> UiErrorType.PERMISSION
        ErrorCode.NOT_FOUND -> UiErrorType.NOT_FOUND
        ErrorCode.SEVER_ERROR -> UiErrorType.SEVER
        ErrorCode.VALIDATE_ERROR -> UiErrorType.VALIDATION
        else -> UiErrorType.GENERAL

    }
}


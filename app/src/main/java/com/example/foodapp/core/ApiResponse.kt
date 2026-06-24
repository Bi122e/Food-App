package com.example.foodapp.core

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T): ApiResponse<T>()
    data object Empty: ApiResponse<Nothing>()
    data object Loading: ApiResponse<Nothing>()
    data class Error(
        val message: String,
        val code: ErrorCode = ErrorCode.UNKNOW,
        val throwable: Throwable? = null
    ): ApiResponse<Nothing>()
    data class Conflict(
         val oldRestaurantName: String,
        val newRestaurantName: String,
    ): ApiResponse<Nothing>()


    fun isSuccess(): Boolean = this is Success
    fun isEmpty(): Boolean = this is Empty
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading


    fun getDataOrNull(): T? = (this as? Success)?.data
    fun getErrorOrNull(): String? = (this as? Error)?.message
}
enum class ErrorCode {
    UNKNOW,
    SYSTEM,
    SEVER_ERROR,
    UNAUTHORIZED,
    VALIDATE_ERROR,
    NETWORK_ERROR,
    TIMEOUT,
    PERMISSION_DENIED,
    NOT_FOUND

}

inline fun <T, R> ApiResponse<T>.map(transform: (T) -> R): ApiResponse<R> {
    return when (this) {
        is ApiResponse.Success -> ApiResponse.Success(transform(data))
        is ApiResponse.Loading -> ApiResponse.Loading
        is ApiResponse.Empty -> ApiResponse.Empty
        is ApiResponse.Error -> ApiResponse.Error(message, code, throwable)
        is ApiResponse.Conflict -> ApiResponse.Conflict(  oldRestaurantName, newRestaurantName)
    }
}

inline fun <T> ApiResponse<T>.onError(action: (String) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Error) action(message)
    return this
}
/*
thay vì viết, 2 when xử lý lồng nhau cho result api
when (api result) {
is ApiSuccess -> {
    gọi tiếp api result khác, tốn 2 bước

thay vào đó api result.flat { result -> api khác (result)
 */
inline fun <T, R> ApiResponse<T>.flatMap(transform: (T) -> ApiResponse<R>): ApiResponse<R> {
    return when (this) {
        is ApiResponse.Success -> transform(data)
        is ApiResponse.Error -> ApiResponse.Error(message, code, throwable)
        is ApiResponse.Loading -> ApiResponse.Loading
        is ApiResponse.Empty -> ApiResponse.Empty
        is ApiResponse.Conflict -> ApiResponse.Conflict(  oldRestaurantName, newRestaurantName)
    }
}


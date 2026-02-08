sealed interface AuthStatus {
    object Loading : AuthStatus
    object Authenticated : AuthStatus
    object Unauthenticated : AuthStatus
}

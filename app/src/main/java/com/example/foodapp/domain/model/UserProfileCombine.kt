package com.example.foodapp.domain.model

data class UserProfileCombine(
    val uid: String,
    val email: String,
    val role: UserRole,
    val isActive: Boolean,
    val name: String,
    val phone: String,
    val address: String,
    val profileUrl: String,
) {
    fun isProfileComplete(): Boolean {
        return (name.isNotBlank() && phone.isNotBlank() && address.isNotBlank())
    }

}

enum class ProfileCompleteness {
    COMPLETE,
    INCOMPLETE
}

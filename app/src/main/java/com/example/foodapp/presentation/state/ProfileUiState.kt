package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Favorite
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.domain.model.User

data class ProfileUiState(
    val isLoading: Boolean = false,
    val currentStep: Int = 0,
    val user: User? = null,
    val favoriteFoods: List<Favorite> = emptyList(),
    val statistics: ProfileStatistics? = null,
    val isClickedUpdate: Boolean = false,
    val expanded: Boolean = false,
    val isEnable: Boolean = false,
    val isEditMode: Boolean = false,
    val editProfile: EditProfileState = EditProfileState(),
    val originalProfile: EditProfileState = EditProfileState(),
    val errorMessage: Map<String, String?> = emptyMap(),
    val successMessage: String? = null,
    val profileCompleteness: ProfileCompleteness = ProfileCompleteness.INCOMPLETE
) {
    val isSavedEnable: Boolean //can click btn update profile tab
        get() = originalProfile != editProfile

}

data class EditProfileState(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val email: String = "",
    val gender: String = "",
)

data class ProfileStatistics(
    val totalOrders: Int = 0,
    val totalSpent: Double = 0.0,
    val favoriteFoodsCount: Int = 0
)

fun User.toEditProfile(): EditProfileState {
    return EditProfileState(
        name = this.name,
        phone = this.phone,
        address = this.address,
        email = this.email,
        gender = this.gender
    )
}

fun ProfileUiState.toUser(): User? {
    val user = this.user ?: return null
    return user.copy(
        email = editProfile.email,
        address = editProfile.address,
        name = editProfile.name,
        phone = editProfile.phone,
        gender = editProfile.gender,
    )
}



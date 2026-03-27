package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Favorite
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.domain.model.UserProfileCombine

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserProfileCombine? = null,
    val favoriteFoods: List<Favorite> = emptyList(),
    val statistics: ProfileStatistics? = null,
    val isClickedUpdate: Boolean = false,
    val isEnable: Boolean = false,
    val isEditMode: Boolean = false,
    val editProfile: EditProfileState = EditProfileState(),
    val errorMessage: Map<String, String?> = emptyMap(),
    val successMessage: String? = null,
    val profileCompleteness: ProfileCompleteness = ProfileCompleteness.INCOMPLETE
)
data class EditProfileState(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val email: String = ""
)

data class ProfileStatistics(
    val totalOrders: Int = 0,
    val totalSpent: Double = 0.0,
    val favoriteFoodsCount: Int =0
)

fun UserProfileCombine.toEditProfile() = EditProfileState(
    name = name,
    phone = phone,
    address = address,
    email = email
)
fun ProfileUiState.toUserProfileCombine(): UserProfileCombine? {
    val user = this.user ?: return null
    return user.copy(
        email = editProfile.email,
        phone = editProfile.phone,
        address = editProfile.address,
        name = editProfile.name
    )
}



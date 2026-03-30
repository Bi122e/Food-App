package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.CustomerProfile
import com.example.foodapp.domain.model.Favorite
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.domain.model.User
import com.example.foodapp.domain.model.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
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

fun User.toEditProfile(): EditProfileState {
    val profile = this.profile?.customer
    return EditProfileState(
        name = profile?.name ?: "",
        phone = profile?.phone ?: "",
        address = profile?.address ?: "",
        email = email
    )
}

fun ProfileUiState.toUser(): User? {
    val user = this.user ?: return null
    val currentProfile = user.profile?.customer
    return user.copy(
        email = editProfile.email,
        profile = user.profile?.copy(
            customer = currentProfile?.copy(
                name = editProfile.name,
                phone = editProfile.phone,
                address = editProfile.address
            ) ?: CustomerProfile(
                uid = user.uid,
                name = editProfile.name,
                phone = editProfile.phone,
                address = editProfile.address
            )
        ) ?: UserProfile(
            customer = CustomerProfile(
                uid = user.uid,
                name = editProfile.name,
                phone = editProfile.phone,
                address = editProfile.address
            )
        )
    )
}



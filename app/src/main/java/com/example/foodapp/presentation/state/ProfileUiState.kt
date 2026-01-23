package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Favorite
 import com.example.foodapp.domain.model.User

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val favoriteFoods: List<Favorite> = emptyList(),
    val statistics: ProfileStatistics? = null,
    val isEditMode: Boolean = false,
    val editProfile: EditProfileState = EditProfileState(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
)
data class EditProfileState(
    val name: String = "",
    val phone: String = "",
    val address: String = ""
)

data class ProfileStatistics(
    val totalOrders: Int = 0,
    val totalSpent: Double = 0.0,
    val favoriteFoodsCount: Int =0
)
{

}
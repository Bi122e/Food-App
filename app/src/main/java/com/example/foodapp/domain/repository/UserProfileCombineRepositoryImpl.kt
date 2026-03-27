package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.ProfileRepository
import com.example.foodapp.data.repository.UserProfileCombineRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.CustomerProfile
import com.example.foodapp.domain.model.UserProfileCombine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserProfileCombineRepositoryImpl @Inject constructor(
     private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository
) : UserProfileCombineRepository {


    override fun getUserProfile(uid: String): Flow<ApiResponse<UserProfileCombine>> = flow {

        val userResponse = userRepository.getCurrentUser(uid).first()
        val profileResponse = profileRepository.getCustomerProfile(uid)

        if (userResponse is ApiResponse.Success && profileResponse is ApiResponse.Success) {
            val combined = UserProfileCombine(
                uid = userResponse.data.uid,
                email = userResponse.data.email,
                role = userResponse.data.role,
                isActive = userResponse.data.isActive,
                name = profileResponse.data.name,
                phone = profileResponse.data.phone,
                address = profileResponse.data.address,
                profileUrl = profileResponse.data.profileUrl
            )
            emit(ApiResponse.Success(combined))
        } else {
            emit(ApiResponse.Error("Failed to load user profile"))
        }
    }

    override suspend fun updateCustomerProfile(profile: UserProfileCombine): ApiResponse<Unit> {
        return try {

            val customerProfile = CustomerProfile(
                uid = profile.uid,
                name = profile.name,
                phone = profile.phone,
                address = profile.address,
                profileUrl = profile.profileUrl
            )
            profileRepository.updateCustomerProfile(customerProfile)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update user profile")
        }
    }
}

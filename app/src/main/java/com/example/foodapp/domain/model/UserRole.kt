package com.example.foodapp.domain.model

enum class UserRole(
    val displayName: String,
    val priority: Int
) {
    DRIVER("Tai xe", 4),
    CUSTOMER("Khach hang", 1),
    RESTAURANT("Chu nha hang", 2),
    ADMIN("Quan tri vien", 3);


    fun canAccessAdmin(): Boolean = this == ADMIN

    fun canManageRestaurant(): Boolean = this in listOf(RESTAURANT, ADMIN)

    fun canViewAllOthers(): Boolean = this == ADMIN

    companion object {

        fun fromString(value: String): UserRole {
            return entries.find {
                it.name.equals(value, ignoreCase = true)
            } ?: CUSTOMER
        }

        fun getAll(): List<UserRole> = entries
            }
}
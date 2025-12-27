package com.example.foodapp.domain

enum class UserRole(
    val displayName: String,
    val priority: Int
) {

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
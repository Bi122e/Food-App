package com.example.foodapp.presentation.extentions

import com.example.foodapp.domain.model.User

fun User.getDisplayName(): String {
    return when  {
        name.isNotEmpty() -> name
        email.isNotEmpty() -> email.substringBefore("@")
        phone.isNotEmpty() -> phone
        else -> "User"
    }
}

//get initials avatar (2 first word)

fun User.getInitials(): String {
    if (name.isNotBlank()) {
        val parts = name.trim().split(Regex("\\s+"))

        return if (parts.size >= 2) {
            "${parts.first().first()}${parts.last().first()}".uppercase()
        } else {
            name.take(2).uppercase()
        }
    }
    return "U"
}

//get role display text

fun User.getRoleDisplayName(): String = role.displayName

//format phone number 0934  -> 09 09
fun User.getFormattedPhone(): String {
    if (phone.length != 10) return phone
    return "${phone.substring(0, 4)} ${phone.substring(4, 7)} ${phone.substring(7)}"
}

//get short address (about 50 tex)
fun User.getShortAddress(): String {
    return if (address.length > 50) {
        "${address.take(47)}..."
    } else {
        address
    }
}

//text profile completion
fun User.getProfileCompletionText(): String {
    return if (isProfileComplete()) {
        "Hoàn thành"
    } else {
        "Chưa hoàn thành hồ sơ ${getProfilePercentage()}%"
    }
}

//text missing fields

fun User.getMissingFieldsText(): String {
    val fields = getMissingFields()
     if (fields.isEmpty()) return ""
    val fieldNames = fields.map { field ->
        when (field) {
            "name" -> "Họ và tên"
            "phone" -> "Số điện thoại"
            "address" -> "Địa chỉ"
            else -> field
        }
    }
    return "Con thieu ${fieldNames.joinToString(", ")}"
}

//check for user have avatar
fun User.hasAvatar(): Boolean = profileUrl.isNotEmpty()

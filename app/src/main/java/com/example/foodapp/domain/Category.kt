package com.example.foodapp.domain

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Category(
    val name: String = "",
    val id: String = "",
    val iconUrl: String = "",
    val slug: String = "",
    val order: Int = 0, //display order
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    fun isValid(): Boolean {
        return name.isNotEmpty() &&
                id.isNotEmpty() &&
                slug.isNotEmpty()
    }

    fun generateSlug(): String {
        if (slug.isNotEmpty()) return slug

        return name
            .lowercase()
            .trim()
            .replace(Regex("[àáạảãâầấậẩẫăằắặẳẵ]"), "a")
            .replace(Regex("[èéẹẻẽêềếệểễ]"), "e")
            .replace(Regex("[ìíịỉĩ]"), "i")
            .replace(Regex("[òóọỏõôồốộổỗơờớợởỡ]"), "o")
            .replace(Regex("[ùúụủũưừứựửữ]"), "u")
            .replace(Regex("[ỳýỵỷỹ]"), "y")
            .replace(Regex("[đ]"), "d")
            .replace(Regex("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]"), "A")
            .replace(Regex("[ÈÉẸẺẼÊỀẾỆỂỄ]"), "E")
            .replace(Regex("[ÌÍỊỈĨ]"), "I")
            .replace(Regex("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]"), "O")
            .replace(Regex("[ÙÚỤỦŨƯỪỨỰỬỮ]"), "U")
            .replace(Regex("[ỲÝỴỶỸ]"), "Y")
            .replace(Regex("[Đ]"), "D")
            .replace(Regex("[^a-z0-9]+"), "-")
            .replace(Regex("^-+|-+$"), "")
    }

    companion object {

        fun getDefaultCategories(): List<Category> {
            return listOf(
                Category("1", "Tất cả", "", "all", 0),
                Category("2", "Món chính", "", "mon-chinh", 1),
                Category("3", "Đồ uống", "", "do-uong", 2),
                Category("4", "Tráng miệng", "", "trang-mieng", 3),
                Category("5", "Món phụ", "", "mon-phu", 4),
            )
        }
    }
}

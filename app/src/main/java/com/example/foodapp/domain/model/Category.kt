package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Category(
    val name: String = "",
    val categoryId: String = "",
    val iconUrl: String = "",
    val slug: String = "",
    val order: Int = 0, //display order
    val type: CategoryType = CategoryType.NORMAL,
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    fun isValid(): Boolean {
        return name.isNotEmpty() &&
                categoryId.isNotEmpty() &&
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
                Category("Tất cả", "all", "", "all", 0),
                Category("Món chính", "main", "", "mon-chinh", 1),
                Category("Đồ Uống", "drink", "", "do-uong", 2),
                Category("Tráng miệng", "desert", "", "trang-mieng", 3),
                Category("Món Phụ", "side_disk", "", "mon-phu", 4),
            )
        }
    }
}
enum class CategoryType {
    ALL,
    NORMAL,
}

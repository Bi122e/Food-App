package com.example.foodapp.presentation.extentions

import androidx.compose.ui.graphics.Color

fun String.getChipStyle(): ChipStyle {
    return when (lowercase().firstOrNull()) {

        'a' -> ChipStyle(
            background = Color(0xFFFFF3E0),
            font = Color(0xFFE65100)
        )

        'b' -> ChipStyle(
            background = Color(0xFFE8F5E9),
            font = Color(0xFF2E7D32)
        )

        'c' -> ChipStyle(
            background = Color(0xFFE3F2FD),
            font = Color(0xFF1565C0)
        )

        'd' -> ChipStyle(
            background = Color(0xFFF3E5F5),
            font = Color(0xFF7B1FA2)
        )

        'e' -> ChipStyle(
            background = Color(0xFFFFEBEE),
            font = Color(0xFFC62828)
        )

        'f' -> ChipStyle(
            background = Color(0xFFE0F7FA),
            font = Color(0xFF00838F)
        )

        'g' -> ChipStyle(
            background = Color(0xFFFFF8E1),
            font = Color(0xFFF9A825)
        )

        'h' -> ChipStyle(
            background = Color(0xFFEDE7F6),
            font = Color(0xFF512DA8)
        )

        'i' -> ChipStyle(
            background = Color(0xFFF1F8E9),
            font = Color(0xFF558B2F)
        )

        'j' -> ChipStyle(
            background = Color(0xFFFFFDE7),
            font = Color(0xFFF57F17)
        )

        'k' -> ChipStyle(
            background = Color(0xFFE8EAF6),
            font = Color(0xFF3949AB)
        )

        'l' -> ChipStyle(
            background = Color(0xFFFCE4EC),
            font = Color(0xFFAD1457)
        )

        'm' -> ChipStyle(
            background = Color(0xFFE0F2F1),
            font = Color(0xFF00695C)
        )

        'n' -> ChipStyle(
            background = Color(0xFFFFF3E0),
            font = Color(0xFFEF6C00)
        )

        'o' -> ChipStyle(
            background = Color(0xFFE1F5FE),
            font = Color(0xFF0277BD)
        )

        'p' -> ChipStyle(
            background = Color(0xFFF9FBE7),
            font = Color(0xFF827717)
        )

        'q' -> ChipStyle(
            background = Color(0xFFFBE9E7),
            font = Color(0xFFD84315)
        )

        'r' -> ChipStyle(
            background = Color(0xFFE8F5E9),
            font = Color(0xFF1B5E20)
        )

        's' -> ChipStyle(
            background = Color(0xFFF3E5F5),
            font = Color(0xFF6A1B9A)
        )

        't' -> ChipStyle(
            background = Color(0xFFE0F7FA),
            font = Color(0xFF006064)
        )

        'u' -> ChipStyle(
            background = Color(0xFFFFF8E1),
            font = Color(0xFFFF8F00)
        )

        'v' -> ChipStyle(
            background = Color(0xFFEDE7F6),
            font = Color(0xFF4527A0)
        )

        'w' -> ChipStyle(
            background = Color(0xFFFCE4EC),
            font = Color(0xFF880E4F)
        )

        'x' -> ChipStyle(
            background = Color(0xFFE0F2F1),
            font = Color(0xFF004D40)
        )

        else -> ChipStyle(
            background = Color(0xFFF5F5F5),
            font = Color(0xFF616161)
        )
    }

}

data class ChipStyle(
    val background: Color,
    val font: Color,
)
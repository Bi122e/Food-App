package com.example.foodapp.presentation.extentions

fun String?.toConvertTag(): String {

    return when {
        this == "Bún" -> "bun"
        this == "Gà rán" -> "ga-ran"
        this == "Trà sữa" -> "tra-sua"
        this == "Bún đậu" -> "bun-dau"
        this == "bún" -> "bun"
        this == "Cơm chiên" -> "com-chien"
        this == "Chè" -> "che"
        this == "cơm" -> "com"
        else -> { "tat-ca" }
    }
}

fun String?.toConvertEscapeTag(): String {

    return when {
        this == "Bún" -> "\"Bún\""
        this == "Gà rán" -> "\"Gà rán\""
        this == "Trà sữa" -> "\"Trà sữa\""
        this == "Bún đậu" -> "\"Bún đậu\""
        this == "bún" -> "\"bún\""
        this == "Cơm chiên" -> "\"Cơm chiên\""
        this == "Chè" -> "\"Chè\""
        this == "Tất cả"  -> { "\"Tất cả\"" }
        else -> "Bạn đang tìm kiếm món gì?"
    }
}




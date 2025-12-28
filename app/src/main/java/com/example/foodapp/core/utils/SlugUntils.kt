package com.example.foodapp.core.utils

import java.text.Normalizer
import java.util.Locale

fun generateSlug(text: String): String {
    val normalized = Normalizer.normalize(text.lowercase(Locale.getDefault()), Normalizer.Form.NFD)
    return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .replace("[^a-z0-9\\s-]".toRegex(), "")
        .replace("\\s+".toRegex(), "-")
        .replace("-+".toRegex(), "-")
        .trim('-')
}
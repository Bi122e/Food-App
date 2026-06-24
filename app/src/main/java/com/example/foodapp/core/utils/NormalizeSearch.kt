package com.example.foodapp.core.utils

import java.text.Normalizer

fun String.toNormalizeSearch(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(),"")
        .lowercase()
}


//tieng viet + hoa -> thuong ko dau
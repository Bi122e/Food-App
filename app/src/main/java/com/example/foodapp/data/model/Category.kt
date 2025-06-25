package com.example.foodapp.data.model

import com.example.foodapp.utils.generateSlug

data class Category(
    val id: String,
    val name: String,
    val iconUrl: String,
    val slug: String = generateSlug(name)
)

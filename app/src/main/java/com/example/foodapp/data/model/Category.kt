package com.example.foodapp.data.model

import com.example.foodapp.utils.generateSlug

data class Category(
    var id: String? = null,
    var name: String? = null,
    var iconUrl: String? = null,
    var slug: String? = null
)
{
    constructor() : this(null, null, null, null)
    init {
        slug = generateSlug(name ?:  "")
    }

}

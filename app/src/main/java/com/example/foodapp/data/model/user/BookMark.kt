//file model bookmark
package com.example.foodapp.data.model.user

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class BookMark(
    val itemId: String = "",
    @ServerTimestamp
    val updateAt: Date? = null
) {
    constructor(): this("", null)
}

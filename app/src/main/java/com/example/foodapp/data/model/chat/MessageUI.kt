//file model messageui
package com.example.foodapp.data.model.chat

data class MessageUI(
    val message: String = "",
    val isMine: Boolean = false,
    val time: String = "",
)

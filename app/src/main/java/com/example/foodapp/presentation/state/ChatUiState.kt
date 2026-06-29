    package com.example.foodapp.presentation.state

    import com.example.foodapp.domain.model.Conversation
    import com.example.foodapp.domain.model.Message

    data class ChatUiState(

        val conversations: List<Conversation> = emptyList(),
        val messages: List<MessageUi> = emptyList(),
        val loadingMessages: Boolean = false,
        val loadingCreate: Boolean = false,
        val errorCreate: Boolean = false,
        val loadingConversations: Boolean = false,
        val errorMessage: Boolean = false,
        val errorConversation: Boolean = false,
        val text: String = "",
        val isDelivered: Boolean = false,
    )


    fun setConversation(
        customerId: String,
        driverId: String,
        displayName: String,
    ): Conversation {
        return Conversation(
            customerId = customerId,
            driverId = driverId,
            displayName = displayName,

            )
    }

    //custome lại để tìm ra ismine, ko ghi đè vào model

    data class MessageUi(
        val message: Message = Message(),
        val isMine: Boolean
    )


    fun List<Message>.toMessageUi(currentId: String): List<MessageUi> {
        return this.map { message ->
            MessageUi(
                message = message,
                isMine = message.senderId == currentId
            )
        }
    }
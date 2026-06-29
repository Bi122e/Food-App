package com.example.foodapp.domain.repository

import android.util.Log
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.core.utils.factorMessage
import com.example.foodapp.data.repository.ChatRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.domain.model.Conversation
import com.example.foodapp.domain.model.Message
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val orderRepository: OrderRepository
) : ChatRepository {

    private val conversationCollection = firestore.collection(Constance.COLLECTION_CONVERSATIONS)
    private val messageCollection = firestore.collection(Constance.COLLECTION_MESSAGES)


    override suspend fun createConversation(conversation: Conversation): ApiResponse<String> {
        return try {

            val conversationRef = conversationCollection.document()
            val conversationId = conversationRef.id
            val conversionData = conversation.copy(
                conversationId = conversationId,
                createdAt = null,
                lastMessage = "Xin chào quý khác, đơn hàng của bạn sẽ được giao trong thời gian sớm nhất.",
                unreadCount = 1,
                lastMessageSenderId = conversation.driverId,

                )
            val messageRef = conversationRef.collection(Constance.COLLECTION_MESSAGES).document()
            val message = factorMessage(
                senderId = conversation.driverId,
                conversationId = conversationId
            ).copy(
                messageId = messageRef.id
            )

            firestore.batch().apply {
                set(messageRef, message)
                set(conversationRef, conversionData)
            }.commit().await()

            Log.d("checkFB_createConversation", "SUCCESS $conversationId")

            ApiResponse.Success(conversationId)

        } catch (e: Exception) {
            Log.d("checkFB_createConversation", "error ${e.message}")
            ApiResponse.Error(e.message ?: "Failed to create conversation")
        }
    }

    override suspend fun createMessage(
        message: Message,
        conversation: Conversation
    ): ApiResponse<Unit> {
        return try {
            val messageRef = conversationCollection
                .document(message.conversationId)
                .collection(Constance.COLLECTION_MESSAGES)
                .document()
            val messageId = messageRef.id
            val conversationRef = conversationCollection.document(message.conversationId)
            val changedField = mapOf(
                "lastMessage" to message.text,
                "lastMessageTime" to FieldValue.serverTimestamp(),
                "unreadCount" to conversation.unreadCount + 1,
            )
            firestore.batch().apply {
                set(messageRef, message.copy(messageId = messageId))
                update(
                    conversationRef,
                    changedField
                )

            }.commit().await()
            Log.d("checkDB_createMessage", "success")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("checkDB_createMessage", e.message ?: "error")
            ApiResponse.Error("error: ${e.message}")
        }
    }

    override suspend fun updateConversation(conversationId: String): ApiResponse<Unit> {

        return try {
            conversationCollection
                .document(conversationId)
                .update("active", false)
                .await()
            Log.d("checkFB_updateConversation", "SUCCESS")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("checkFB_updateConversation", "ERROR: ${e.message}")
            ApiResponse.Error(e.message ?: "error")
        }
    }

    override fun observeMessages(conversationId: String): Flow<ApiResponse<List<Message>>> =
        callbackFlow {
            Log.d("CheckFB_observeMessages", "conversationId: $conversationId")
            val listener = conversationCollection
                .document(conversationId)
                .collection(Constance.COLLECTION_MESSAGES)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Log.d("CheckFB_observeMessages", "ERROR: $exception")
                        trySend(ApiResponse.Error("${exception.message}"))
                        return@addSnapshotListener
                    }
                    val messages = snapshot?.toObjects(Message::class.java) ?: emptyList()
                    Log.d("CheckFB_observeMessages", "Success: ${messages.size}")

                    trySend(ApiResponse.Success(messages))

                }
            awaitClose { listener.remove() }
        }

    override suspend fun resetUnread(conversationId: String): ApiResponse<Unit> {
        return try {
            val doc = conversationCollection
                .document(conversationId)
            doc.update(
                "unreadCount", 0
            )
                .await()
            Log.d("checkFB_resetUnread", "check doc: ${doc.id}")
            Log.d("checkFB_resetUnread", "success")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("checkFB_resetUnread", e.message ?: "error")
            ApiResponse.Error(e.message ?: "error")
        }
    }


    override fun getConversationsByUserId(userId: String): Flow<ApiResponse<List<Conversation>>> =
        callbackFlow {
            Log.d("checkFB_getConversationsByUserId", "uid: $userId")
            val listener =
                conversationCollection
                    .whereEqualTo("customerId", userId)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, error ->
                        Log.e("CHAT_OBSERVE", "Snapshot fired ${System.currentTimeMillis()}")
                        Log.d(
                            "checkFB_getConversationsByUserId",
                            "snapshot: ${snapshot}, size: ${snapshot?.size()}"
                        )
                        if (error != null) {
                            trySend(
                                ApiResponse.Error(
                                    error.message ?: "Failed to get conversations"
                                )
                            )
                            Log.d("checkFB_getConversationsByUserId", "error: ${error.message}")
                            return@addSnapshotListener
                        }
                        val conversations = snapshot?.documents?.mapNotNull {
                            it.toObject(Conversation::class.java)
                        } ?: emptyList()

                        if (conversations.isNotEmpty()) {
                            trySend(ApiResponse.Success(conversations))
                            Log.d("checkFB_getConversationsByUserId", "success: $conversations")
                        } else {
                            Log.d("checkFB_getConversationsByUserId", "empty")
                            trySend(ApiResponse.Empty)
                        }
                    }
            awaitClose { listener.remove() }
        }


}

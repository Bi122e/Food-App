package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.ChatRepository
import com.example.foodapp.domain.model.Conversation
import com.example.foodapp.domain.model.Message
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore
): ChatRepository {

    private val conversationCollection = firestore.collection(Constance.COLLECTION_CONVERSATIONS)
    private val messageCollection = firestore.collection(Constance.COLLECTION_MESSAGES)


    override suspend fun createConversation(conversation: Conversation): ApiResponse<String> {
        return try {
            val setId = conversation.participants.toString()
            val conversationRef = conversationCollection.document(setId)
            val conversion = conversation.copy(
                conversationId = setId, createdAt = Date(), updatedAt = Date()
            )
            conversationRef.set(conversationRef).await()
            ApiResponse.Success(setId)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to create conversation")
        }
    }

    override suspend fun getConversationById(conversationId: String): ApiResponse<Conversation> {
        return try {
            val conversationRef = conversationCollection.document(conversationId).get().await()
            val conversation = conversationRef?.toObject(Conversation::class.java)
            if (conversation != null && conversation.isValid()) {
                ApiResponse.Success(conversation)
            } else {
                ApiResponse.Error("Conversation not found")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get conversation")
        }
    }

    override fun getConversationsByUserId(userId: String): Flow<ApiResponse<List<Conversation>>> =
        callbackFlow {
            val listener = conversationCollection.whereArrayContains("participants", userId)
                .orderBy("updatedAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ApiResponse.Error(error.message ?: "Failed to get conversations"))
                        return@addSnapshotListener
                    }
//                    if (snapshot != null) {
//                        val conversation = snapshot.documents.mapNotNull {
//                            it.toObject(Conversation::class.java)
//                        }
//                        ApiResponse.Success(conversation)
//                    } else {
//                        ApiResponse.Empty
//                    }
                    val conversation = snapshot?.documents?.mapNotNull {
                        it.toObject(Conversation::class.java)
                    } ?: emptyList()
                    trySend(ApiResponse.Success(conversation))
                }
            awaitClose { listener.remove() }
        }

    //hoac return Unit
    override suspend fun updateConversation(conversation: Conversation): ApiResponse<Boolean> {
        return try {
            val updatedConversation = conversation.copy(updatedAt = Date())
            conversationCollection.document(conversation.conversationId).set(updatedConversation)
                .await()
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update conversation")
        }
    }

    override suspend fun deleteConversation(
        conversationId: String, currentUserId: String
    ): ApiResponse<Boolean> {
        return try {

//            val messageSnapshot = messageCollection
//                .whereEqualTo("conversationId", conversationId)
//                .get()
//                .await()
//            val batch = firestore.batch()
//            messageSnapshot.documents.forEach { doc ->
//                batch.delete(doc.reference)
//            }
//            batch.delete(conversationCollection.document(conversationId))
//            batch.commit().await()
//            val conversationRef = conversationCollection.document(conversationId)
//

            val conversationRef = conversationCollection.document(conversationId)

            val conversation = conversationRef.get().await().toObject(Conversation::class.java)
                ?: return ApiResponse.Error("Conversation not found")

            if (!conversation.participants.contains(currentUserId)) {
                return ApiResponse.Error("Permission denied")
            }

            val messageRef = conversationRef.collection(Constance.COLLECTION_MESSAGES)

            val snapshot = messageRef.get().await()
            snapshot.documents.chunked(450).forEach { snapshots ->
                val batch = firestore.batch()
                snapshots.forEach { batch.delete(it.reference) }
                batch.commit().await()
            }
            conversationRef.delete().await()
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to delete conversation")
        }
    }

    override suspend fun deleteMessage(messageId: String): ApiResponse<Boolean> {
        return try {
            messageCollection.document(messageId).delete().await()
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Error("Failed to delete message")
        }
    }

    override suspend fun getUnreadMessageCount(
        conversationId: String, userId: String
    ): ApiResponse<Int> {
        return try {
            val count = messageCollection
                .whereEqualTo("conversationId", conversationId)
                .whereEqualTo("isRead", false)
                .whereNotEqualTo("senderId", userId)
                .count()
                .get(AggregateSource.SERVER).await()
            ApiResponse.Success(count.count.toInt())
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get unread message")
        }
    }

    override suspend fun markMessageAsRead(messageId: String): ApiResponse<Boolean> {
        return try {
            messageCollection
                .document(messageId)
                .update(
                    mapOf(
                        "isRead" to true, "updated" to Date()
                    )
                ).await()
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to mark message as read")
        }
    }

    override suspend fun markAllMessageAsRead(
        conversationId: String, userId: String
    ): ApiResponse<Boolean> {
        return try {
            val snapshot = messageCollection
                .whereEqualTo("conversationId", conversationId)
                .whereEqualTo("isRead", false)
                .whereNotEqualTo("senderId", userId)
                .get()
                .await()
            if (snapshot.isEmpty) {
                return ApiResponse.Success(true)
            }
            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.update(
                    doc.reference, mapOf(
                        "isRead" to true, "updatedAt" to Date()
                    )
                )
            }
            batch.commit().await()
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to mark all messages as read")
        }
    }

    override suspend fun searchMessage(
        conversationId: String, query: String
    ): ApiResponse<List<Message>> {
        return try {
            val snapshot = conversationCollection
                .whereEqualTo("conversationId", conversationId)
                .orderBy("text")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .await()
            val message = snapshot.documents.mapNotNull {
                it.toObject(Message::class.java)
            }
            ApiResponse.Success(message)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to search message")
        }
    }

    override suspend fun sendMessage(message: Message): ApiResponse<String> {
        return try {
            val messageRef = messageCollection.document()
            val messageId = messageRef.id
            val messageWithId = message.copy(
                messageId = messageId, createdAt = Date(), updatedAt = Date()
            )
            val batch = firestore.batch()
            batch.set(messageRef, messageWithId)

            val conversationRef = conversationCollection.document(message.conversationId)
            batch.update(
                conversationRef, mapOf(
                    "lastMessage" to messageWithId.getConversationPreview(),
                    "lastMessageAt" to Date(),
                    "updated" to Date()
                )
            )
            batch.commit().await()
            ApiResponse.Success(messageId)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to send message")
        }
    }

    override fun getLastMessage(conversationId: String): Flow<ApiResponse<Message?>> =
        callbackFlow {
            val listener = messageCollection.whereEqualTo("conversationId", conversationId)
                .orderBy("createdAt", Query.Direction.DESCENDING).limit(1)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ApiResponse.Error(error.message ?: "Failed to get latest message"))
                        return@addSnapshotListener
                    }
                    val message = snapshot?.documents?.firstOrNull()?.toObject(Message::class.java)
                    trySend(ApiResponse.Success(message))
                }
            awaitClose { listener.remove() }
        }

    override fun getMessagesByConversationId(conversationId: String): Flow<ApiResponse<List<Message>>> =
        callbackFlow {
            val listener = messageCollection.whereEqualTo("conversationId", conversationId)
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(
                            ApiResponse.Error(
                                error.message ?: "Failed to get message conversation"
                            )
                        )
                    }

                    val conversation = snapshot?.documents?.mapNotNull {
                        it.toObject(Message::class.java)
                    } ?: emptyList()
                    trySend(ApiResponse.Success(conversation))
                }
            awaitClose { listener.remove() }
        }
}

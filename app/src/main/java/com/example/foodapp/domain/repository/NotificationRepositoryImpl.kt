package com.example.foodapp.domain.repository

import android.util.Log
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.NotificationRepository
import com.example.foodapp.domain.model.AppNotification
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : NotificationRepository {

    val notificationRef = firestore.collection(Constance.COLLECTION_NOTIFICATION)
    override fun getNotifications(userId: String): Flow<ApiResponse<List<AppNotification>>> =
        callbackFlow {
            val listener = notificationRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("active", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshots, exception ->
                    snapshots?.documents?.forEach {
                        Log.d("RAW_DOC_CHECK", it.data.toString())
                    }
                    if (exception != null) {
                        Log.d("get_getNotifications", exception.message ?: "error")
                        trySend(ApiResponse.Error(exception.message ?: "error"))
                        return@addSnapshotListener
                    }
                    val notifications = snapshots?.documents?.mapNotNull {
                        it.toObject(AppNotification::class.java)
                    } ?: emptyList()
                    snapshots?.documents?.forEach { doc ->
                        val obj = doc.toObject(AppNotification::class.java)

                        Log.d(
                            "MAP_CHECK_",
                            """
        raw=${doc.data}
        mapped=$obj
        """.trimIndent()
                        )
                    }
                    Log.d("get_getNotifications", "success: $notifications")
                    trySend(ApiResponse.Success(notifications))
                }
            awaitClose { listener.remove() }
        }

    override suspend fun createNotification(notification: AppNotification): ApiResponse<Unit> {
        return try {
            val docRef = notificationRef.document()
            val id = docRef.id
            val updatedNotification = notification
                .copy(
                    id = id,
                    updatedAt = null,
                    createdAt = null
                )
            docRef.set(updatedNotification).await()
            Log.d("set_createNotification", "success")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("set_createNotification", "EEROR: ${e.message}")
            ApiResponse.Error(e.message ?: "error")
        }
    }

    override suspend fun deleteNotification(notificationId: String): ApiResponse<Unit> {
        return try {
            notificationRef
                .document(notificationId)
                .delete()
                .await()
            Log.d("set_deleteNotification", "success")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("set_deleteNotification", "EEROR: ${e.message}")
            ApiResponse.Error(e.message ?: "error")
        }
    }

    override suspend fun markAsRead(notificationId: String): ApiResponse<Unit> {
        return try {
            notificationRef
                .document(notificationId)
                .update(
                    mapOf(
                        "read" to true,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                )
                .await()

            Log.d("set_deleteNotification", "success")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("set_deleteNotification", "EEROR: ${e.message}")
            ApiResponse.Error(e.message ?: "error")
        }
    }

    override suspend fun deactivateNotification(notificationId: String): ApiResponse<Unit> {
        //update active
        return try {
            notificationRef
                //.whereEqualTo("id", notification.id)
                .document(notificationId)
                .update(
                    mapOf(
                        "active" to false,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                )
                .await()
            Log.d("checkRepo_updateNotification", "Success")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("checkRepo_updateNotification", "error ${e.message}")
            ApiResponse.Error(e.message ?: "error")
        }

    }

    override fun getUnreadCount(userId: String): Flow<ApiResponse<Int>> = callbackFlow {
        /*
        * hien tai fetch heet dữ liệu chỉ để lấy count ko tối ưu
        * 1 là tải dạng suspend (
        * .count()
        * .get(AggregateSource.SERVER)
        *  .await()
        *  .countko -> cách này ko real time
        * 2 la lưu unreadCount và update bằng write batch
        *
        *
        *
        * */

        val listener = notificationRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("active", true)
            .whereEqualTo("read", false)
            .addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    Log.d("check_noti_getUnreadCount", exception.message ?: "error")
                    trySend(ApiResponse.Error(exception.message ?: "error"))
                    return@addSnapshotListener
                }
                val unreadCount = snapshots?.documents?.size ?: 0
                Log.d("check_noti_getUnreadCount", "success $unreadCount")
                trySend(ApiResponse.Success(unreadCount))
            }
        awaitClose { listener.remove() }
    }
}
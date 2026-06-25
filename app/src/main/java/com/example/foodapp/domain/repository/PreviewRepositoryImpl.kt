package com.example.foodapp.domain.repository

import android.util.Log
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.PreviewRepository
 import com.example.foodapp.domain.model.RestaurantPreview
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.jvm.java


class PreviewRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : PreviewRepository {

    private val previewRef = firestore.collection(Constance.COLLECTION_PREVIEW)

    override fun observePreviews(restaurantId: String): Flow<ApiResponse<List<RestaurantPreview>>> =
        callbackFlow {
            val listener = previewRef
                .whereEqualTo("restaurantId", restaurantId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshots, exception ->

                    if (exception != null) {
                        Log.d("check_observePreviews", "error ${exception.message}")
                        trySend(ApiResponse.Error(exception.message ?: "error"))
                        return@addSnapshotListener
                    }

                    val previews = snapshots?.documents?.mapNotNull {
                        it.toObject(RestaurantPreview::class.java)
                    } ?: emptyList()

                    Log.d("check_observePreviews", "Success $previews")
                    trySend(ApiResponse.Success(previews))
                }

            awaitClose { listener.remove() }
        }

    override suspend fun createPreview(
        orderId: String,
        preview: RestaurantPreview,
    ): ApiResponse<Unit> {
        return try {
            //orderId -> để tránh user spam preview cùng 1 đơn hàng
            val doc = previewRef.document(orderId)
            val previewId = doc.id
            doc
                .set(
                    preview.copy(previewId = previewId)
                )
                .await()

            Log.d("check_createPreview", "success")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("check_createPreview", "error ${e.message}")
            ApiResponse.Error(e.message ?: "error")
        }
    }

    override suspend fun deletePreview(previewId: String): ApiResponse<Unit> {
        return try {
            previewRef
                .document(previewId)
                .delete()
                .await()
            Log.d("check_deletePreview", "SUCCESS")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("check_deletePreview", "ERROR")
            ApiResponse.Error(e.message ?: "error")
        }
    }
}
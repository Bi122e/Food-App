package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.CategoryRepository
import com.example.foodapp.domain.model.Category
import com.example.foodapp.domain.model.Conversation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore
)
: CategoryRepository {
    private val categoriesCollection = firestore.collection(Constance.COLLECTION_CATEGORIES)

    override suspend fun createCategory(category: Category): ApiResponse<String> {
        return try {
            val categoryRef = categoriesCollection.document()
            val categoryId = categoryRef.id
            val categoryWithId = category.copy(
                id = categoryId,
                createdAt = Date(),
                updatedAt = Date(),
            )
            categoryRef.set(category)
            ApiResponse.Success(categoryId)

        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to create category")
        }
    }

    override suspend fun getAllCategories(): Flow<ApiResponse<List<Category>>> = callbackFlow {
        val listener = categoriesCollection
            .orderBy("order", Query.Direction.DESCENDING)
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Failed to get all category"))
                    return@addSnapshotListener
                }
                val categories = snapshot?.documents?.mapNotNull {
                    it.toObject(Category::class.java)
                } ?: emptyList()
                trySend(ApiResponse.Success(categories))
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getCategoryById(categoryId: String): ApiResponse<Category> {
        return try {
//            val categoryRef = categoriesCollection
//                .whereEqualTo("categoryId", categoryId)
//                .get()
//                .await()
            val categoryRef = categoriesCollection
                .document(categoryId)
                .get()
                .await()
            categoryRef.toObject(Category::class.java)
                ?: return ApiResponse.Error("Category not found")

            ApiResponse.Error("Category not Found")

        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get category")
        }
    }

    override suspend fun getCategoriesOrderedByOrder(): ApiResponse<List<Category>> {
        return try {
            val snapshot = categoriesCollection
                .orderBy("order", Query.Direction.ASCENDING)
                .orderBy("name", Query.Direction.DESCENDING)
                 .get()
                .await()
            val categories = snapshot.documents.mapNotNull {
                it.toObject(Category::class.java)
            }
            ApiResponse.Success(categories)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get ordered categories")
        }
    }

    override suspend fun searchCategories(query: String): ApiResponse<List<Category>> {
        return try {
            val snapshot = categoriesCollection
//                .whereGreaterThanOrEqualTo("name", query)
//                .whereLessThanOrEqualTo("name", query + "\uf8ff")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .orderBy("order", Query.Direction.ASCENDING)
                .get()
                .await()

            val category = snapshot.documents.mapNotNull { snapshot ->
                snapshot.toObject(Category::class.java)
            }

            ApiResponse.Success(category)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to search categories")
        }
    }

    override suspend fun deleteCategory(categoryId: String): ApiResponse<Unit> {
        return try {
            categoriesCollection
                .document(categoryId)
                .delete()
                .await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to delete category")
        }
    }

    override suspend fun updateCategory(category: Category): ApiResponse<Unit> {
        return try {
            val updatedCategory = category.copy(
                updatedAt = Date()
            )
            categoriesCollection
                .document(category.id)
                .set(updatedCategory)
                .await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update category")
        }
    }
}
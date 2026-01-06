package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun createCategory(category: Category): ApiResponse<String>

    suspend fun getCategoryById(categoryId: String): ApiResponse<Category>

    suspend fun getAllCategories(): Flow<ApiResponse<List<Category>>>

    suspend fun updateCategory(category: Category): ApiResponse<Unit>

    suspend fun deleteCategory(categoryId: String): ApiResponse<Unit>

    suspend fun searchCategories(query: String): ApiResponse<List<Category>>

    suspend fun getCategoriesOrderedByOrder(): ApiResponse<List<Category>>
}
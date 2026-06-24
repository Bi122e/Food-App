package com.example.foodapp.core

import com.google.firebase.firestore.DocumentSnapshot

data class PaginationResult<T>(

    val data: List<T>,
    val lastDoc: DocumentSnapshot?,
    val isEndReached: Boolean,
)

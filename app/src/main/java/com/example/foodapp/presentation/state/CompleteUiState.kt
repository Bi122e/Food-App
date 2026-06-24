package com.example.foodapp.presentation.state

data class CompleteUiState(

    val isOrderLoading: Boolean = false,
    val isCreateLoading: Boolean = false,
    val isOrderError: Boolean = false,
//    val isCreateError: Boolean = false,
    val restaurantImgUrls: String = "",
    val restaurantName: String = "",
    val restaurantId: String = "",
    val userName: String = "",
    val notificationId: String = "",
    val message: String = "",
    val rating: Int? = null ,
    val previewTags: List<String> = emptyList(),
     val isPrivateName: Boolean = false,
)

//convert rating ->
fun Int?.toMappingRatingCount(): String? {
    return when {
        this == 1 -> "OneStars"
        this == 2 -> "twoStars"
        this == 3 -> "threeStars"
        this == 4 -> "fourStars"
        this == 5 -> "fiveStars"
        else -> null
    }
}

package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.VariationOption

data class CartUiState(
    val cart: Cart? = null,
    val restaurant: Restaurant? = null,
    val loadingFoodIds: Set<String> = emptySet(), //luwu key (foodkey + optionkey) vì var.id có thể trùng với option cũ, ví dụ cùng op
    val currentEditingItem: ActiveCartItemUi? = null, //luu state option cua user, checkboxx, radio,..
    val error: String? = null,

    val showConfirmDialog: Boolean = false,
    val conflictData: ConflictData? = null,
    val pending: ActiveCartItemUi? = null,

)

data class ActiveCartItemUi(
    val food: Food,
//    val price: Int = 0, k nen dung
    val quantity: Int = 1,
    val variations: Map<String, List<VariationOption>> = emptyMap(),
//    val selectedOption: Map<String, Set<String>> = emptyMap(),
    val note: String = ""
)

//flow này phức tạp nên tách ra layer riêng, tránh leak ra ui
data class ConflictData(
    val message: String,
    val oldRestaurantName: String,
    val newRestaurantName: String,
)
fun ActiveCartItemUi.getTotalPrice(): Int{
    return food.getPriceWithVariation(variations) * quantity
}





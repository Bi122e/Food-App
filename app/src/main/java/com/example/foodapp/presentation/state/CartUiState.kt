package com.example.foodapp.presentation.state

import com.example.foodapp.core.utils.buildCartItemKey
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.VariationOption

data class CartUiState(
    val cart: Cart? = null,
    val restaurant: Restaurant? = null,
    val loadingItemKeys: Set<String> = emptySet(), //luwu key (foodkey + optionkey) vì var.id có thể trùng với option cũ, ví dụ cùng op
    val currentEditingItem: ActiveCartItemUi? = null, //luu state option cua user, checkboxx, radio,..
    val currentEditingCart: List<String>? = null,
    val error: String? = null,

    val showConfirmDialog: Boolean = false,
    val conflictData: ConflictData? = null,
     // thì cần active để lưu dữ liệu mới

)


//item đang edit
data class ActiveCartItemUi(

    val food: Food,
//    val price: Int = 0, k nen dung
    val quantity: Int = 1,
    val variations: Map<String, Set<VariationOption>> = emptyMap(),
//    val selectedOption: Map<String, Set<String>> = emptyMap(),
    val note: String = ""
) {
    val key: String
        get() = buildCartItemKey(food.foodId, variations)
}

//data cũ và mới
data class ConflictData(
    val oldRestaurantName: String,
    val newRestaurantName: String,
    val item: ActiveCartItemUi,
    val restaurant: Restaurant,
) {

    val message: String
        get() = "Giỏ hàng hiện đang chứa món từ $oldRestaurantName. " +
                "Nếu tiếp tục, toàn bộ món hiện tại sẽ bị xóa và thay bằng món từ $newRestaurantName."
}
fun ActiveCartItemUi.getTotalPrice(): Long{
    return food.getPriceWithVariation(variations) * quantity
}





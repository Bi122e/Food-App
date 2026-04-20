package com.example.foodapp.domain.model


/* cart chua item, item chua variation, variation chua cac option
* cấu trúc cartItem(id = "1", name = "Bánh nếp", price = 10...
*   variations: lưu các thông tin user đã chọn
* mapOf("size" to "M, "do an them" to "...")
*  */
// variation (ten option vd: size, option(ten vd: M, S, L)
data class CartItem(
    val key: String = "", //luu key tranh 1 foodId bi trung voi nhieu op
    val foodId: String = "",
    val name: String = "",
    val basePrice: Int = 0,
    val quantity: Int = 0,
    val imgUrls: String = "",
    val restaurantId: String = "",
    val notes: String = "",
//    val variation: Map<String, Set<String>> = emptyMap() //refactor kien truc moi
    val variation: Map<String, List<VariationOption>> = emptyMap(),
) {

    //flattent la hàm làm phẳng vd: map.value -> [ Variation("M"),Variation("L"),Variation("cheese")]
//    val totalPrice: Int
//        get() = (basePrice + variation.values.flatten().sumOf { it.price }) * quantity
    fun     validate(): Boolean {
        return foodId.isNotEmpty() &&
                name.isNotEmpty() &&
                basePrice >= 0 &&
                quantity >= 1
    }


//    fun getTotalPrice(): Int = basePrice * quantity

//    fun getFormattedPrice(): String {
//        return "$basePrice đ"
//    }

    //    fun getUniqueKey(): String {
//        val variationKey = variation.joinToString("|") { variation ->
//            variation.options.joinToString(","){"${it.name}: ${it.price}"}
//        }
//        return "${foodId}-${variationKey}"
//    }
    fun updateQuantity(newQuantity: Int): CartItem {
        require(newQuantity > 0) { "Quantity must be greater than 0" }
        return copy(quantity = newQuantity)
    }

    fun incrementQuantity(): CartItem {
        require(quantity < 20) { "Quantity must be less than 20" }
        return copy(quantity = quantity + 1)
    }

    fun decrementQuantity(): CartItem {
        require(quantity > 1) { "Quantity must be greater than 1" }
        return copy(quantity = quantity - 1)
    }

    fun getTotalPrice(): Int {
       val variationPrice =  variation.values.flatten().sumOf { it.price }
        return (variationPrice + basePrice) * quantity
    }

    //co the nen dung property de clean code hon
//    val totalPrice: Int
//        get() {
//            val variationPrice = variation.values.flatten().sumOf { it.price }
//            return (basePrice + variationPrice) * quantity
//        }
    //variation = {"size" to list("x", "y","z")
    //list< variation(name, price, variationOption(name, price)),
//          variation(name, price, variationOption(name, price))>.flatten()
    // variation
    //câu hoỉ đặt ra flatten() có dạng như thế nào
    //    fun Food.toCartItem(): CartItem = CartItem(
//        foodId = foodId,
//        name = name,
//        price = price,
//        quantity = quantity,
//        imgUrls = imgUrls,
//        restaurantId = restaurantId,
//        notes = notes,
//        variation = variation
//    )
// File: domain/model/Food.kt hoặc FoodExt.kt

}

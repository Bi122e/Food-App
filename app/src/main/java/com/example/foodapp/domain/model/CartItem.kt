package com.example.foodapp.domain.model


/* cart chua item, item chua variation, variation chua cac option
* cấu trúc cartItem(id = "1", name = "Bánh nếp", price = 10...
*   variations = listOf(variation(name ="Size", option = listOf((name = "nho", price = 120))
*  */
data class CartItem(
    val foodId: String = "",
    val name: String = "",
    val price: Int = 0,
    val quantity: Int = 0,
    val imgUrls: String = "",
    val restaurantName: String = "",
    val notes: String = "",
    val variation: List<Variation> = emptyList()

) {
    fun isValid(): Boolean {
        return foodId.isNotEmpty() &&
                name.isNotEmpty() &&
                price >= 0 &&
                quantity >= 1
    }

    fun getTotalPrice(): Int = price * quantity

    fun getFormattedPrice(): String {
        return "$price đ"
    }

    fun getUniqueKey(): String {
        val variationKey = variation.joinToString("|") { variation ->
            variation.options.joinToString(","){"${it.name}: ${it.price}"}
        }
        return "${foodId}-${variationKey}"
    }
    fun updateQuantity(newQuantity: Int): CartItem {
        require(newQuantity > 0){"Quantity must be greater than 0"}
        return copy(quantity = newQuantity)
    }

    fun incrementQuantity(): CartItem{
        require(quantity < 20){"Quantity must be less than 20"}
        return copy(quantity = quantity + 1)
    }
    fun decrementQuantity(): CartItem {
        require(quantity > 1) {"Quantity must be greater than 1"}
        return copy(quantity = quantity - 1)
    }

}

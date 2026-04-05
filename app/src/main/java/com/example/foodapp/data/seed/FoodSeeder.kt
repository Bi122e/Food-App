package com.example.foodapp.data.seed

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.example.foodapp.core.Constance
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.domain.model.VariationOption
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object FoodSeeder {
    private const val SEED_VERSION = 9

    suspend fun seedIfNeeded(context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val currentVersion = prefs.getInt("seed_version", 0)

        if (currentVersion < SEED_VERSION) {
            seedAllPizza()
            prefs.edit { putInt("seed_version", SEED_VERSION) }
        }
        Log.d("Seeder", "Current: $currentVersion - Code: $SEED_VERSION")
    }
    suspend fun seedAllPizza() {
        val firebase = FirebaseFirestore.getInstance()
        val collection = firebase.collection(Constance.COLLECTION_FOOD)



        val foods = listOf(
            createPizza4Cheese(),
            createPizzaBongCai(),
            createPizzaHaiSan(),
            createPizzaPepperoni1(),
            createPizzaPepperoni2(),
            createPizzaXucXich(),
            createPizzaTest()
        )

        try {
            foods.forEach { food ->
                collection
                    .document(food.foodId)
                    .set(food, SetOptions.merge())
                    .await()
            }
            Log.e("seedAllPizza", "Seed Pizza Successful")
        } catch (e: Exception) {
            Log.e("seedAllPizza", "Seed Pizza Failed ${e.message}")
        }
    }

    private fun setDefaultOption(): List<Variation> {

        val options = listOf(
            VariationOption("Size S", 35000, "sizeS", "", true),
            VariationOption("Size M", 45000, "sizeM", "", true),
            VariationOption("Size L", 55000, "sizeL", "", true),
            )

        val variations = listOf(
            Variation("Size", "size", "Chỉ được chọn một kích thước", Variation.VariationType.SINGLE, true, 1, 1, options   )
        )

        return variations
    }

    private fun extraToppingVariation() = Variation(
        name = "Thêm topping",
        id = "extraTopping",
        description = "Có thể chọn nhiều topping",
        type = Variation.VariationType.MULTI,
        required = false,
        minSelection = 0,
        maxSelection = 5,
        options = listOf(
            VariationOption("Extra cheese", 20000, "extra_cheese", "Thêm phô mai", true),
            VariationOption("Extra pepperoni", 30000, "extra_pepperoni", "Thêm pepperoni", true),
            VariationOption("Extra seafood", 40000, "extra_seafood", "Thêm hải sản", true)
        )
    )

    private fun createPizzaPepperoni1() = Food(

        name = "Pizza Pepperoni 1",
        nameLower = "pizza pepperoni 1",
        foodId = "pizza_Pepperoni_1",
        description = "Pizza Quattro Formaggi với mozzarella, cheddar, parmesan và blue cheese.",
        price = 70000,
        imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751529947/pizza5_kom9ey.jpg",
        available = true,
        reviewCount = 3,
        totalRating = 12.6,
        averageRating = 4.2,
        foodTime = 30,
        ingredient = "Phô mai Mozzarella, Cheddar, Parmesan và phô mai xanh Blue ",
        calories = 255,
        restaurantId = "pizza_hurt",
        categoryId = "pizza",
        variations = setDefaultOption()
    )

    private fun createPizzaPepperoni2() = Food(

        name = "Pizza pepperoni loại 2",
        nameLower = "pizza pepperoni loại 2",
        foodId = "pizza_pepperoni_2",
        description = "Phiên bản pepperoni cay nồng và đậm vị hơn.",
        price = 75000,
        imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751529944/pizza4_ftaon4.jpg",
        available = true,
        reviewCount = 3,
        totalRating = 12.6,
        averageRating = 4.2,
        foodTime = 20,
        ingredient = "Pepperoni cay, Mozzarella",
        calories = 266,
        restaurantId = "pizza_hurt",
        categoryId = "pizza",
        variations = setDefaultOption()
    )

    private fun createPizza4Cheese() = Food(
        name = "Pizza 4 loại phô mai",
        nameLower = "pizza 4 loại phô mai",
        foodId = "pizza_4_cheese",
        description = "Pizza Quattro Formaggi với mozzarella, cheddar, parmesan và blue cheese.",
        price = 0,
        imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751529947/pizza5_kom9ey.jpg",
        available = true,
        reviewCount = 3,
        totalRating = 12.6,
        averageRating = 4.2,
        foodTime = 20,
        ingredient = "Mozzarella, Cheddar, Parmesan, Blue cheese",
        calories = 266,
        restaurantId = "pizza_hurt",
        categoryId = "pizza",
        variations = setDefaultOption()
    )

    private fun createPizzaBongCai() = Food(
        name = "Pizza bông cải",
        nameLower = "pizza bông cải",
        foodId = "pizza_bong_cai",
        description = "Pizza bông cải xanh, ít chất béo và nhiều chất xơ.",
        price = 54000,
        imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751530024/pizza0_tysmwq.jpg",
        available = true,
        reviewCount = 3,
        totalRating = 12.6,
        averageRating = 4.2,
        foodTime = 20,
        ingredient = "Bông cải xanh, Mozzarella, Ớt chuông, Hành tây",
        calories = 266,
        restaurantId = "pizza_hurt",
        categoryId = "pizza",
        variations = setDefaultOption()
    )

    private fun createPizzaHaiSan() = Food(
        name = "Pizza hải sản",
        nameLower = "pizza hải sản",
        foodId = "pizza_hai_san",
        description = "Pizza hải sản đậm đà với tôm, mực tươi.",
        price = 54000,
        imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751529957/pizza3_kz3sv3.jpg",
        available = true,
        reviewCount = 3,
        totalRating = 12.6,
        averageRating = 4.2,
        foodTime = 20,
        ingredient = "Tôm, Mực, Bắp ngọt, Mozzarella",
        calories = 266,
        restaurantId = "pizza_hurt",
        categoryId = "pizza",
        variations = setDefaultOption()
    )
    private fun createPizzaXucXich() = Food(
        name = "Pizza xúc xích",
        nameLower = "pizza xúc xích",
        foodId = "pizza_xuc_xich",
        description = "Pizza xúc xích với lớp phô mai béo ngậy.",
        price = 54000,
        imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751529957/pizza2_d06ggs.jpg",
        available = true,
        reviewCount = 3,
        totalRating = 12.6,
        averageRating = 4.2,
        foodTime = 20,
        ingredient = "Xúc xích heo, Xúc xích cay, Mozzarella",
        calories = 266,
        restaurantId = "pizza_hurt",
        categoryId = "pizza",
        variations = setDefaultOption()
    )
    private fun createPizzaTest() = Food(
        name = "Pizza xúc xích",
        nameLower = "pizza xúc xích",
        foodId = "pizza_xuc_xich",
        description = "Pizza xúc xích với lớp phô mai béo ngậy.",
        price = 54000,
        imgUrl = "",
        available = true,
        reviewCount = 3,
        totalRating = 12.6,
        averageRating = 4.2,
        foodTime = 20,
        ingredient = "Xúc xích heo, Xúc xích cay, Mozzarella",
        calories = 266,
        restaurantId = "pizza_hurt",
        categoryId = "pizza",
        variations = emptyList(),
    )
}
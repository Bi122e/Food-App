package com.example.foodapp.utils

import android.util.Log
import com.example.foodapp.data.model.category.Category
import com.google.firebase.firestore.FirebaseFirestore

object CategorySeeder {
    fun seedCategories() {
        val db = FirebaseFirestore.getInstance()
//        val categories = listOf(
//            Category(
//                id = "nuoc",
//                name = "Nước",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023154/water_encelw.png",
//                slug = "nuoc"
//            ),
//            Category(
//                id = "pizza",
//                name = "Pizza",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023154/pizza_ysel49.png",
//                slug = "pizza"
//            ),
//            Category(
//                id = "com-chien",
//                name = "Cơm chiên",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023154/fried-rice_ubayiz.png",
//                slug = "com-chien"
//            ),
//            Category(
//                id = "com-chien",
//                name = "Cơm chiên",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023154/fried-rice_ubayiz.png",
//                slug = "com-chien"
//            ),
//            Category(
//                id = "banh-mi",
//                name = "Bánh mì",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023154/fried-rice_ubayiz.png",
//                slug = "banh-mi"
//            ),
//            Category(
//                id = "banh-cake",
//                name = "Bánh cake",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023151/cake_ii0nar.png",
//                slug = "banh-cake"
//            ),
//            Category(
//                id = "salad",
//                name = "Salad",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023150/salad_wgsbkk.png",
//                slug = "salad"
//            ),
//            Category(
//                id = "banh-hamburger",
//                name = "Bánh Hamburger",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023150/hambuger_sxskab.png",
//                slug = "banh-hamberger"
//            ),
//            Category(
//                id = "tra-sua",
//                name = "Trà sữa",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023149/bubble-tea_guxpcx.png",
//                slug = "Trà sửa"
//            ),
//            Category(
//                id = "ca-phe",
//                name = "Cà phê",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023149/coffee-cup_jwqzdf.png",
//                slug = "Cà phê"
//            ),
//            Category(
//                id = "ga-chien",
//                name = "Gà chiên",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023148/fried-chicken_cx4o8q.png",
//                slug = "ga-chien"
//            ),
//            Category(
//                id = "tra",
//                name = "Trà",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023148/tea-cup_gpd2ou.png",
//                slug = "tra"
//            ),
//            Category(
//                id = "kem",
//                name = "Kem",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023148/ice-cream_p78as4.png",
//                slug = "kem"
//            ),
//            Category(
//                id = "nuoc-ep",
//                name = "Nước ép",
//                iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749023147/drink_mmwic4.png",
//                slug = "nuoc-ep"
//            ),
//
//            )
//        categories.forEach { category ->
//            val data = mapOf(
//                "id" to category.id,
//                "name" to category.name,
//                "slug" to category.slug,
//                "iconUrl" to category.iconUrl
//            )
//            db.collection("categories")
//                .document(category.id)
//                .set(data)
//                .addOnSuccessListener {
//                    Log.d("Seeder", "Add on complete")
//                }
//                .addOnFailureListener { e ->
//                    Log.e("Seeder", "Error ${category.name}", e)
//                }
//        }
    }
}
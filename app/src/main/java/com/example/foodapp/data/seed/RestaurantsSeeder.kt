//package com.example.foodapp.utils
//
//import com.example.foodapp.data.model.restaurant.Restaurant
//import com.google.firebase.firestore.FirebaseFirestore
//
//object RestaurantsSeeder {
//    fun seedRestaurant() {
//        val db = FirebaseFirestore.getInstance()
//        val restaurants = listOf(
//            Restaurant(
//                restaurantId = "phuclong",
            //                restaurantName = "Phúc Long Coffee & Tea",
//                description = "Thương hiệu trà và cà phê nổi tiếng Việt Nam với hương vị đậm đà.",
//                imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103446/logo_phuclong_qaa02v.png",
//                phoneNumber = "0901234567",
//                address = "123 Nguyễn Trãi, Quận 1, TP.HCM",
//                rating = 4.8f,
//                email = "support@phuclong.vn",
//                totalReview = 5120,
//                deliveryFree = 0.0,
//                minOrderAmount = 50000.0,
//                estimatedDeliveryTime = 25,
//                isOpen = true,
//                categories = listOf("ca-phe", "tra", "nuoc-ep")
//            ),
//            Restaurant(
//                restaurantId = "pizzahurt",
//                restaurantName = "Pizza Hurt",
//                description = "Pizza theo phong cách Mỹ với lớp phô mai đầy đặn và hương vị khó cưỡng.",
//                imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103438/logo_pizzahurt_gwg20n.png",
//                phoneNumber = "0987223344",
//                address = "45 Lê Văn Sỹ, Phú Nhuận, TP.HCM",
//                rating = 4.5f,
//                email = "contact@pizzahurt.com",
//                totalReview = 4321,
//                deliveryFree = 15000.0,
//                minOrderAmount = 70000.0,
//                estimatedDeliveryTime = 30,
//                isOpen = true,
//                categories = listOf("pizza", "ga-chien", "nuoc")
//            ),
//            Restaurant(
//                restaurantId = "domino",
//                restaurantName = "Domino's Pizza",
//                description = "Chuỗi nhà hàng pizza nổi tiếng thế giới, giao hàng nhanh, chất lượng đảm bảo.",
//                imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103438/logo_domino_l1ney6.png",
//                phoneNumber = "0977554433",
//                address = "80 Trần Hưng Đạo, Quận 5, TP.HCM",
//                rating = 4.6f,
//                email = "support@dominos.vn",
//                totalReview = 3900,
//                deliveryFree = 10000.0,
//                minOrderAmount = 80000.0,
//                estimatedDeliveryTime = 28,
//                isOpen = true,
//                categories = listOf("pizza", "nuoc", "tra-sua")
//            ),
//            Restaurant(
//                restaurantId = "mcdonald",
//                restaurantName = "McDonald's",
//                description = "Chuỗi thức ăn nhanh nổi tiếng toàn cầu với hamburger và khoai tây chiên đặc trưng.",
//                imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103438/logo_mcdonald_ieotis.png",
//                phoneNumber = "0966778899",
//                address = "200 Điện Biên Phủ, Bình Thạnh, TP.HCM",
//                rating = 4.4f,
//                email = "hello@mcdonalds.vn",
//                totalReview = 2890,
//                deliveryFree = 0.0,
//                minOrderAmount = 60000.0,
//                estimatedDeliveryTime = 20,
//                isOpen = true,
//                categories = listOf("banh-hamburger", "ga-chien", "nuoc-ngot")
//            )
//        )
//        restaurants.forEach { restaurant ->
//            db.collection("restaurants")
//                .document(restaurant.restaurantId)
//                .set(restaurant)
//        }
//    }
//}
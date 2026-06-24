package com.example.foodapp.data.seed

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.example.foodapp.core.Constance
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.domain.model.VariationOption
import com.example.foodapp.utils.RestaurantsSeeder
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object FoodSeeder {
    private const val SEED_VERSION = 25


    suspend fun seedIfNeeded(context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val currentVersion = prefs.getInt("seed_version", 0)

        Log.d(
            "seed_version",
            "check: $currentVersion < $SEED_VERSION = ${currentVersion < SEED_VERSION}"
        )

        if (currentVersion < SEED_VERSION) {
            RestaurantsSeeder.seedRestaurant()
            val firestore = FirebaseFirestore.getInstance()
             val notificationRef = firestore.collection(Constance.COLLECTION_NOTIFICATION)
            FirebaseApp.getInstance().options.projectId?.let {
                Log.d(
                    "firebase_project",
                    it
                )
            }

//            restaurants.forEach { restaurant ->
//                val updateRes = restaurant.copy(
//                    updatedAt = null,
//                    createdAt = null
//                )
//                db.collection("restaurants")
//                    .document(updateRes.restaurantId)
//                    .set(updateRes, SetOptions.merge())
//                    .await()

//                AppNotificationSeeder.seederNotification.forEach {
//                    try {
//                        val updatedNotification = it.copy(
//                            updatedAt = null,
//                            createdAt = null
//                        )
//                        notificationRef
//                            .document()
//                            .set(updatedNotification, SetOptions.merge())
//                            .await()
//                        val count = notificationRef.get().await()
//                        Log.d(
//                            "seed_version",
//                            "check_notification_count = ${count.size()}"
//                        )
//
//                        Log.d("seed_version", "success")
//                    } catch (e: Exception) {
//                        Log.d("seed_version", "error ${e.message}")
//                    }
//
//                }


//            seedFood()
//            RestaurantsSeeder.seedRestaurant()

                prefs.edit { putInt("seed_version", SEED_VERSION) }
            }
            Log.d("seed_version", "Current version: $currentVersion - Code: $SEED_VERSION")
        }

        suspend fun seedFood() {
            val firebase = FirebaseFirestore.getInstance()
            val collection = firebase.collection(Constance.COLLECTION_FOOD)


            val foods = listOf(
                createPizza4Cheese(),
                createPizzaBongCai(),
                createPizzaHaiSan(),
                createPizzaPepperoni1(),
                createPizzaPepperoni2(),
                createPizzaXucXich(),
                createPizzaTest(),
                createBanhBotLoc(),
                createBanhDaCua(),
                createBanhMi247(),
                createBanhMiChaoGiaHuy(),
                createBanhQueKyDuyen(),
                createBanhXeoHaiTu1(),
                createBunDau(),
                createBunMoc(),
                createBunRieu(),
                createCaKho(),
                createComTam(),
                createComChien(),
                createGoiCuon(),
                createCaPhe(),
                createNuocGiaiKhat(),
                createMiQuang(),
                createPhoHaNoi(),
                createBunChaHaNoi1(),
                createBunThitNuong(),
                createTraMe(),
                createTraSua(),
                createTraNhietDoi(),


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
                Variation(
                    "Size",
                    "size",
                    "Chỉ được chọn một kích thước",
                    Variation.VariationType.SINGLE,
                    true,
                    1,
                    1,
                    options
                )
            )

            return variations
        }

        private fun setDefaultOption3(): List<Variation> {

            val options = listOf(
                VariationOption("Thịt thêm", 35000, "toppingA", "", true),
                VariationOption("Tôm thê,", 45000, "toppingB", "", true),
            )

            val variations = listOf(
                Variation(
                    "Topping",
                    "topping",
                    "Chỉ được chọn một kích thước",
                    Variation.VariationType.MULTI,
                    false,
                    0,
                    25,
                    options
                )
            )

            return variations
        }

        private fun setDefaultOption2(): List<Variation> {
            val options1 = listOf(
                VariationOption("Size S", 35000, "sizeS", "", true),
                VariationOption("Size M", 45000, "sizeM", "", true),
                VariationOption("Size L", 55000, "sizeL", "", true),
            )
            val options2 = listOf(
                VariationOption("Tôm thêm", 35000, "tom", "", true),
                VariationOption("Thịt thêm", 45000, "thit", "", true),
                VariationOption("Mô mai thêm", 55000, "phomai", "", true),
            )

            val variations = listOf(
                Variation(
                    "Size",
                    "size",
                    "Chỉ được chọn một kích thước",
                    Variation.VariationType.SINGLE,
                    true,
                    1,
                    1,
                    options1
                ),
                Variation(
                    "Size",
                    "size",
                    "nhiều lựa chọn ",
                    Variation.VariationType.MULTI,
                    true,
                    1,
                    20,
                    options2
                )

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
                VariationOption(
                    "Extra pepperoni",
                    30000,
                    "extra_pepperoni",
                    "Thêm pepperoni",
                    true
                ),
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
            categoriesId = listOf("pizza", "banh"),
            variations = setDefaultOption()
        )

        private fun createCaPhe() = Food(
            name = "Cà Phê Sữa Đá",
            nameLower = "ca phe sua da",
            foodId = "ca_phe1",
            description = "",
            price = 30000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031461/c%C3%A0_ph%C3%AA_1_cerkiv.jpg",
            available = true,
            reviewCount = 32,
            totalRating = 150.0,
            averageRating = 4.9,
            foodTime = 10,
            ingredient = "Cà phê phin, sữa đặc",
            calories = 180,
            restaurantId = "ca_phe1",
            categoriesId = listOf("nuoc", "ca-phe"),
            variations = setDefaultOption3(),
        )

        private fun createMiQuang() = Food(
            name = "Mì Quảng",
            nameLower = "mi quang",
            foodId = "mi_quang1",
            description = "",
            price = 52000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031601/m%C3%AC_qu%E1%BA%A3ng_2_ew6qgh.jpg",
            available = true,
            reviewCount = 17,
            totalRating = 78.0,
            averageRating = 4.7,
            foodTime = 28,
            ingredient = "Mì quảng, tôm, thịt, đậu phộng",
            calories = 540,
            restaurantId = "mi_quang1",
            categoriesId = listOf("mi", "mi-quang"),
            variations = setDefaultOption3(),
        )

        private fun createPhoHaNoi() = Food(
            name = "Phở Bò Hà Nội",
            nameLower = "pho bo ha noi",
            foodId = "pho_ha_noi1",
            description = "",
            price = 60000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031643/ph%E1%BB%9F_2_q83cbd.jpg",
            available = true,
            reviewCount = 25,
            totalRating = 118.0,
            averageRating = 4.8,
            foodTime = 30,
            ingredient = "Bánh phở, bò, nước dùng",
            calories = 500,
            restaurantId = "pho_ha_noi1",
            categoriesId = listOf("pho", "pho-ha-noi"),
            variations = setDefaultOption2(),
        )

        private fun createTraNhietDoi() = Food(
            name = "Trà Nhiệt Đới",
            nameLower = "tra nhiet doi",
            foodId = "tra_nhiet_doi1",
            description = "",
            price = 38000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031711/120d823b366e3dfa008afe8998fc2689_mo4hsv.jpg",
            available = true,
            reviewCount = 18,
            totalRating = 82.0,
            averageRating = 4.6,
            foodTime = 10,
            ingredient = "Trà đào, chanh dây, cam",
            calories = 210,
            restaurantId = "tra_nhiet_doi1",
            categoriesId = listOf("nuoc", "tra-nhiet-doi"),
            variations = setDefaultOption2(),
        )

        private fun createTraMe() = Food(
            name = "Trà Me",
            nameLower = "tra me",
            foodId = "tra_me1",
            description = "",
            price = 25000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031685/tr%C3%A0_me_2_yfkijp.jpg",
            available = true,
            reviewCount = 10,
            totalRating = 44.0,
            averageRating = 4.4,
            foodTime = 10,
            ingredient = "Me, đường, đá",
            calories = 160,
            restaurantId = "tra_me1",
            categoriesId = listOf("nuoc", "tra-me"),
            variations = setDefaultOption3(),
        )

        private fun createTraSua() = Food(
            name = "Trà Sữa Trân Châu",
            nameLower = "tra sua tran chau",
            foodId = "tra_sua1",
            description = "",
            price = 45000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031765/Bubble_tea_tokxgx.jpg",
            available = true,
            reviewCount = 22,
            totalRating = 102.0,
            averageRating = 4.7,
            foodTime = 12,
            ingredient = "Trà sữa, trân châu đen",
            calories = 320,
            restaurantId = "tra_sua1",
            categoriesId = listOf("nuoc", "tra-sua"),
            variations = setDefaultOption2(),
        )

        private fun createNuocGiaiKhat() = Food(
            name = "Nước Cam Ép",
            nameLower = "nuoc cam ep",
            foodId = "nuoc_giai_khat1",
            description = "",
            price = 28000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031628/n%C6%B0%E1%BB%9Bc_2_zk9aue.jpg",
            available = true,
            reviewCount = 13,
            totalRating = 56.0,
            averageRating = 4.5,
            foodTime = 10,
            ingredient = "Cam tươi, đá",
            calories = 140,
            restaurantId = "nuoc_giai_khat1",
            categoriesId = listOf("nuoc", "nuoc-giai-khat"),
            variations = setDefaultOption3(),
        )

        private fun createComChien() = Food(
            name = "Cơm Chiên Dương Châu",
            nameLower = "com chien duong chau",
            foodId = "com_chien1",
            description = "",
            price = 50000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031544/c%C6%A1m_chi%C3%AAn_2_c0dg1e.jpg",
            available = true,
            reviewCount = 11,
            totalRating = 48.5,
            averageRating = 4.4,
            foodTime = 25,
            ingredient = "Cơm, trứng, tôm, xúc xích",
            calories = 650,
            restaurantId = "com_chien1",
            categoriesId = listOf("com", "com-chien"),
            variations = setDefaultOption3(),
        )

        private fun createGoiCuon() = Food(
            name = "Gỏi Cuốn Tôm Thịt",
            nameLower = "goi cuon tom thit",
            foodId = "goi_cuon1",
            description = "",
            price = 35000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031575/g%E1%BB%8Fi_cu%E1%BB%91n_1_bh2mkw.jpg",
            available = true,
            reviewCount = 14,
            totalRating = 62.0,
            averageRating = 4.6,
            foodTime = 20,
            ingredient = "Bánh tráng, tôm, thịt, rau",
            calories = 290,
            restaurantId = "goi_cuon1",
            categoriesId = listOf("goi", "goi-cuon"),
            variations = setDefaultOption2(),
        )

        private fun createComTam() = Food(
            name = "Cơm Tấm Sườn",
            nameLower = "com tam suon",
            foodId = "com_tam1",
            description = "",
            price = 55000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031522/c%C6%A1m_x%C3%A1_x%C3%ADu_nfnhlq.jpg",
            available = true,
            reviewCount = 20,
            totalRating = 94.0,
            averageRating = 4.7,
            foodTime = 30,
            ingredient = "Cơm tấm, sườn nướng, bì, chả",
            calories = 720,
            restaurantId = "com_tam1",
            categoriesId = listOf("com", "com-tam"),
            variations = setDefaultOption3(),
        )


        private fun createBunRieu() = Food(
            name = "Bún Riêu",
            nameLower = "bun rieu",
            foodId = "bun_rieu1",
            description = "",
            price = 42000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779030920/b%C3%BAn_ri%C3%AAu_kfg2b9.jpg",
            available = true,
            reviewCount = 14,
            totalRating = 62.0,
            averageRating = 4.7,
            foodTime = 28,
            ingredient = "Bún, riêu cua, cà chua",
            calories = 430,
            restaurantId = "bun_rieu1",
            categoriesId = listOf("bun", "bun-rieu"),
            variations = setDefaultOption3(),
        )

        private fun createBunThitNuong() = Food(
            name = "Bún Thịt Nướng",
            nameLower = "bun thit nuong",
            foodId = "bun_thit_nuong1",
            description = "",
            price = 50000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779030958/b%C3%BAn_th%E1%BB%8Bt_n%C6%B0%E1%BB%9Bng_2_lo3nfz.jpg",
            available = true,
            reviewCount = 17,
            totalRating = 76.5,
            averageRating = 4.8,
            foodTime = 27,
            ingredient = "Bún, thịt nướng, rau sống",
            calories = 560,
            restaurantId = "bun_thit_nuong1",
            categoriesId = listOf("bun", "bun-thit-nuong"),
            variations = setDefaultOption3(),
        )

        private fun createCaKho() = Food(
            name = "Cá Kho Tộ",
            nameLower = "ca kho to",
            foodId = "ca_kho1",
            description = "",
            price = 65000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031411/c%C3%A1_kho_1_w251yi.jpg",
            available = true,
            reviewCount = 11,
            totalRating = 48.0,
            averageRating = 4.6,
            foodTime = 35,
            ingredient = "Cá basa, tiêu, nước màu",
            calories = 610,
            restaurantId = "ca_kho1",
            categoriesId = listOf("com", "ca-kho"),
            variations = setDefaultOption3(),
        )

        private fun createBanhBotLoc() = Food(

            name = "Bánh Bột Lọc",
            nameLower = "banh bot loc",
            foodId = "banh_bot_loc1",
            description = "",
            price = 20000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031196/b%C3%A1nh_b%E1%BB%99t_l%E1%BB%8Dc_bwlxav.jpg",
            available = true,
            reviewCount = 5,
            totalRating = 10.6,
            averageRating = 4.0,
            foodTime = 30,
            ingredient = "Bột, tôm",
            calories = 232,
            restaurantId = "bot_loc1",
            categoriesId = listOf("banh-vn", "banh-bot-loc"),
            variations = setDefaultOption3(),
        )

        private fun createBunMoc() = Food(
            name = "Bún Mọc",
            nameLower = "bun moc",
            foodId = "bun_moc1",
            description = "",
            price = 40000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031305/b%C3%BAn_m%E1%BB%8Dc1_dsscdk.jpg",
            available = true,
            reviewCount = 9,
            totalRating = 40.5,
            averageRating = 4.5,
            foodTime = 30,
            ingredient = "Bún, mọc heo, nấm",
            calories = 410,
            restaurantId = "bun_moc1",
            categoriesId = listOf("bun", "bun-moc"),
            variations = setDefaultOption2(),
        )

        private fun createBunDau() = Food(
            name = "Bún Đậu Mắm Tôm",
            nameLower = "bun dau mam tom",
            foodId = "bun_dau1",
            description = "",
            price = 45000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031234/b%C3%BAn_%C4%91%E1%BA%ADu_1_p7yjis.jpg",
            available = true,
            reviewCount = 10,
            totalRating = 45.0,
            averageRating = 4.5,
            foodTime = 25,
            ingredient = "Bún, đậu hũ, chả cốm, mắm tôm",
            calories = 520,
            restaurantId = "bun_dau1",
            categoriesId = listOf("bun", "bun-dau"),
            variations = setDefaultOption3(),
        )

        private fun createBunChaHaNoi1() = Food(

            name = "Bún Chả Hà Nội 1",
            nameLower = "banh cha ha noi1",
            foodId = "bun_cha_ha_noi1",
            description = "",
            price = 30000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779030817/b%C3%BAn_ch%E1%BA%A3_2_fvyjhf.jpg",
            available = true,
            reviewCount = 5,
            totalRating = 10.6,
            averageRating = 4.0,
            foodTime = 30,
            ingredient = "bun, thit",
            calories = 232,
            restaurantId = "bun_cha_ha_noi1",
            categoriesId = listOf("bun", "banh-cha"),
            variations = setDefaultOption3(),
        )

        private fun createBanhXeoHaiTu1() = Food(

            name = "Bánh Xèo",
            nameLower = "banh xep",
            foodId = "banh_xeo_hai_tu1",
            description = "",
            price = 20000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031138/b%C3%A1nh_x%C3%A8o_2_q11eg3.jpg",
            available = true,
            reviewCount = 5,
            totalRating = 11.4,
            averageRating = 4.1,
            foodTime = 30,
            ingredient = "Bột, tôm",
            calories = 232,
            restaurantId = "bot_loc1",
            categoriesId = listOf("banh-vn", "banh-xeo"),
            variations = setDefaultOption(),
        )

        private fun createBanhQueKyDuyen() = Food(

            name = "Bánh Quê Kỳ Duyên",
            nameLower = "banh que ky duyen",
            foodId = "banh_que_ky_duyen1_1",
            description = "",
            price = 30000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031265/b%C3%A1nh_kh%E1%BB%8Dt_u4wdma.jpg",
            available = true,
            reviewCount = 5,
            totalRating = 10.6,
            averageRating = 4.0,
            foodTime = 30,
            ingredient = "Bột, tôm",
            calories = 232,
            restaurantId = "banh_que_ky_duyen1",
            categoriesId = listOf("banh-vn"),
            variations = setDefaultOption(),
        )

        private fun createBanhDaCua() = Food(

            name = "Bánh Đa Cua đầy đủ",
            nameLower = "danh da cua day du",
            foodId = "banh_da_cua_day_du1",
            description = "",
            price = 35000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031196/b%C3%A1nh_b%E1%BB%99t_l%E1%BB%8Dc_bwlxav.jpg",
            available = true,
            reviewCount = 5,
            totalRating = 10.6,
            averageRating = 4.0,
            foodTime = 30,
            ingredient = "Bột, tôm",
            calories = 232,
            restaurantId = "banh_da_cua177",
            categoriesId = listOf("banh-vn", "banh-da"),
            variations = emptyList(),
        )

        private fun createBanhMi247() = Food(

            name = "Bánh Đa Cua đầy đủ",
            nameLower = "danh da cua day du",
            foodId = "banh_da_cua_day_du1",
            description = "",
            price = 35000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031196/b%C3%A1nh_b%E1%BB%99t_l%E1%BB%8Dc_bwlxav.jpg",
            available = true,
            reviewCount = 5,
            totalRating = 10.6,
            averageRating = 4.0,
            foodTime = 30,
            ingredient = "Bột, tôm",
            calories = 232,
            restaurantId = "banh_mi_247",
            categoriesId = listOf("banh-vn", "banh-mi"),
            variations = emptyList(),
        )

        private fun createBanhMiChaoGiaHuy() = Food(

            name = "Bánh Mì Chảo",
            nameLower = "banh mi chao",
            foodId = "banh_mi_chao_gia_huy1",
            description = "",
            price = 38000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031196/b%C3%A1nh_b%E1%BB%99t_l%E1%BB%8Dc_bwlxav.jpg",
            available = true,
            reviewCount = 5,
            totalRating = 10.6,
            averageRating = 4.0,
            foodTime = 30,
            ingredient = "Bột, tôm",
            calories = 232,
            restaurantId = "banh_mi_chao_gia_huy",
            categoriesId = listOf("banh-vn", "banh-mi-chao"),
            variations = setDefaultOption3(),
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
            categoriesId = listOf("pizza"),
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
            categoriesId = listOf("pizza"),
            variations = setDefaultOption2()
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
            categoriesId = listOf("pizza"),
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
            categoriesId = listOf("pizza"),
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
            categoriesId = listOf("pizza"),
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
            categoriesId = listOf("pizza"),
            variations = emptyList(),
        )
    }
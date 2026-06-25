package com.example.foodapp.utils

import com.example.foodapp.domain.model.RatingCount
import com.example.foodapp.domain.model.Restaurant
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object RestaurantsSeeder {
    suspend fun seedRestaurant() {
        val db = FirebaseFirestore.getInstance()
        val restaurants = listOf(
            Restaurant(
                restaurantId = "phuclong",
                restaurantName = "Phúc Long Coffee & Tea",
                description = "Thương hiệu trà và cà phê nổi tiếng Việt Nam với hương vị đậm đà.",
                imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103446/logo_phuclong_qaa02v.png",
                phoneNumber = "0901234567",
                address = "123 Nguyễn Trãi, Quận 1, TP.HCM",
                rating = 4.8,
                email = "support@phuclong.vn",
                totalReview = 5120,
                deliveryFee = 30,
                minOrderAmount = 50000.0,
                estimatedDeliveryTime = 25,
                isOpen = true,
                ratingCount = RatingCount(),
                categories = listOf("ca-phe", "tra", "nuoc-ep")
            ),
            Restaurant(
                restaurantId = "pizzahurt",
                restaurantName = "Pizza Hurt",
                description = "Pizza theo phong cách Mỹ với lớp phô mai đầy đặn và hương vị khó cưỡng.",
                imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103438/logo_pizzahurt_gwg20n.png",
                phoneNumber = "0987223344",
                address = "45 Lê Văn Sỹ, Phú Nhuận, TP.HCM",
                rating = 4.5,
                email = "contact@pizzahurt.com",
                totalReview = 4321,
                deliveryFee = 15000,
                minOrderAmount = 70000.0,
                ratingCount = RatingCount(),
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("pizza", "ga-chien", "nuoc")
            ),
//            Restaurant(
//                restaurantId = "domino",
//                restaurantName = "Domino's Pizza",
//                description = "Chuỗi nhà hàng pizza nổi tiếng thế giới, giao hàng nhanh, chất lượng đảm bảo.",
//                imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103438/logo_domino_l1ney6.png",
//                phoneNumber = "0977554433",
//                address = "80 Trần Hưng Đạo, Quận 5, TP.HCM",
//                rating = 4.6,
//                email = "support@dominos.vn",
//                totalReview = 3900,
//                deliveryFee = 10000,
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
//                rating = 4.4,
//                email = "hello@mcdonalds.vn",
//                totalReview = 2890,
//                deliveryFee = 30,
//                minOrderAmount = 60000.0,
//                estimatedDeliveryTime = 20,
//                isOpen = true,
//                categories = listOf("banh-hamburger", "ga-chien", "nuoc-ngot")
//            ),

            Restaurant(
                restaurantId = "bot_loc1",
                restaurantName = "Bánh bột lọc Dì sáu",
                description = "Bánh",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031198/b%C3%A1nh_b%E1%BB%99t_l%E1%BB%99c_juhw9i.jpg",
                imageUrl = "",
                phoneNumber = "0966778899",
                address = "2001 Điện Biên Phủ, Bình Chánh, TP.HCM",
                rating = 3.4,
                email = "hello@mcdonalds.vn",
                totalReview = 90,
                deliveryFee = 30,
                minOrderAmount = 60000.0,
                ratingCount = RatingCount(),

                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("banh-vn", "banh-bot-loc","tat-ca")
            ),

            Restaurant(
                restaurantId = "banh_da_cua177",
                restaurantName = "Bánh Đa 177",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031006/b%C3%A1nh_canh_cua_2_ybudbq.jpg",
                imageUrl = "",
                phoneNumber = "09667782399",
                address = "212 Trung Trực, Tân Phú, TP.HCM",
                rating = 3.8,
                ratingCount = RatingCount(),

                email = "hello@banhda177.vn",
                totalReview = 30,
                deliveryFee = 30,
                minOrderAmount = 60000.0,
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("banh-vn", "banh-da","tat-ca")
            ),

            Restaurant(
                restaurantId = "banh_mi_247",
                restaurantName = "Bánh Mì 247",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031050/b%C3%A1nh_m%C3%AC_1_rjw4op.jpg",
                imageUrl = "",
                phoneNumber = "09347782399",
                address = "212-23c, Tân Bình, TP.HCM",
                rating = 3.7,
                email = "hello@banhmi247.vn",
                totalReview = 33,
                deliveryFee = 30,
                ratingCount = RatingCount(),

                minOrderAmount = 60000.0,
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("banh-vn", "banh-mi","tat-ca")
            ),

            Restaurant(
                restaurantId = "banh_mi_chao_gia_huy",
                restaurantName = "Bánh Mì Chảo Gia Huy",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031087/0bb5026b8a6dbfcd68f11746e4486e94_npcy9a.jpg",
                imageUrl = "",
                phoneNumber = "09123782399",
                address = "223-32c, Tân Bình, TP.HCM",
                rating = 3.7,
                ratingCount = RatingCount(),

                email = "hello@banhmichaogiahuy.vn",
                totalReview = 13,
                deliveryFee = 30,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("banh-vn", "banh-mi-chao","tat-ca")
            ),
            Restaurant(
                restaurantId = "banh_que_ky_duyen1",
                restaurantName = "Bánh Hương Quê Kỳ Duyên",
                description = "",
                ratingCount = RatingCount(),

                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031265/b%C3%A1nh_n%E1%BA%ADm_r1m3xy.jpg",
                imageUrl = "",
                phoneNumber = "09123782399",
                address = "2/23 Thái Văn, Tân Bình, TP.HCM",
                rating = 4.7,
                email = "hello@banh_que_ky_duyen.vn",
                totalReview = 13,
                deliveryFee = 30,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("banh-vn","tat-ca")
            ),

            Restaurant(
                restaurantId = "banh_xeo_hai_tu1",
                restaurantName = "Bánh Xèo Hải Tú",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031136/b%C3%A1nh_x%C3%A8o_1_qixk2a.jpg",
                imageUrl = "",
                phoneNumber = "09223782399",
                address = "4c/2 Thái Văn, Tân Bình, TP.HCM",
                rating = 4.7,
                ratingCount = RatingCount(),

                email = "hello@banh_que_ky_duyen.vn",
                totalReview = 13,
                deliveryFee = 30,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("banh-vn", "banh-xeo","tat-ca")
            ),

            Restaurant(
                restaurantId = "bun_cha_ha_noi1",
                restaurantName = "Bún Chả Hà Nội 1",
                description = "",
                ratingCount = RatingCount(),

                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779030817/b%C3%BAn_ch%E1%BA%A3_1_s3fixp.jpg",
                imageUrl = "",
                phoneNumber = "09222782399",
                address = "4c/2 Thái Văn, Phú Nhuận, TP.HCM",
                rating = 4.7,
                email = "hello@bunchahanoi1.vn",
                totalReview = 13,
                deliveryFee = 30,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("bun", "bun-cha","tat-ca")
            ),

            Restaurant(
                restaurantId = "bun_dau1",
                restaurantName = "Bún Đậu Mắm Tôm Cô Ba",
                description = "",
                ratingCount = RatingCount(),

                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031235/b%C3%BAn_%C4%91%E1%BA%ADu_2_qvsnpv.jpg",
                imageUrl = "",
                phoneNumber = "0938123456",
                address = "25 Nguyễn Gia Trí, Bình Thạnh, TP.HCM",
                rating = 4.6,
                email = "hello@bundau.vn",
                totalReview = 21,
                deliveryFee = 25,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 25,
                isOpen = true,
                categories = listOf("bun", "bun-dau","tat-ca")
            ),

            Restaurant(
                restaurantId = "bun_moc1",
                restaurantName = "Bún Mọc Gia Truyền",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031304/b%C3%BAn_m%E1%BB%8Dc_2_lfngoy.jpg",
                imageUrl = "",
                ratingCount = RatingCount(),

                phoneNumber = "0944556677",
                address = "88 Lê Văn Sỹ, Phú Nhuận, TP.HCM",
                rating = 4.5,
                email = "hello@bunmoc.vn",
                totalReview = 18,
                deliveryFee = 20,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("bun", "bun-moc","tat-ca")
            ),

            Restaurant(
                restaurantId = "bun_rieu1",
                restaurantName = "Bún Riêu Cua Đồng",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779030921/cd5d67116c016954f776bd52ad272c72_kiudbs.jpg",
                imageUrl = "",
                ratingCount = RatingCount(),

                phoneNumber = "0977112233",
                address = "12 Trường Sa, Quận 3, TP.HCM",
                rating = 4.8,
                email = "hello@bunrieu.vn",
                totalReview = 26,
                deliveryFee = 22,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 28,
                isOpen = true,
                categories = listOf("bun", "bun-rieu","tat-ca")
            ),
            Restaurant(
                restaurantId = "bun_thit_nuong1",
                restaurantName = "Bún Thịt Nướng Sài Gòn",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779030956/b%C3%BAn_th%E1%BB%8Bt_n%C6%B0%E1%BB%9Bng_1_cptyry.jpg",
                imageUrl = "",
                ratingCount = RatingCount(),

                phoneNumber = "0911223344",
                address = "55 Nguyễn Thượng Hiền, Bình Thạnh, TP.HCM",
                rating = 4.7,
                email = "hello@bunthitnuong.vn",
                totalReview = 30,
                deliveryFee = 25,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 27,
                isOpen = true,
                categories = listOf("bun", "bun-thit-nuong","tat-ca")
            ),

            Restaurant(
                restaurantId = "ca_kho1",
                restaurantName = "Cá Kho Tộ Miền Tây",
                description = "",
                ratingCount = RatingCount(),

                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031413/c%C3%A1_kho_2_osgi4g.jpg",
                imageUrl = "",
                phoneNumber = "0988776655",
                address = "100 Điện Biên Phủ, Quận 1, TP.HCM",
                rating = 4.6,
                email = "hello@cakho.vn",
                totalReview = 15,
                deliveryFee = 20,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 35,
                isOpen = true,
                categories = listOf("com", "ca-kho","tat-ca")
            ),

            Restaurant(
                restaurantId = "ca_phe1",
                restaurantName = "Cà Phê Phin Việt",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031463/c%C3%A0_ph%C3%AA_2_ekanzr.jpg",
                imageUrl = "",
                ratingCount = RatingCount(),

                phoneNumber = "0909888777",
                address = "9 Võ Văn Tần, Quận 3, TP.HCM",
                rating = 4.9,
                email = "hello@caphe.vn",
                totalReview = 44,
                deliveryFee = 15,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 15,
                isOpen = true,
                categories = listOf("nuoc", "ca-phe","tat-ca")
            ),
            Restaurant(
                restaurantId = "nuoc_giai_khat1",
                restaurantName = "Nước Giải Khát 247",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031623/2591d05d659887f7b67433658eb48809_vauxlk.jpg",
                imageUrl = "",
                ratingCount = RatingCount(),

                phoneNumber = "0977665544",
                address = "200 Nguyễn Thị Minh Khai, Quận 3, TP.HCM",
                rating = 4.5,
                email = "hello@drink247.vn",
                totalReview = 28,
                deliveryFee = 12,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 15,
                isOpen = true,
                categories = listOf("drink", "nuoc-giai-khat","tat-ca")
            ),

            Restaurant(
                restaurantId = "pho_ha_noi1",
                restaurantName = "Phở Hà Nội Gia Truyền",
                description = "",
                ratingCount = RatingCount(),

                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031647/ph%E1%BB%9F_h%C3%A0_n%E1%BB%99i_ec2fja.jpg",
                imageUrl = "",
                phoneNumber = "0911002200",
                address = "18 Hai Bà Trưng, Quận 1, TP.HCM",
                rating = 4.9,
                email = "hello@phohanoi.vn",
                totalReview = 42,
                deliveryFee = 25,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("pho", "pho-ha-noi","tat-ca")
            ),


            Restaurant(
                restaurantId = "com_tam1",
                restaurantName = "Cơm Tấm Sườn Bì Chả",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031516/c%C6%A1m_t%E1%BA%A5m_ap9fii.jpg",
                imageUrl = "",
                ratingCount = RatingCount(),

                phoneNumber = "0933445566",
                address = "120 Nguyễn Tri Phương, Quận 10, TP.HCM",
                rating = 4.8,
                email = "hello@comtam.vn",
                totalReview = 34,
                deliveryFee = 25,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 30,
                isOpen = true,
                categories = listOf("com", "com-tam","tat-ca")
            ),

            Restaurant(
                restaurantId = "com_chien1",
                restaurantName = "Cơm Chiên Dương Châu",
                description = "",
                ratingCount = RatingCount(),

                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031540/c%C6%A1m_chi%C3%AAn_1_poflns.jpg",
                imageUrl = "",
                phoneNumber = "0911778899",
                address = "40 Lý Thường Kiệt, Tân Bình, TP.HCM",
                rating = 4.5,
                email = "hello@comchien.vn",
                totalReview = 16,
                deliveryFee = 20,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 25,
                isOpen = true,
                categories = listOf("com", "com-chien","tat-ca")
            ),
            Restaurant(
                restaurantId = "goi_cuon1",
                restaurantName = "Gỏi Cuốn Tôm Thịt",
                description = "",
                ratingCount = RatingCount(),

                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031578/g%E1%BB%8Fi_cu%E1%BB%91n_2_diw4sq.jpg",
                imageUrl = "",
                phoneNumber = "0988112233",
                address = "75 Phạm Văn Đồng, Gò Vấp, TP.HCM",
                rating = 4.6,
                email = "hello@goicuon.vn",
                totalReview = 19,
                deliveryFee = 18,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 20,
                isOpen = true,
                categories = listOf("goi", "goi-cuon","tat-ca")
            ),
            Restaurant(
                restaurantId = "mi_quang1",
                restaurantName = "Mì Quảng Đà Nẵng",
                description = "",
                ratingCount = RatingCount(),

                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031598/m%C3%AC_qu%E1%BA%A3ng_1_vdlmtn.jpg",
                imageUrl = "",
                phoneNumber = "0903445566",
                address = "15 Nguyễn Huệ, Quận 1, TP.HCM",
                rating = 4.7,
                email = "hello@miquang.vn",
                totalReview = 24,
                deliveryFee = 25,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 28,
                isOpen = true,
                categories = listOf("mi", "mi-quang","tat-ca")
            ),
            Restaurant(
                restaurantId = "tra_me1",
                restaurantName = "Trà Me Giải Nhiệt",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031685/tr%C3%A0_me_2_yfkijp.jpg",
                imageUrl = "",
                phoneNumber = "0933881122",
                address = "60 Hoàng Văn Thụ, Phú Nhuận, TP.HCM",
                rating = 4.6,
                email = "hello@trame.vn",
                totalReview = 15,
                ratingCount = RatingCount(),

                deliveryFee = 10,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 12,
                isOpen = true,
                categories = listOf("nuoc", "tra-me","tat-ca")
            ),
            Restaurant(
                restaurantId = "tra_nhiet_doi1",
                restaurantName = "Trà Nhiệt Đới Fresh",
                description = "",
                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031716/tr%C3%A0_nhi%E1%BB%87t_%C4%91%E1%BB%9Bi1_i1cfxa.jpg",
                imageUrl = "",
                phoneNumber = "0912334455",
                address = "45 Phan Xích Long, Phú Nhuận, TP.HCM",
                rating = 4.7,
                ratingCount = RatingCount(),

                email = "hello@tranhietdoi.vn",
                totalReview = 27,
                deliveryFee = 12,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 12,
                isOpen = true,
                categories = listOf("nuoc", "tra-nhiet-doi","tat-ca")
            ),

            Restaurant(
                restaurantId = "tra_sua1",
                restaurantName = "Trà Sữa House",
                description = "",
                ratingCount = RatingCount(),

                coverImage = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1779031761/61228e24f799ef1642ff824f17b85fb1_letczw.jpg",
                imageUrl = "",
                phoneNumber = "0909556677",
                address = "88 Nguyễn Gia Trí, Bình Thạnh, TP.HCM",
                rating = 4.8,
                email = "hello@trasua.vn",
                totalReview = 39,
                deliveryFee = 15,
                minOrderAmount = 3000.0,
                estimatedDeliveryTime = 15,
                isOpen = true,
                categories = listOf("nuoc", "tra-sua","tat-ca")
            )


            )
        restaurants.forEach { restaurant ->
            val updateRes = restaurant.copy(
                updatedAt = null,
                createdAt = null
            )
            db.collection("restaurants")
                .document(updateRes.restaurantId)
                .set(updateRes)
                .await()
        }
    }
}
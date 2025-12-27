//package com.example.foodapp.ui.home
//
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.viewpager2.widget.CompositePageTransformer
//import androidx.viewpager2.widget.MarginPageTransformer
//import androidx.viewpager2.widget.ViewPager2
//import com.example.foodapp.R
//import com.example.foodapp.data.model.category.FoodCategory
//import kotlin.math.abs
//
//class Test3 : AppCompatActivity() {
//
//    private lateinit var handler: Handler
//    private lateinit var imageList: List<String>
//    private lateinit var viewPager2: ViewPager2
//    private lateinit var adapter: Test3Adapter
//    private var runnable = Runnable {
//        val nextItem = viewPager2.currentItem + 1
//        viewPager2.setCurrentItem(nextItem, true)
//        viewPager2.currentItem = nextItem
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_test3)
//
//        handler = Handler(Looper.getMainLooper())
//        setupWindowInsets()
//        setupImageList()
//        setupTransformer()
//        setupAutoScroll()
//        setupIconResCategory()
//        setupFoodImg()
//    }
//
//    private fun setupAutoScroll() {
//        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                handler.removeCallbacks(runnable)
//                handler.postDelayed(runnable, 3000)
//            }
//        })
//        handler.postDelayed(runnable, 3000)
//    }
//
//    private fun setupTransformer() {
//        val transformer = CompositePageTransformer()
//        transformer.addTransformer(MarginPageTransformer(20))
//        transformer.addTransformer { page, position ->
//            val r = 1 - abs(position)
//            page.scaleY = 0.85f + r * 0.15f
//        }
//        viewPager2.setPageTransformer(transformer)
//    }
//
//    private fun setupImageList() {
//        viewPager2 = findViewById(R.id.viewPager2)
//        imageList = listOf(
//            "https://picsum.photos/id/237/400/300",
//            "https://picsum.photos/id/238/400/300",
//            "https://picsum.photos/id/239/400/300",
//            "https://picsum.photos/id/240/400/300",
//        )
//        adapter = Test3Adapter(imageList)
//        viewPager2.adapter = adapter
//
//        // ✅ Tối ưu ViewPager2
//        viewPager2.offscreenPageLimit = 1 // Chỉ 1 page
//        viewPager2.clipChildren = false
//        viewPager2.clipToPadding = false
//
//        // ✅ Bỏ infinite scroll để giảm memory
//        val startPosition = Int.MAX_VALUE / 2
//        viewPager2.setCurrentItem(startPosition - (startPosition % imageList.size), false)    }
//
//    private fun setupIconResCategory() {
//        val rvFoodCategory = findViewById<RecyclerView>(R.id.rvFoodAdapter)
//        val iconList: List<FoodCategory> = listOf(
//            FoodCategory("pizza", "1", R.drawable.ic_fb, true),
//            FoodCategory("pizza", "2", R.drawable.ic_fb, true),
//            FoodCategory("pizza", "3", R.drawable.ic_fb, true),
//        )
//
//        // ✅ Tối ưu RecyclerView
//        rvFoodCategory.apply {
//            setHasFixedSize(true)
//            itemAnimator = null // Tắt animation
//            layoutManager = LinearLayoutManager(this@Test3, LinearLayoutManager.HORIZONTAL, false)
//            adapter = FoodCategoryAdapter(iconList)
//        }
//    }
//
//    private fun setupFoodImg() {
//        val foodImg = findViewById<RecyclerView>(R.id.rvFoodImg)
//        val foodlstImg = listOf<FoodCategory>(
//            FoodCategory("pizza", "Pizza 01", R.drawable.img_pizza, false),
//            FoodCategory("pizza", "Pizza 02", R.drawable.img_pizza, false),
//            FoodCategory("pizza", "Pizza 03", R.drawable.img_pizza, false),
//            FoodCategory("pizza", "Pizza 04", R.drawable.img_pizza, false),
//        )
//
//        // ✅ Tối ưu RecyclerView
//        foodImg.apply {
//            setHasFixedSize(true)
//            itemAnimator = null
//            layoutManager = LinearLayoutManager(this@Test3, LinearLayoutManager.VERTICAL, false)
//            adapter = FoodImg(foodlstImg)
//        }
//    }
//
//    private fun setupWindowInsets() {
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.test3)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        handler.removeCallbacks(runnable) // ✅ Đúng cách
//    }
//
//    override fun onResume() {
//        super.onResume()
//        handler.postDelayed(runnable, 3000) // ✅ Đúng cách
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        // ✅ Clean up resources
//        handler.removeCallbacks(runnable)
//        viewPager2.adapter = null
//        findViewById<RecyclerView>(R.id.rvFoodAdapter).adapter = null
//        findViewById<RecyclerView>(R.id.rvFoodImg).adapter = null
//    }
//}
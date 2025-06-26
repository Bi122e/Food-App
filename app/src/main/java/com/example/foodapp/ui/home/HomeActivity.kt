package com.example.foodapp.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Runnable

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var imageList: List<String>
    private lateinit var adapter: ImageSliderAdapter
    private lateinit var handler: Handler
    private val dotViews = mutableListOf<ImageView>()

    private val autoScrollRunnable = Runnable {
        val nextItem = viewPager2.currentItem + 1
        viewPager2.setCurrentItem(nextItem, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        setupSlider()
        setupDots(imageList.size)
        setupIndicatorListener()
        setupAutoScroll()
        setupNavBottomBar()
    }

    private fun setupSlider() {
        viewPager2 = binding.viewPager2
        imageList = listOf(
            "https://picsum.photos/id/237/400/300",
            "https://picsum.photos/id/238/400/300",
            "https://picsum.photos/id/239/400/300",
            "https://picsum.photos/id/240/400/300"
        )

        adapter = ImageSliderAdapter(imageList)
        viewPager2.adapter = adapter

        val startPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % imageList.size)
        viewPager2.setCurrentItem(startPosition, false)
    }

    private fun setupDots(count: Int) {
        binding.dotContainer.removeAllViews()
        dotViews.clear()

        for (i in 0 until count) {
            val dot = ImageView(this).apply {
                setImageResource(R.drawable.bg_dot_inactive)
                val params = LinearLayout.LayoutParams(20, 20)
                params.setMargins(6, 0, 6, 0)
                layoutParams = params
            }
            binding.dotContainer.addView(dot)
            dotViews.add(dot)
        }

        if (dotViews.isNotEmpty()) {
            dotViews[0].setImageResource(R.drawable.bg_dot_active)
        }
    }

    private fun setupIndicatorListener() {
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val index = position % imageList.size
                dotViews.forEachIndexed { i, imageView ->
                    imageView.setImageResource(
                        if (i == index) R.drawable.bg_dot_active else R.drawable.bg_dot_inactive
                    )
                }

                handler.removeCallbacks(autoScrollRunnable)
                handler.postDelayed(autoScrollRunnable, 4000)
            }
        })
    }

    private fun setupAutoScroll() {
        handler.postDelayed(autoScrollRunnable, 4000)
    }

    private fun setupNavBottomBar() {
        binding.bottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_explore -> {
                    Toast.makeText(this, "Explore clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_search -> {
                    Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this, "Shopping clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_person -> {
                    Toast.makeText(this, "Person clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}


//    private fun setView() {
//        val userId = firebaseAuth.currentUser?.uid
//        if (userId != null) {
//            firebaseFirestore.collection("users").document(userId)
//                .get()
//                .addOnSuccessListener { document->
//                    if (document.exists()) {
//                        val userData = document.data
//                        Log.d("FireStore", "user data $userData")
//
//                        val name = document.getString("name")
//                        val email = document.getString("email")
//                        val uid = document.getString("uid")
//
//                    } else {
//                        Log.d("Firestore", "User document not found")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d("Firestore", "Error $exception")
//                }
//        }
//    }


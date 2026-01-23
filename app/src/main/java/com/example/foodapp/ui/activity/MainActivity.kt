package com.example.foodapp.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setContent {
            Greeting("compose")
        }

    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name")
    }

//        CategorySeeder.seedCategories() //thêm các category
////        RestaurantsSeeder.seedRestaurant()// thêm các nhà hàng mẫu
//        val text = "Ban co tai khoan chua ? dang nhap"
//        val spannableString = SpannableString(text)
//        val clickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                val intent = Intent(this@MainActivity, LoginActivity::class.java)
//                startActivity(intent)
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//                ds.color = Color.BLACK
//            }
//        }
//        val start = text.indexOf("dang nhap")
//        val end = start + "dang nhap".length
//        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//        binding.txtDangNhap.text = spannableString
//        binding.txtDangNhap.movementMethod = LinkMovementMethod.getInstance()
//        binding.txtDangNhap.highlightColor = Color.TRANSPARENT
//
//        binding.button2.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//    }

}
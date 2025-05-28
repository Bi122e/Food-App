package com.example.foodapp

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
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()
        backToRegister()
        binding.btnLogin.setOnClickListener {
            Login()
        }
    }

    private fun Login() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString()

        var checkState = true

        binding.edtEmailLayout.error = null
        binding.edtPasswordLayout.error = null

        if (email.isEmpty()) {
            binding.edtEmailLayout.error = "Không được dể trống email"
            checkState = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmailLayout.error = "Sai định dạng email"
        }
        if (password.isEmpty()) {
            binding.edtPasswordLayout.error = "Không được để trống password"
        }

        if (checkState) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this@LoginActivity, SplashActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@LoginActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("Auth", it.exception?.message.toString())
                    }
                }
        }
    }

    private fun backToRegister() {
        val text = "Bạn chưa có tài khoản ? Đăng ký"
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }
        val start = text.indexOf("Đăng ký")
        val end = start + "Đăng ký".length
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        binding.txtDangNhap.text = spannableString
        binding.txtDangNhap.movementMethod = LinkMovementMethod.getInstance()
        binding.txtDangNhap.highlightColor = Color.TRANSPARENT

    }
}
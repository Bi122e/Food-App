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
import android.util.Patterns
import android.view.View

import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.R
import com.example.foodapp.core.UiState
import com.example.foodapp.databinding.ActivityRegisterBinding
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGoogle()
        observeAuthState()

//        binding.btnRegister.setOnClickListener { registerByEmail() }
        setUpRegisterButton()
        backToLogin()

        binding.imgbtnGb.setOnClickListener { googleLauncher.launch(googleSignInClient.signInIntent) }

//        binding.txtDangNhap.setOnClickListener {
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
    }

//    private fun registerByEmail() {
//        val name = binding.edtUserName.text.toString()
//        val email = binding.edtEmail.text.toString()
//        val password = binding.edtPassword.text.toString()
//
//        if (name.isBlank() || email.isBlank() || password.length < 6) {
//            toast("Dữ liệu không hợp lệ")
//            return
//        }
//        viewModel.register(name, email, password)
//    }

    private fun setupGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        viewModel.loginWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        toast("Google Sign-In failed")
                    }
                }
            }
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    UiState.Idle -> Unit

                    UiState.Loading -> {
                        // show loading
                    }

                    is UiState.Success -> {
                        Toast.makeText(this@RegisterActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoadingActivity::class.java))
                        finish()
                    }

                    is UiState.Error -> {
                        Toast.makeText(this@RegisterActivity, state.message, Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Empty -> Unit
                }
            }
        }

    }
    private fun setUpRegisterButton() {
        binding.btnRegister.setOnClickListener {

            val name = binding.edtUserName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()

            if (!validateRegister(name, email, password, confirmPassword)) return@setOnClickListener

            // ✅ GỌI VIEWMODEL – KHÔNG FIREBASE
            viewModel.register(name, email, password)
        }
    }
    private fun validateRegister(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {

        binding.edtUserNameLayout.error = null
        binding.edtEmailLayout.error = null
        binding.edtPasswordLayout.error = null
        binding.edtConfirmPasswordLayout.error = null

        var valid = true

        if (name.isBlank()) {
            binding.edtUserNameLayout.error = "Không được để trống"
            valid = false
        }

        if (email.isBlank()) {
            binding.edtEmailLayout.error = "Không được để trống"
            valid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmailLayout.error = "Sai định dạng email"
            valid = false
        }

        if (password.length < 6) {
            binding.edtPasswordLayout.error = "Mật khẩu tối thiểu 6 ký tự"
            valid = false
        }

        if (password != confirmPassword) {
            binding.edtConfirmPasswordLayout.error = "Mật khẩu không khớp"
            valid = false
        }

        return valid
    }




    private fun backToLogin() {
        val text = "Ban co tai khoan ? Dang nhap"
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.BLACK
            }
        }

        val start = text.indexOf("Dang nhap")
        val end = start + "Dang nhap".length
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE )
        spannableString.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE )

        binding.txtDangKy.text = spannableString
        binding.txtDangKy.movementMethod = LinkMovementMethod.getInstance()
        binding.txtDangKy.highlightColor = Color.TRANSPARENT
    }
    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
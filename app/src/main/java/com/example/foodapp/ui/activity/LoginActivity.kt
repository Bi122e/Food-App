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
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.R
import com.example.foodapp.core.UiState
import com.example.foodapp.databinding.ActivityLoginBinding
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    private lateinit var googleClient: GoogleSignInClient
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsets()
        setupGoogle()
        setupListeners()
        obverseAuthState()
        backToRegister()
    }

    //setup
    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener { loginByEmail() }
        binding.imgbtnGb.setOnClickListener {
            googleLauncher.launch(googleClient.signInIntent)
        }
    }

    //login email

    private fun loginByEmail() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString()

        if (!validateLogin(email, password)) return

        viewModel.login(email, password)
    }

    private fun validateLogin(email: String, password: String): Boolean {
        binding.edtEmailLayout.error = null
        binding.edtPasswordLayout.error = null

        var valid = true

        if (email.isBlank()) {
            binding.edtEmailLayout.error = "Khong duoc de trong email"
            valid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmailLayout.error = "Sai dinh dang email"
            valid = false
        }

        if (password.isBlank()) {
            binding.edtPasswordLayout.error = "Khong duoc de trong mat khau"
            valid = false
        }

        return valid
    }

    //google sign in
    private fun setupGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(this, gso)
        googleLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val account = GoogleSignIn
                        .getSignedInAccountFromIntent(result.data)
                        .getResult(ApiException::class.java)
                    viewModel.loginWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Toast.makeText(this, "Google sign in that bai", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obverseAuthState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Idle -> Unit
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "Dang nhap thanh cong",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(this@LoginActivity, LoadingActivity::class.java)
                        )
                        finish()
                    }

                    is UiState.Empty -> Unit
                    is UiState.Error -> {
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun backToRegister() {
        val text = "Bạn chưa có tài khoản ? Đăng ký"
        val spannable = SpannableString(text)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                startActivity(
                    Intent(this@LoginActivity, RegisterActivity::class.java)
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }

        val start = text.indexOf("Đăng ký")
        val end = start + "Đăng ký".length
        spannable.setSpan(
            clickableSpan, start, end,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )

        binding.txtDangNhap.text = spannable
        binding.txtDangNhap.movementMethod = LinkMovementMethod.getInstance()
        binding.txtDangNhap.highlightColor = Color.TRANSPARENT
    }

    //Utils


}
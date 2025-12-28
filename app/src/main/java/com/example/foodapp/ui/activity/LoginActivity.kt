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
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodapp.R
import com.example.foodapp.ui.activity.LoadingActivity
import com.example.foodapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("FirebaseUser", "SignInLauncher resultCode: ${result.resultCode}")
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Toast.makeText(this, "Google Sign-In success: ${account.email}", Toast.LENGTH_SHORT).show()
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w("LoginActivity", "Google sign in failed: " + e.statusCode, e)
                    Toast.makeText(this, "Đăng nhập thất bại: " + e.statusCode, Toast.LENGTH_SHORT).show()
                }
            } else  {
                Log.e("FirebaseUser", "Google Sign-In canceled or failed. resultCode=${result.resultCode}, data=${result.data}")
                Toast.makeText(this, "Sign in canceled or failed", Toast.LENGTH_SHORT).show()
            }
        }


        binding.imgbtnGb.setOnClickListener {
            signInWithGoogle()
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Log.d("FirebaseUser", "Logging in...")
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Log.d("FirebaseUser", "UID: ${user?.uid}, Email: ${user?.email}")
                    Toast.makeText(this, "Đăng nhập thành công: ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    val intent =
                        Intent(this@LoginActivity, LoadingActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                    Log.e("FirebaseUser", "Google Sign-In canceled or failed. resultCode=, data=")

                }
            }
    }

    private fun signInWithGoogle() {
        val signInTent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInTent)
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
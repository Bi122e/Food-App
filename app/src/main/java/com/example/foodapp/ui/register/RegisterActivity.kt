package com.example.foodapp.ui.register

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
import com.example.foodapp.data.model.user.User
import com.example.foodapp.databinding.ActivityRegisterBinding
import com.example.foodapp.ui.splash.LoadingActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var firebaseFirestore: FirebaseFirestore
    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        setUpRegisterButton()
        backToLogin()

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
                        Intent(this@RegisterActivity, LoadingActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val signInTent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInTent)
    }



    private fun setUpRegisterButton() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edtUserName.text.toString()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()

            var checkState = true
            binding.edtUserNameLayout.error = null
            binding.edtEmailLayout.error = null
            binding.edtPasswordLayout.error = null
            binding.edtConfirmPasswordLayout.error = null
            if (name.isEmpty()) {
                binding.edtUserNameLayout.error = "Khong duoc de trong"
                checkState = false
            }
            if (email.isEmpty()) {
                binding.edtEmailLayout.error = "khong duoc de trong"
                checkState = false

            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailLayout.error = "sai dinh dang email"
                checkState = false
            }
            if (password.isEmpty()) {
                binding.edtPasswordLayout.error = "khong duoc de trong"
                checkState = false
            } else if (password.length < 6) {
                binding.edtPasswordLayout.error = "Khong duoc nho hon 6 ky tu"
                checkState = false
            } else if (password != confirmPassword) {
                binding.edtPasswordLayout.error = "Mat khau khong khop"
                checkState = false
            }
            if (confirmPassword.isEmpty()) {
                binding.edtConfirmPasswordLayout.error = "khong duoc de trong"
                checkState = false
            } else if (confirmPassword.length < 6) {
                binding.edtConfirmPasswordLayout.error = "Khong duoc nho hon 6 ky tu"
                checkState = false
            } else if (password != confirmPassword) {
                binding.edtConfirmPassword.error = "Mat khau khong khop"
                checkState = false
            }

            if (checkState) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            if (user != null) {
                                val userData = User(
                                    user.uid,
                                    name,
                                    email,
                                )
                                firebaseFirestore.collection("users").document(user.uid)
                                    .set(userData)
                                    .addOnCompleteListener {
                                        Toast.makeText(this@RegisterActivity, "Đăng ký thành công và lưu user", Toast.LENGTH_SHORT).show()

                                    }
                                    .addOnFailureListener { e->
                                        Toast.makeText(this@RegisterActivity, "Đăng ký thất bại ${e.message}",
                                            Toast.LENGTH_SHORT).show()
                                    }

                            }
                            Toast.makeText(this, "thanh cong", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("firebaseAuth", it.exception?.message.toString() )
                            Log.d("LOI", it.exception?.message.toString() )
                            Toast.makeText(this, "fail roi", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

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


}
package com.example.foodapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.core.utils.GoogleSignInManager
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.ui.screen.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var googleSignInManager: GoogleSignInManager
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>
    private val authModelView: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val data = result.data
            val accountResult = googleSignInManager.getAccountFormIntent(data)

            accountResult
                .onSuccess { account ->
                    val idToken = account.idToken
                    if (idToken != null) {
                        authModelView.loginWithGoogle(idToken)
                    }
                    Log.d("Google Login", "Email = ${account.email}")
                }
                .onFailure {
                    Log.e("Google Login", "Login failed", it)
                }
        }

        setContent {

            AppNavGraph(
                authModelView,
                googleLauncher,
                googleSignInManager.googleClient
            )

        }

    }

//    @Composable
//    fun CounterScreen(viewModel: CounterViewModel = hiltViewModel()) {
//        val value by viewModel.count.collectAsStateWithLifecycle()
//
//        when (value) {
//            is UiState.Success -> {
//                val count = (value as UiState.Success).data
//                CounterContent(count, viewModel::increment, viewModel::decrement)
//            }
//
//            else -> Unit
//        }
//
//    }


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





//    LoginSocial (Button)
//    ↓
//    googleLauncher.launch()
//    ↓
//    MainActivity.onActivityResult
//    ↓
//    authViewModel.loginWithGoogle()
//    ↓
//    uiState = Loading → Success / Error
//    ↓
//    Compose tự recompose
}
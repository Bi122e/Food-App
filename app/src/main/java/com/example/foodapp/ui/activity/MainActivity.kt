package com.example.foodapp.ui.activity

import AuthStatus
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.core.Routes
import com.example.foodapp.core.utils.GoogleSignInManager
import com.example.foodapp.data.seed.FoodSeeder
import com.example.foodapp.presentation.state.AppState
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.screen.navigation.AppNavGraph
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /*
    * Context
    └── ContextWrapper
      └── Activity
           └── ComponentActivity
                └── FragmentActivity
                *               └── AppCompatActivity
    * app compat ke thua may cai o tren nen chua nhieu tinh nang nhat
    * */
    @Inject
    lateinit var googleSignInManager: GoogleSignInManager
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        lifecycleScope.launch {
            FoodSeeder.seedIfNeeded(this@MainActivity)
        }

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            authViewModel.appState == AuthStatus.Loading
        }

        googleLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val data = result.data
            val accountResult = googleSignInManager.getAccountFormIntent(data)

            accountResult
                .onSuccess { account ->
                    val idToken = account.idToken
                    if (idToken != null) {
                        authViewModel.loginWithGoogle(idToken)
                    }
                    Log.d("Google Login", "Email = ${account.email}")
                }
                .onFailure {
                    Log.e("Google Login", "Login failed", it)
                }
        }

        setContent {


//            FirebaseMessaging.getInstance()
//                .token
//                .addOnSuccessListener {
//                    Log.d("FCM_TOKEN", it)
//                }
            FirebaseMessaging.getInstance()
                .token
                .addOnFailureListener {
                    Log.e("FCM_TOKEN", "failed", it)
                }
                .addOnSuccessListener { token ->
                    Log.d("FCM_TOKEN", token )
                }
            Log.d(
                "NOTI_PERMISSION",
                NotificationManagerCompat
                    .from(this)
                    .areNotificationsEnabled()
                    .toString()
            )

            /* logic doan nay kt de quyet dinh start o man nao
            ,luồng sẽ gọi current id và getuser để kt xem có tồn tại chưa,
            nếu chưa appstate = guess, và ngược lại result api là user được truyền state
             */
            val appState by authViewModel.appState.collectAsState()
//            val uiState by authViewModel.uiState.collectAsState()
            val context = LocalContext.current


            // set giá trị màn hình để hiển thị màn hình nào
            var startDestination by remember { mutableStateOf(Routes.SPLASH) }
            // khi trạng thái gues log/ dc chạy rồi mới cần chuyển sang true
            var isDestinationSet by remember { mutableStateOf(false) }

            Log.d("MainActivity", "Current AppState: $appState")

            if (!isDestinationSet) {
                when (appState) {
                    is AppState.Loading -> {
                        Log.d("MainActivity", "AppState is Loading, waiting...")
                    }

                    is AppState.Guest -> {
                        startDestination = Routes.LOGIN
                        isDestinationSet = true
                        Log.d("MainActivity", "Decided StartDestination: ${Routes.LOGIN} (Guest)")
                        android.widget.Toast.makeText(
                            context,
                            "Navigating to Login",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }

                    is AppState.LoggedIn -> {
//                        val route = state.user.role.toRootRoute()
                        //hiện tại flow splash ko được chạy, vì gần như gọi dữ liệu từ firebase
                        startDestination = Routes.HOME
                        isDestinationSet = true
                    }

                    is AppState.NeedCompleteProfile -> {
                        startDestination = Routes.COMPLETE_PROFILE
                        isDestinationSet = true
                    }
                    else -> {}
                }
            }

            if (isDestinationSet) {
                Log.d(
                    "MainActivity",
                    "Rendering AppNavGraph with startDestination: $startDestination"
                )
                AppNavGraph(
                    startDestination = startDestination, //sau khi phân loại ở trên thì chứa route cần gửi
                    appState = appState,
                    onLoginClick = authViewModel::login,
                    onRegisterClick = authViewModel::register,
                    onGoogleLogin = {
                        Log.d("MainActivity", "Google login clicked")
                        googleLauncher.launch(googleSignInManager.googleClient.signInIntent)
                    },
                    onLogout = authViewModel::logout,
                    onResetState = authViewModel::resetState,
                    onLoggedIn = { updateUser ->
                        authViewModel.setLoggedIn(updateUser)
                    }

                )
            }
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "order_channel",
                "order update",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}

package com.example.foodapp.ui.activity

 import android.content.Intent
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.foodapp.core.utils.GoogleSignInManager
import com.example.foodapp.presentation.state.AppState
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.ui.screen.navigation.AppNavGraph
import com.example.foodapp.core.Routes
import com.example.foodapp.core.toRootRoute
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var googleSignInManager: GoogleSignInManager
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            authViewModel.authStatus.value == AuthStatus.Loading
        }
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
                        authViewModel.loginWithGoogle(idToken)
                    }
                    Log.d("Google Login", "Email = ${account.email}")
                }
                .onFailure {
                    Log.e("Google Login", "Login failed", it)
                }
        }

        setContent {
            val appState by authViewModel.appState.collectAsState()
            val uiState by authViewModel.uiState.collectAsState()
            val context = LocalContext.current
            
            // startDestination calculated ONCE when transition from Loading happens
            var startDestination by remember { mutableStateOf(Routes.Splash) }
            var isDestinationSet by remember { mutableStateOf(false) }

            Log.d("MainActivity", "Current AppState: $appState")

            if (!isDestinationSet) {
                when (val state = appState) {
                    is AppState.Loading -> { 
                        Log.d("MainActivity", "AppState is Loading, waiting...")
                    }
                    is AppState.Guest -> {
                        startDestination = Routes.Login
                        isDestinationSet = true
                        Log.d("MainActivity", "Decided StartDestination: ${Routes.Login} (Guest)")
                        android.widget.Toast.makeText(context, "Navigating to Login", android.widget.Toast.LENGTH_SHORT).show()
                    }
                    is AppState.LoggedIn -> {
                        val route = state.user.role.toRootRoute()
                        startDestination = route
                        isDestinationSet = true
                        Log.d("MainActivity", "Decided StartDestination: $route (LoggedIn as ${state.user.role})")
                        android.widget.Toast.makeText(context, "Welcome! Role: ${state.user.role}", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (isDestinationSet || appState !is AppState.Loading) {
                Log.d("MainActivity", "Rendering AppNavGraph with startDestination: $startDestination")
                AppNavGraph(
                    startDestination = startDestination,
                    appState = appState,
                    uiState = uiState,
                    onLoginClick = authViewModel::login,
                    onRegisterClick = authViewModel::register,
                    onGoogleLogin = { 
                        Log.d("MainActivity", "Google login clicked")
                        googleLauncher.launch(googleSignInManager.googleClient.signInIntent) 
                    },
                    onLogout = authViewModel::logout,
                    onResetState = authViewModel::resetState
                )
            }
        }
    }
}
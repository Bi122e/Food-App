package com.example.foodapp.ui.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

//@Composable
//fun SplashScreen(
//    onNavigate: (String) -> Unit,
//    viewModel: AuthViewModel = hiltViewModel()
//) {
//    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("AnimationFood1.json"))
//    val progress by animateLottieCompositionAsState(composition)
//    val authStatus by viewModel.authStatus.collectAsStateWithLifecycle()
//    // Theo dõi trạng thái để chuyển màn hinh`
//    LaunchedEffect(authStatus) {
//        when (authStatus) {
//            is AuthStatus.Authenticated -> onNavigate("home")
//            is AuthStatus.Unauthenticated -> onNavigate("login")
//            else -> Unit
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        LottieAnimation(
//            composition = composition,
//            progress = { progress },
//            modifier = Modifier.size(250.dp)
//        )
//    }}
@Composable
fun SplashScreen() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("AnimationFood1.json")
    )
    val progress by animateLottieCompositionAsState(composition)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(250.dp)
        )
    }
}

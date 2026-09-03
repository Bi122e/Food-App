package com.example.foodapp.ui.screen.login


import android.annotation.SuppressLint
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coloredShadow
import com.example.foodapp.R
import com.example.foodapp.core.AuthResult
import com.example.foodapp.core.UiState
import com.example.foodapp.core.utils.showToast
 import com.example.foodapp.presentation.state.AppState
import com.example.foodapp.presentation.state.AuthUiState
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.Gray85
import com.example.foodapp.ui.theme.MediumGray
import com.example.foodapp.ui.theme.PrimaryBlue
import com.example.foodapp.ui.theme.White
import com.example.foodapp.ui.theme.secondBlue

@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    appState: AppState,
    onLoginClick: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onRegisterClick: () -> Unit,
) {
//    val context = LocalContext.current
//    LaunchedEffect(uiState) {
//        when (uiState) {
//            is UiState.Success -> {
//                showToast(context, "Đăng nhập thành công")
//                navController.navigate("home") {
//                    popUpTo("login") { inclusive = true  }
//                }
//            }
//            else -> Unit
//        }
//    }
    val activity = (LocalContext.current as? ComponentActivity) ?: return
    val authViewModel: AuthViewModel = hiltViewModel(activity)
    val authUiState by authViewModel.authUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(appState) {
        when (appState) {
            is AppState.LoggedIn -> {
                showToast(context, "Đăng nhập thành công")
            }

            is AppState.Error -> {
                showToast(context, appState.message)
            }

            else -> {
                Log.d("LoginScreenLogic", "...")
            }
        }
    }

    LoginContent(
        appState = appState,
        onLoginClick = onLoginClick,
        onGoogleLogin = onGoogleLogin,
        onRegisterClick = {

            onRegisterClick()
        },
        authUiState = authUiState,
    )
}

@Composable
fun LoginHeader(
    onRegisterClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .coloredShadow(
                colors = listOf(Blue1, Color.Red),
                borderRadius = 20.dp,
                blurRadius = 10.dp,
                spread = 0.1.dp
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .clickable(
                onClick = onRegisterClick
            )

    ) {
        Icon(
            imageVector = Icons.Rounded.Login,
            contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .size(48.dp)
                .padding(6.dp),
            tint = Color.Black
        )
    }



    Spacer(Modifier.height(14.dp))

    Text(
        "Đăng ký tài khoản",
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
    )

    Spacer(Modifier.height(6.dp))

    Text(
        "Đăng tài khoản để được chương trình\n ưu đãi mới nhất",
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        color = Color.Gray
    )

}


@Composable
fun LoginForm(
    appState: AppState,
    onLoginClick: (String, String) -> Unit,
    authUiState: AuthUiState,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var hasSubmitted by remember { mutableStateOf(false) }

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val errorEmail = hasSubmitted && (!isEmailValid)
    val passwordError = hasSubmitted && (password.length < 8)
    val isValidForm =
        (isEmailValid && email.isNotBlank()) && (password.length >= 8 && password.isNotBlank())

    Log.d("check_login_valid","valid = ${isValidForm.toString() }")
    Text(
        text = "Email của bạn",
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, start = 2.dp)
    )

    OutlinedTextField(
        value = email,
        isError = errorEmail,
        onValueChange = {
            hasSubmitted = false
            email = it
        },
        placeholder = { Text("Email", color = Color.Gray) },
        leadingIcon = {
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,

            focusedContainerColor = MediumGray,
            unfocusedContainerColor = MediumGray,
            disabledContainerColor = MediumGray,
            focusedIndicatorColor = Blue1.copy(0.3f),
             unfocusedIndicatorColor = Color.Transparent,

            errorContainerColor = Color.Transparent
        )
    )
    if (errorEmail || authUiState.errorLogin != null) {
        Text(
            "Email không hợp lệ",
            modifier = Modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .fillMaxWidth()
                .padding(10.dp),
            color = Color.Red,
            textAlign = TextAlign.Start,
        )
    }



    Spacer(Modifier.height(14.dp))
    Text(
        "Mật khẩu của bạn",
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, bottom = 10.dp)
    )
    OutlinedTextField(
        value = password,
        isError = passwordError,
        onValueChange = {
            hasSubmitted = false
            password = it
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        placeholder = { Text("Mật khẩu", color = Color.Gray) },
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    imageVector =
                        if (showPassword)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        },
        visualTransformation =
            if (showPassword)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,

            focusedContainerColor = MediumGray,
            unfocusedContainerColor = MediumGray,
            disabledContainerColor = MediumGray,
            focusedIndicatorColor = Blue1.copy(0.3f),
             unfocusedIndicatorColor = Color.Transparent,

            errorContainerColor = Color.Transparent

            )
    )
    if (passwordError) {
        Text(
            "Mật khẩu không hợp lệ",
            modifier = Modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .fillMaxWidth()
                .padding(10.dp),
            color = Color.Red,
            textAlign = TextAlign.Start,

            )
    }

    Spacer(Modifier.height(14.dp))

    Text(
        "Quên mật khẩu?",
        textAlign = TextAlign.End,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clickable {}
            .fillMaxWidth())

    Spacer(Modifier.height(14.dp))



    //error
    if (!authUiState.errorLogin.isNullOrEmpty() && hasSubmitted ) {
        Log.d("check_error_auth", "login ")

        Row(
            modifier = Modifier.padding(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                tint = Color.Red,
                contentDescription = null,
            )
            Spacer(Modifier.width(5.dp))

            Text(
                text = "Email hoặc mật khẩu không đúng",
                color = Color.Red,
                )
        }

    }

    Button(
        onClick = {
                 hasSubmitted = true
                if (isValidForm) {
                    onLoginClick(email, password)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),

        enabled = appState !is AppState.Loading && email.isNotEmpty() && password.isNotEmpty(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue1,
            disabledContainerColor = Blue1.copy(0.4f),

            ),
        shape = RoundedCornerShape(14.dp)

    ) {
        if (authUiState.isLoadingLogin) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                "Đăng nhập",
                color = Color.White
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LoginFormPreview(

) {
    LoginContent(
        appState = AppState.Loading,
        onLoginClick = { _, _ -> },
        onRegisterClick = {},
        onGoogleLogin = {},
        authUiState = AuthUiState(),
    )
}


//review
@Composable
fun LoginContent(
    appState: AppState,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onGoogleLogin: () -> Unit,
    authUiState: AuthUiState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.bg_sky9),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyColumn() {

            item {
                Box(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 16.dp
                         )
                        .fillMaxWidth()

                        .coloredShadow(
                            colors = listOf(Gray65),
                            1f,
                            blurRadius = 5.dp,
                            spread = 0.1.dp
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.0f to Color(0xFFc6f0fe),
                                    0.12f to Color(0xFFe4f8fc),
                                    0.25f to Color.White,
                                    1.5f to Color.White
                                )
                            ),
                            RoundedCornerShape(20.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoginHeader(onRegisterClick = onRegisterClick)
                        Spacer(Modifier.height(24.dp))
                        LoginForm(
                            appState = appState,
                            onLoginClick = onLoginClick,
                            authUiState = authUiState,
                        )
//                        LoginForm(
//                            uiState = uiState,
//                            onLoginClick = { email, password ->
//                                viewModel.login(email, password)
//                            }
//                        )
                        Spacer(Modifier.height(24.dp))

                        OrDivider("Hoặc đăng nhập bằng")

                        Spacer(Modifier.height(24.dp))

                        LoginSocial(
                            appState = appState,
                            onGoogleLogin = onGoogleLogin
                        )

                    }
                }
            }
        }




    }
}

@Composable
fun OrDivider(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Divider(Modifier.weight(1f))
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )
        Divider(Modifier.weight(1f))
    }
}

@Composable
fun LoginSocial(
    appState: AppState,
    onGoogleLogin: () -> Unit
) {
    IconButton(
        onClick = onGoogleLogin,
        enabled = appState !is AppState.Loading,
        modifier = Modifier
//            .padding(horizontal = 50.dp)
            .fillMaxWidth()
            .coloredShadow(
                colors = listOf(Color.Red, Blue0),
                borderRadius = 20.dp,
                blurRadius = 5.dp,
                offsetY = 0.5.dp
            )
            .background(Color.White, RoundedCornerShape(20.dp))
    ) {
//        .coloredShadow(
//        colors = listOf(Color.Black, Color.Red),
//        borderRadius = 20.dp,
//        blurRadius = 5.dp,
//        offsetY = 0.5.dp
//    )
        Image(
            painter = painterResource(R.drawable.ic_google2),
            modifier = Modifier
                .fillMaxWidth()
                .size(24.dp)
                 ,
            contentDescription = null,
            alignment = Alignment.Center,
        )
    }
}


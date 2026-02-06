package com.example.foodapp.ui.screen.login


import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodapp.R
import com.example.foodapp.core.AuthResult
import com.example.foodapp.core.UiState
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.ui.theme.MediumGray
import com.example.foodapp.ui.theme.PrimaryBlue
import com.example.foodapp.ui.theme.secondBlue

@Composable
fun LoginScreen(
    navController: NavController,
    uiState: UiState<AuthResult>,
    onLoginClick: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> {
                showToast(context, "Đăng nhập thành công")
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true  }
                }
            }
            else -> Unit
        }
    }

//    LaunchedEffect(uiState) {
////        if (uiState is UiState.Success) {
////            navController.navigate("home") {
////                popUpTo("login") { inclusive = true }
////            }
////        }
////    }
    LoginContent(
        uiState = uiState,
        onLoginClick = onLoginClick,
        onGoogleLogin = onGoogleLogin,
        onRegisterClick = {

            onRegisterClick()}
    )
}

@Composable
fun LoginHeader(
    onRegisterClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        tonalElevation = 8.dp,
        onClick = onRegisterClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_registration),
            contentDescription = null,
            modifier = Modifier
                .size(58.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = CircleShape,
                    clip = false
                )
                .background(
                    Color.White,
                    RoundedCornerShape(12.dp)
                )
//            .clickable(
//                indication = ripple(bounded = false),
//                interactionSource = remember { MutableInteractionSource() }
//            ) {onRegisterClick()}
                .padding(6.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
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
    uiState: UiState<AuthResult>,
    onLoginClick: (String, String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var hasSubmitted by remember { mutableStateOf(false) }

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val errorEmail = hasSubmitted && (!isEmailValid)
    val passwordError = hasSubmitted && (password.length < 8)
    val isValidForm = (isEmailValid && email.isNotBlank()) && (password.length >= 8 && password.isNotBlank())

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
            email = it },
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

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
    if (errorEmail) {
        Text("Email không hợp lệ",  modifier = Modifier
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
            password = it },
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

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            )
    )
    if (passwordError) {
        Text("Mật khẩu không hợp lệ",  modifier = Modifier
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

//    Button(
//        onClick = { onLoginClick(email, password) },
//        enabled = uiState !is UiState.Loading,
//    ) {
//        Text("Đăng nhập")
//    }


    //error
    if (uiState is UiState.Error) {

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

        ) }

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

        enabled = uiState !is UiState.Loading,
        colors = ButtonDefaults.buttonColors(Color(0xff222222)),
        shape = RoundedCornerShape(14.dp)

    ) {
        if (uiState is UiState.Loading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text("Đăng nhập")
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LoginFormPreview(

) {
    LoginContent(
        uiState = UiState.Idle,
        onLoginClick = { _, _ -> },
        onRegisterClick = {},
        onGoogleLogin = {})
}


//review
@Composable
fun LoginContent(
    uiState: UiState<AuthResult>,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onGoogleLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        secondBlue,
                        Color.White
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(20.dp),

            ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to PrimaryBlue,
                                0.15f to PrimaryBlue,
                                0.25f to Color.White,
                                1.5f to Color.White
                            )
                        )
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginHeader(onRegisterClick = onRegisterClick)
                    Spacer(Modifier.height(24.dp))
                    LoginForm(
                        uiState = uiState,
                        onLoginClick = onLoginClick
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
                        uiState = uiState,
                        onGoogleLogin = onGoogleLogin
                    )
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
    uiState: UiState<AuthResult>,
    onGoogleLogin: () -> Unit
) {
    IconButton(
        onClick = onGoogleLogin,
        enabled = uiState !is UiState.Loading,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.ic_gg),
            modifier = Modifier
                .fillMaxWidth()
                .size(56.dp),
            contentDescription = null,
            alignment = Alignment.Center,
        )
    }
}


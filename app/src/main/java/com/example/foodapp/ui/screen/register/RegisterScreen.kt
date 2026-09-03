package com.example.foodapp.ui.screen.register

import android.annotation.SuppressLint
 import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.foodapp.R
import com.example.foodapp.core.utils.showToast
import coloredShadow
import com.example.foodapp.presentation.state.AppState
import com.example.foodapp.presentation.state.AuthUiState
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.ui.screen.splash.LoadingBtn
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Blue4
import com.example.foodapp.ui.theme.BlueGreen
import com.example.foodapp.ui.theme.Gray
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.MediumGray
import com.example.foodapp.ui.theme.PrimaryBlue
import com.example.foodapp.ui.theme.Yellow3
import com.example.foodapp.ui.theme.secondBlue


@SuppressLint("ContextCastToActivity", "RestrictedApi", "StateFlowValueCalledInComposition")
@Composable
fun RegisterScreen(
    appState: AppState,
    onRegisterClick: (String, String) -> Unit,
    onResetState: () -> Unit,
    onBackToLogin: () -> Unit,
) {
    val context = LocalContext.current
    val showLoading = appState is AppState.Loading
    val activity = LocalContext.current as? ComponentActivity ?: return

    val authViewModel: AuthViewModel = hiltViewModel(viewModelStoreOwner = activity)

    //check loading btn register
    val authUiState by authViewModel.authUiState.collectAsStateWithLifecycle()


    if (showLoading) {
        showToast(context, "loading")

    }

    LaunchedEffect(appState) {
        when (appState) {
            is AppState.LoggedIn -> {
                showToast(context, "Đăng ký thành công")
                onResetState()
            }

            is AppState.Error -> {
                showToast(context, appState.message)
                Log.d("check_error_register", "error ${appState.message}")

            }

            is AppState.Loading -> {
                showToast(context, "loading res")
                Log.d("check_error_register", "loading")
            }

            else -> {
                Log.d("check_error_register", "else")
            }
        }
    }


             RegisterContent(
                onRegisterClick = onRegisterClick,
                onClickLogin = onBackToLogin,
                authUiState = authUiState,
            )
     }



@Composable
fun RegisterContent(
    onRegisterClick: (String, String) -> Unit,
    onClickLogin: () -> Unit,
    authUiState: AuthUiState,

    ) {


     Box(
        modifier = Modifier.fillMaxSize(),
//            .background(
//                brush = Brush.horizontalGradient(
//                    listOf(
//                        secondBlue,
//                        Color.White
//                    )
//                )
//            ),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(R.drawable.bg_sky9),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
//        Box(
//            modifier = Modifier
//                .padding(horizontal = 12.dp)
//                .fillMaxWidth()
//                .height(602.dp)
//                .background(Color.White, RoundedCornerShape(22.dp))
//        )

         LazyColumn() {
             item {
                 Box(
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(16.dp)
                         .coloredShadow(
                             colors = listOf(Gray65), 1f, blurRadius = 5.dp, spread = 0.1.dp
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
                             ), RoundedCornerShape(20.dp)
                         )
                 ) {
                     Column(
                         modifier = Modifier
                             .padding(20.dp)
                             .fillMaxWidth(),
                         horizontalAlignment = Alignment.CenterHorizontally
                     ) {
                         HeaderRegister()

                         Spacer(Modifier.height(30.dp))


                         RegisterForm(
                             onClickRegister = onRegisterClick, authUiState = authUiState
                         )

                         Spacer(Modifier.height(0.dp))



                         LoginHint(onClickLogin)




                     }
                 }
             }
         }

    }
}


@Composable
fun HeaderRegister() {
    val montserratExtraBold = FontFamily(Font(R.font.montserrat_extrabold))
    val montserratMedium = FontFamily(Font(R.font.montserrat_semibold))
    Text(
        text = "Tạo tài khoản",
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        fontSize = 22.sp,
        fontWeight = FontWeight.ExtraBold,
//        color = Color(0xff00A3A3),
        color = Color.Black,
        textAlign = TextAlign.Center
    )
    Text(
        text = "Vui lòng nhập thông tin của bạn",
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Gray,
        textAlign = TextAlign.Center
    )


}

@Preview
@Composable
fun RegisterFormReview() {
    RegisterContent(
        { _, _ -> Unit }, {}, authUiState = AuthUiState(isLoadingRegister = false)
    )

}

@Composable
fun RegisterForm(
    onClickRegister: (String, String) -> Unit,
    authUiState: AuthUiState,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var hasSubmitted by remember { mutableStateOf(false) }
    //error
//    var emailError by remember { mutableStateOf(false)}


    Text(
        text = "Email của bạn",
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 2.dp)
    )

    //email field
    val isEmailValid =
        Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() && email.substringAfterLast(
            ".",
            ""
        ).length > 2
    val emailError = hasSubmitted && (email.isBlank() || !isEmailValid)
    val passwordError = hasSubmitted && password.isNotBlank() && password.length < 8
    val confirmPasswordError =
        hasSubmitted && confirmPassword.isNotBlank() && (password != confirmPassword)
    val isFormValid =
        isEmailValid && email.isNotBlank() && password.length >= 8 && password == confirmPassword

    val canClick = (email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank())

    OutlinedTextField(
        value = email,
        onValueChange = {
            hasSubmitted = false
            email = it
        },
        placeholder = { Text("email", color = Color.Gray) },
        isError = emailError,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color.Gray,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(23.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedIndicatorColor = Blue1.copy(0.3f),
            focusedContainerColor = MediumGray,
            unfocusedContainerColor = MediumGray,
            disabledContainerColor = MediumGray,

            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
        ),
    )
    if (emailError) Text(
        "Địa chỉ email không hợp lệ",
        color = Color.Red,
        modifier = Modifier
            .padding(top = 6.dp, bottom = 6.dp)
            .fillMaxWidth()
            .padding(10.dp),
        textAlign = TextAlign.Start
    )



    Text(
        "Mật khẩu của bạn",
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, bottom = 10.dp)
    )

    //password field
    OutlinedTextField(
        value = password,
        isError = passwordError,
        onValueChange = {
            hasSubmitted = false
            password = it
        },
        visualTransformation = if (showPassword) VisualTransformation.None
        else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(20.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Lock, contentDescription = null, tint = Color.Gray
            )
        },
        placeholder = {
            Text(
                text = "Mật khẩu",

                color = Color.Gray, modifier = Modifier
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,

            focusedContainerColor = MediumGray,
            unfocusedContainerColor = MediumGray,
            disabledContainerColor = MediumGray,

            focusedIndicatorColor = Blue1.copy(0.3f),
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        //false -> true (on), ,
        trailingIcon = {
            IconButton(
                onClick = { showPassword = !showPassword }) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.Visibility
                    else Icons.Default.VisibilityOff, contentDescription = null,
//                    tint = if (password.isNotBlank() && !isEmailValid)
//                        Color.Red
//                    else
//                        MediumGray
                    tint = Color.Gray
                )
            }
        })
    if (passwordError) {
        Text(
            text = "Mật khẩu phải 8 ký tự trở lên",
            modifier = Modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Start,
            color = Color.Red
        )
    }
    Text(
        "Xác nhận mật khẩu",
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, bottom = 10.dp)
    )

    //confirm pass field
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = {
            hasSubmitted = false
            confirmPassword = it
        },
        isError = confirmPasswordError,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (showConfirmPassword) VisualTransformation.None
        else PasswordVisualTransformation(),
        shape = RoundedCornerShape(20.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Lock, contentDescription = null, tint = Color.Gray
            )
        },
        placeholder = {
            Text(
                text = "Nhập lại mật khẩu", color = Color.Gray, modifier = Modifier
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,

            focusedContainerColor = MediumGray,
            unfocusedContainerColor = MediumGray,
            disabledContainerColor = MediumGray,

            focusedIndicatorColor = Blue1.copy(0.3f),
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorContainerColor = Color.Transparent,

            ),
        trailingIcon = {
            IconButton(
                onClick = { showConfirmPassword = !showConfirmPassword }) {
                Icon(
                    imageVector = if (showConfirmPassword) Icons.Default.Visibility
                    else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    modifier = Modifier,
                    tint = Color.Gray
                )
            }

        })
    val context = LocalContext.current
    if (confirmPasswordError) {
        Text(
            "Mật khẩu nhập lại không khớp",
            color = Color.Red,
            modifier = Modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Start
        )
    }

    Spacer(Modifier.height(70.dp))
    //button register
    //email = isEmail (false), email (false) -> false
    //true ||
    Log.d("check_can_click_btn", "can click${canClick}")
    Log.d("check_can_click_btn", "iaLoading${!authUiState.isLoadingLogin}")

    Log.d("check_can_click_btn", "sum = ${canClick && !authUiState.isLoadingLogin}")
    Button(
        enabled = canClick && !authUiState.isLoadingRegister, onClick = {
            hasSubmitted = true
            Log.d("check_error_register", "dk: $isFormValid")
            if (isFormValid) {
                showToast(context = context, "click regis")
                onClickRegister(email.trim(), password)
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .height(52.dp), shape = RoundedCornerShape(10.dp),
//        colors = ButtonDefaults.buttonColors(BlueGreen),
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue1,
            disabledContainerColor = Blue1.copy(0.4f),

            )
    ) {

        if (authUiState.isLoadingRegister) {

            LoadingBtn()
        } else {
            Text(
                text = "Đăng ký", fontSize = 16.sp, color = Color.White
            )
        }

    }
}

@Composable
fun LoginHint(
    onClickLogin: () -> Unit
) {

    Row(
        modifier = Modifier, verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Bạn đã có tài khoản?", modifier = Modifier
        )
        Text(
            text = "Đăng nhập", modifier = Modifier
                .padding(5.dp)
                .clickable {
                    onClickLogin()
                }, color = BlueGreen, fontWeight = FontWeight.Bold
        )
    }
}
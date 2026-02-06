package com.example.foodapp.ui.screen.register

import android.util.Patterns
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodapp.core.AuthResult
import com.example.foodapp.core.UiState
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.ui.theme.BlueGreen
import com.example.foodapp.ui.theme.MediumGray
import com.example.foodapp.ui.theme.PrimaryBlue
import com.example.foodapp.ui.theme.secondBlue


@Composable
fun RegisterScreen(
    navController: NavController,
    uiState: UiState<AuthResult>,
    onRegisterClick: (String, String) -> Unit,
    onResetState: () -> Unit,
) {
    val context = LocalContext.current
    when (uiState) {
        is UiState.Success -> {
            showToast(context, "Đăng ký thành công")
            onResetState()
            navController.navigate("home")
        }
        is UiState.Error -> showToast(context, uiState.message)
        else -> Unit
    }
    RegisterContent(
        onRegisterClick = { email, password ->
            onRegisterClick(email, password)
        },
        onClickLogin = {
            navController.popBackStack()

        }
    )

}


@Composable
fun RegisterContent(
    onRegisterClick: (String, String) -> Unit,
    onClickLogin: () -> Unit

) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
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
                .fillMaxWidth()
                .padding(15.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to PrimaryBlue,
                                0.15f to PrimaryBlue,
                                0.25f to Color.White,
                                1.5f to Color.White
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeaderRegister()
                    Spacer(Modifier.height(10.dp))
                    RegisterForm(onClickRegister = onRegisterClick)
                    Spacer(Modifier.height(20.dp))
                    LoginHint(onClickLogin)
                }
            }
        }
    }
}


@Composable
fun HeaderRegister() {
    Text(
        text = "Tạo tài khoản",
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
//        color = Color(0xff00A3A3),
        color = Color.Black,
        textAlign = TextAlign.Center
    )
    Text(
        text = "Vui lòng nhập thông tin chi tiết của bạn",
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Gray,
        textAlign = TextAlign.Center
    )


}

@Preview
@Composable
fun RegisterFormReview() {
    RegisterContent({ _, _ -> Unit }, {})
}

@Composable
fun RegisterForm(
    onClickRegister: (String, String) -> Unit,
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, start = 2.dp)
    )

    //email field
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
            email = it },
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

            focusedContainerColor = MediumGray,
            unfocusedContainerColor = MediumGray,
            disabledContainerColor = MediumGray,

            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
    )
    if (emailError) Text(
        "Địa chỉ email không hợp lệ", color = Color.Red, modifier = Modifier
            .padding(top = 6.dp, bottom = 6.dp)
            .fillMaxWidth()
            .padding(10.dp),
        textAlign = TextAlign.Start
    )



    Text(
        "Mật khẩu của bạn",
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
            password = it },
        visualTransformation =
            if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(20.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        placeholder = {
            Text(
                text = "Mật khẩu",
                color = Color.Gray,
                modifier = Modifier
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,

            focusedContainerColor = MediumGray,
            unfocusedContainerColor = MediumGray,
            disabledContainerColor = MediumGray,

            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        //false -> true (on), ,
        trailingIcon = {
            IconButton(
                onClick = { showPassword = !showPassword }) {
                Icon(
                    imageVector =
                        if (showPassword)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                    contentDescription = null,
//                    tint = if (password.isNotBlank() && !isEmailValid)
//                        Color.Red
//                    else
//                        MediumGray
                    tint = Color.Gray
                )
            }
        }
    )
    if (passwordError) {
        Text(
            text = "Mật khẩu phải 8 ký tự trở lên", modifier = Modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Start,
            color = Color.Red
        )
    }
    Text(
        "Xác nhận mật khẩu",
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, bottom = 10.dp)
    )

    //confirm pass field
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = {
            hasSubmitted = false
            confirmPassword = it },
        isError = confirmPasswordError,
        modifier = Modifier
            .fillMaxWidth(),
        visualTransformation =
            if (showConfirmPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
        shape = RoundedCornerShape(20.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        placeholder = {
            Text(
                text = "Nhập lại mật khẩu",
                color = Color.Gray,
                modifier = Modifier
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,

            focusedContainerColor = MediumGray,
            unfocusedContainerColor = MediumGray,
            disabledContainerColor = MediumGray,

            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(
                onClick = { showConfirmPassword = !showConfirmPassword }
            ) {
                Icon(
                    imageVector =
                        if (showConfirmPassword) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    modifier = Modifier,
                    tint = Color.Gray
                )
            }

        }
    )
    if (confirmPasswordError) {
        Text(
            "Mật khẩu nhập lại không khớp", color = Color.Red, modifier = Modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Start
        )
    }

    Spacer(Modifier.height(50.dp))
    //button register
    //email = isEmail (false), email (false) -> false
    //true ||
    Button(
        enabled = canClick,
        onClick = {
            hasSubmitted = true
            if (isFormValid) {
                onClickRegister(email, password)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
//        colors = ButtonDefaults.buttonColors(BlueGreen),
        colors = ButtonDefaults.buttonColors(Color.Black)
    ) {

        Text(
            text = "Đăng ký",
            fontSize = 16.sp
        )
    }

}

@Composable
fun LoginHint(
    onClickLogin: () -> Unit
) {

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Bạn đã có tài khoản?",
            modifier = Modifier
        )
        Text(
            text = "Đăng nhập",
            modifier = Modifier
                .padding(5.dp)
                .clickable {
                    onClickLogin()
                }

            ,
            color = BlueGreen,
            fontWeight = FontWeight.Bold
        )
    }
}
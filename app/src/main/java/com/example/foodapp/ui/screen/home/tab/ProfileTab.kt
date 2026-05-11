package com.example.foodapp.ui.screen.home.tab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.presentation.state.EditProfileState
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.Gray85
import com.example.foodapp.ui.theme.Yellow3


@Composable
fun ProfileTabRoute(
    onClickBack: () -> Unit,
    onProfileCompleted: () -> Unit = {}
) {
    val profileViewModel: UserProfileViewModel = hiltViewModel()
    val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()

    ProfileTab(
        onClickBack = onClickBack,
        onProfileCompleted = onProfileCompleted,
        profileUiState = profileState,

        onEnable = {
            profileViewModel.setEnable(true)
        },

        onFieldChange = profileViewModel::onFieldChange,

        onUpdateProfile = {
            profileViewModel.onCheckedChange()
            profileViewModel.updateUserProfile()
        })
}

@Composable
fun ProfileTab(
    onClickBack: () -> Unit,
    onProfileCompleted: () -> Unit = {},
    profileUiState: ProfileUiState,
    onEnable: () -> Unit,
    onFieldChange: (String, String) -> Unit,
    onUpdateProfile: () -> Unit,
) {

    LaunchedEffect(profileUiState.successMessage) {
        if (profileUiState.successMessage != null && profileUiState.profileCompleteness == ProfileCompleteness.COMPLETE) {
            onProfileCompleted()
        }
    }
    var clickedUpdate by remember { mutableStateOf(false) }

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf2f6f8))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.2f), shape = CircleShape)
                    .padding(6.dp)
                    .clickable { onClickBack() })


        }


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            item {
                HeaderProfileUser(
                    uiState = profileUiState,
                    onEnable = onEnable,
                    onFieldChange = onFieldChange,
                    onUpdateProfile = onUpdateProfile,
                )
            }

//            item {
//                Text(
//                    text = "$profileUiState",
//                    )
//            }
        }
    }

//
//    TabRow(
//        selectedTabIndex = 0
//    ) { }
}

@Composable
fun HeaderProfileUser(
    uiState: ProfileUiState,
    onUpdateProfile: () -> Unit,
    onEnable: () -> Unit,
    onFieldChange: (String, String) -> Unit,
) {

    val context = LocalContext.current



    Spacer(Modifier.height(20.dp))

//    AsyncImage(
//        model = "",
//        contentDescription = null,
//        modifier = Modifier
//            .size(150.dp)
//            .clip(CircleShape)
//            .background(Color.White)
//            .padding(0.dp),
//        contentScale = ContentScale.Crop,
//        placeholder = painterResource(R.drawable.avatar1),
//
//    )

    //avatar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, RoundedCornerShape(30.dp))
            .background(
                Color.White,
                RoundedCornerShape(30.dp),
            )
            .clickable {
                showToast(context, "Chưa hổ trợ tính năng đổi avatar")
            }
            .padding(horizontal = 10.dp, vertical = 15.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier

        ) {
            Box {
                AsyncImage(
                    model = "user.avatarUrl",
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_avatar1),
                    error = painterResource(R.drawable.avatar1),
                    fallback = painterResource(R.drawable.avatar1),
                )

                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .offset { IntOffset(x = 100, y = 100) }
                        .shadow(
                            elevation = 2.dp, CircleShape
                        )
                        .background(Color.White, CircleShape)
                        .padding(5.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Tran Binh",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "+09078778",
                    color = Color.Gray.copy(alpha = 0.7f)
                    )
            }

            Spacer(Modifier.weight(1f))


                Box(
                    modifier = Modifier
                        .shadow(0.5.dp, RoundedCornerShape(8.dp))
                        .background(Gray85.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Hồ sơ",
                        )
//                        Icon(
//                            imageVector = Icons.Rounded.ArrowForwardIos,
//                            tint = Gray65,
//                            contentDescription = null
//                        )
                    }

                }



        }

    }


    Spacer(Modifier.height(50.dp))

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Yellow3.copy(alpha = 0.2f), Blue1.copy(alpha = 0.5f)
                    )
                )
            )
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row {
                Box {
                    Image(
                        painter = painterResource(R.drawable.bg_rec_card),
                        contentDescription = null,
                        modifier = Modifier.width(200.dp)
                    )

                    Text(
                        text = "GOLDEN FLAVOR",
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
                        fontSize = 16.sp,
                        color = Color(0xFF306345).copy(alpha = 0.6f),
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .padding(
                            vertical = 10.dp, horizontal = 10.dp
                        )
                        .background(
                            Color(0xFFECFFFE), RoundedCornerShape(10.dp)
                        ), contentAlignment = Alignment.Center
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(vertical = 7.dp, horizontal = 10.dp),

                        ) {
                        Text(
                            text = "Xem tiến trình", color = Color(0xFF119DC4)
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForwardIos,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF119DC4)
                        )
                    }
                }

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(start = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.bg_cutlery), contentDescription = null
                    )
                    Text(
                        text = "Bậc thầy gọi món",
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                color = Color(0xFF8DB28A), RoundedCornerShape(30.dp)
                            )
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Điểm tích lũy",
                        color = Color(0xFF0E6A84),
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFF6D98B), Color(0xFFFFF0C9), Color(0xFFF3D27B)
                                    ),
//                                    start = Offset.Zero,
//                                    end = Offset.Infinite
                                )
                            ), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "0",
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFB07D30)
                        )
                    }
                }
            }

            Box(
                Modifier
                    .height(1.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tổng số xu\nđã tích lũy",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = "1",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color(0xFF1A5302)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Tổng món\nđã đặt",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = "100.000",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color(0xFF1A5302)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Món tích lũy\nnăm 2026",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = "100.000",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color(0xFF1A5302)
                    )
                }
            }
        }
    }


    Spacer(Modifier.height(50.dp))
    //edit info Box
    Box(
        Modifier
            .background(Color.White, RoundedCornerShape(30.dp))
            .border(
                BorderStroke(1.dp, Gray100),
                RoundedCornerShape(30.dp),
            ),
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(
                10.dp, alignment = Alignment.CenterVertically
            ),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .shadow(elevation = 2.dp, CircleShape)
                        .background(Blue2, CircleShape)
                        .padding(3.dp)
                        .clickable {
                            onEnable()
                        },
                    tint = Blue0
                )
            }


            OutlinedTextField(
                value = uiState.editProfile.email,
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Gray65.copy(alpha = 0.3f), RoundedCornerShape(15.dp)
                    ),
                readOnly = true,
                enabled = false,
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Gray,
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Gray65,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color.Black
                )

            )
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "Tên người dùng",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )


            OutlinedTextField(
                value = uiState.editProfile.name,
                onValueChange = {
                    onFieldChange("name", it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Gray65.copy(alpha = 0.3f), RoundedCornerShape(15.dp)
                    ),
                enabled = uiState.isEnable,
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedBorderColor = Gray65,
                    focusedBorderColor = Color.Blue,
                    cursorColor = Color.Black
                )
            )

            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "địa chỉ",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )

            OutlinedTextField(
                value = uiState.editProfile.address,
                onValueChange = {
                    onFieldChange("address", it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Gray65.copy(alpha = 0.3f), RoundedCornerShape(15.dp)
                    ),
                shape = RoundedCornerShape(15.dp),
                enabled = uiState.isEnable,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedBorderColor = Gray65,
                    focusedBorderColor = Color.Blue,
                    cursorColor = Color.Black
                )
            )
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "Số điện thoại",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )

            OutlinedTextField(
                value = uiState.editProfile.phone,
                onValueChange = {
                    onFieldChange("phone", it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Gray65.copy(alpha = 0.3f), RoundedCornerShape(15.dp)
                    ),
                shape = RoundedCornerShape(15.dp),
                enabled = uiState.isEnable,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Gray,
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Gray65,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color.Black
                )

            )

        }

    }

    Spacer(Modifier.height(30.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = uiState.isEnable,
                onClick = {
                    showToast(context, "Cập nhật thành công")
                    onUpdateProfile()

                }
            )
            .background(
                Blue1,
                RoundedCornerShape(30.dp)
            )
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Cập nhật",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (uiState.isEnable) Color.White else Gray65
        )
    }
//    if (uiState.isEditMode) {
//        Icon(
//            imageVector = Icons.Default.Check,
//            contentDescription = null,
//            tint = Color.Green,
//            modifier = Modifier
//                .background(
//                    Color.Black.copy(alpha = 0.25f), shape = CircleShape
//                )
//                .padding(6.dp)
//                .clickable {
//                    showToast(context, "Clicked")
//                    onUpdateProfile()
//                }
//        )
//    }
}

//@Composable
//fun EditCheckIcon(isEditMode: Boolean) {
//    Icon(
//        imageVector = Icons.Default.Check,
//        contentDescription = null,
//        tint = if (isEditMode) Color.Green else Color.White,
//        modifier = Modifier
//            .background(Color.Black.copy(alpha = 0.25f), shape = CircleShape)
//            .padding(6.dp)
//    )
//}
@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {

    ProfileTab(
        onClickBack = {},

        profileUiState = ProfileUiState(
            isEnable = true, isEditMode = true,

            editProfile = EditProfileState(
                name = "Tran Binh",
                email = "binh@gmail.com",
                address = "Ho Chi Minh",
                phone = "0123456789"
            )
        ), onEnable = {}, onFieldChange = { _, _ -> },

        onUpdateProfile = {},

        onProfileCompleted = {}
    )
}
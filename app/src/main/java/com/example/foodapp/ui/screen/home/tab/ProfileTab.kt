package com.example.foodapp.ui.screen.home.tab

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.theme.Gray65

@Composable
fun ProfileTab(
    onClickBack: () -> Unit
) {
    val userProfile: UserProfileViewModel = hiltViewModel()
    val profileState by userProfile.uiState.collectAsStateWithLifecycle()
    var clickedUpdate by remember { mutableStateOf(false) }

    val context = LocalContext.current
//
//
//    val tabList = listOf("Thông tin người dùng")
//    val index by remember { mutableStateOf(0) }userProfile.uiState.value.editProfile.address
//    val check = (profileState as? UiState.Success<UserProfileCombine>)
//    val data = check?.data?.isProfileComplete() ?: "null"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.25f), shape = CircleShape)
                    .padding(6.dp)
                    .clickable{ onClickBack() }
            )

            Spacer(modifier = Modifier.weight(1f))
//            EditCheckIcon(
//                isEditMode = profileState.isEditMode
//            )

//            IconButton(
//                onClick = { showToast(context, "Clicked") },
//                enabled = profileState.isEditMode
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Check,
//                    contentDescription = null,
//                    tint = if (profileState.isEditMode) Color.Green else Color.Gray,
//                    modifier = Modifier
//                        .background(Color.Black.copy(alpha = 0.25f), shape = CircleShape)
//                        .padding(6.dp)
//                )
//            }

            //muc tieu la go xong,
            // btn sang, click thi state clicked true,
            // udp xong, neu thanh cong, click false, ko thi true
            //check icon
            if (profileState.isEditMode) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Green ,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.25f), shape = CircleShape)
                        .padding(6.dp)
                        .clickable {
                            userProfile.onCheckedChange()
                            showToast(context, "Clicked")
                            userProfile.updateUserProfile()
                        }
                    //input -> input state = true
                    //clickUp ->            true
                )
            }
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            item {
                HeaderProfileUser(uiState = profileState, userProfile = userProfile)
            }

            item {
                Text(
                    text = "$profileState",

                )
            }
        }
    }

//
//    TabRow(
//        selectedTabIndex = 0
//    ) { }
}

@Composable
fun HeaderProfileUser(
    userProfile: UserProfileViewModel,
    uiState: ProfileUiState,
) {

    val context = LocalContext.current

    Text(
        text = "Hồ Sơ",
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    )

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
            .clickable {
                showToast(context, "Chưa hổ trợ tính năng đổi avatar")
            }
    ) {
        AsyncImage(
            model = "user.avatarUrl",
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.avatar1),
            error = painterResource(R.drawable.avatar1),
            fallback = painterResource(R.drawable.avatar1),
        )

        Icon(
            imageVector = Icons.Outlined.CameraAlt,
            contentDescription = null,
            modifier = Modifier
                .offset { IntOffset(x = 236, y = 188) }
                .shadow(
                    elevation = 2.dp, CircleShape
                )
                .background(Color.White, CircleShape)
                .padding(5.dp))
    }


    Spacer(Modifier.height(15.dp))


    //edit info Box
    Box(
        Modifier
            .background(Gray65.copy(alpha = 0.30f), RoundedCornerShape(25.dp)),
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterVertically
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
                        .background(Color.White, CircleShape)
                        .padding(3.dp)
                        .clickable{
                            userProfile.setEnable(true)
                        },

                )
            }


            OutlinedTextField(
                value = uiState.editProfile.email,
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Gray65.copy(alpha = 0.3f),
                        RoundedCornerShape(15.dp)
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
                onValueChange = { userProfile.onFieldChange("name",it)},
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Gray65.copy(alpha = 0.3f),
                        RoundedCornerShape(15.dp))
                ,
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
                onValueChange = { userProfile.onFieldChange("address", it) } ,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Gray65.copy(alpha = 0.3f),
                        RoundedCornerShape(15.dp)),
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
                onValueChange = {userProfile.onFieldChange("phone",it)},

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Gray65.copy(alpha = 0.3f),
                        RoundedCornerShape(15.dp)),
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

//        OutlinedTextField(
//            value = "name",
//            onValueChange = {},
//            modifier = Modifier,
//            enabled = true,
//            colors = OutlinedTextFieldDefaults.colors(
//                forcu
//            )
//        )

    }
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
@Preview (showBackground = true)
@Composable
fun showPreview() {
    ProfileTab(onClickBack = {})
}
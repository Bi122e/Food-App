package com.example.foodapp.ui.screen.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.foodapp.R
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.presentation.extentions.coloredShadow
import com.example.foodapp.presentation.state.EditProfileState
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.screen.main.section.FavoriteBottomSheet
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.DefaultBg1
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.Yellow3


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTabRoute(
    onClickBack: () -> Unit,
    onProfileCompleted: () -> Unit = {},
    paddingValues: PaddingValues,
    onUpdateProfile: () -> Unit,
) {
    val profileViewModel: UserProfileViewModel = hiltViewModel()
    val profileState by profileViewModel.uiStateProfile.collectAsStateWithLifecycle()



    ProfileTab(
        onClickBack = onClickBack,
        paddingValues = paddingValues,
        profileUiState = profileState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTab(
    paddingValues: PaddingValues,
    onClickBack: () -> Unit,
     profileUiState: ProfileUiState,
) {
    val screenH = LocalConfiguration.current.screenHeightDp
    val screenW = LocalConfiguration.current.screenWidthDp



    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color(0xFFf2f6f8))
            .background(DefaultBg1)
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {


        //back
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .padding(6.dp)
                    .clickable {
                        onClickBack()
                    }
            )
        }



        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + (screenH * 0.020).dp
            ),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(35.dp),
//            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            item {
                HeaderProfileUser(
                    uiState = profileUiState,

                )
            }

            //card
            item {

                //card
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
                                    modifier = Modifier.padding(
                                        vertical = 10.dp,
                                        horizontal = 16.dp
                                    ),
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
                                    modifier = Modifier.padding(
                                        vertical = 7.dp,
                                        horizontal = 10.dp
                                    ),

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
                                    painter = painterResource(R.drawable.bg_cutlery),
                                    contentDescription = null
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
                                                    Color(0xFFF6D98B),
                                                    Color(0xFFFFF0C9),
                                                    Color(0xFFF3D27B)
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
            }

            //favor
            item {
                //favorite
                var isShowFavorite by remember { mutableStateOf(false) }


                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .size(90.dp)
                            .clickable(
                                onClick = {
                                    isShowFavorite = true
                                }
                            )
                            .coloredShadow(
                                color = Gray65,
                                alpha = 0.5f,
                                borderRadius = 20.dp,
                                blurRadius = 3.dp
                            )
                            .background(
                                Color.White,
                                RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .padding(6.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Rounded.Favorite,
                                contentDescription = null,
                                tint = Color.Red.copy(0.4f),
                                modifier = Modifier
                                    .size(32.dp)
                            )

                            Text(
                                text = "Yêu thích",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(0.7f)
                            )
                        }
                    }

                    //thanh toan
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .size(90.dp)
                            .coloredShadow(
                                color = Gray65,
                                alpha = 0.5f,
                                borderRadius = 20.dp,
                                blurRadius = 3.dp
                            )
                            .background(
                                Color.White,
                                RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center

                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                5.dp,
                                Alignment.CenterVertically
                            ),
                            modifier = Modifier
                                .padding(6.dp)
                                .fillMaxHeight(),

                            ) {

                            Icon(
                                imageVector = Icons.Rounded.Wallet,
                                contentDescription = null,
                                tint = Color.Blue.copy(0.5f),
                                modifier = Modifier
                                    .size(32.dp)
                            )

                            Text(
                                text = "Thanh toán",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(0.7f)
                            )
                        }
                    }

                    //dia chi
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .size(90.dp)
                            .coloredShadow(
                                color = Gray65,
                                alpha = 0.5f,
                                borderRadius = 20.dp,
                                blurRadius = 3.dp
                            )
                            .background(
                                Color.White,
                                RoundedCornerShape(20.dp)
                            ),

                        ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                5.dp,
                                Alignment.CenterVertically
                            ),
                            modifier = Modifier
                                .padding(start = 25.dp)
                                .fillMaxHeight()
                        ) {

                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = Color.Magenta.copy(0.5f),
                                modifier = Modifier
                                    .size(32.dp)
                            )

                            Text(
                                text = "Địa chỉ",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(0.7f)
                            )
                        }
                    }

                    var isShowFavorite by remember { mutableStateOf(false) }

                    if (isShowFavorite) {
                        FavoriteBottomSheet(
                            onDismiss = {
                                isShowFavorite = false
                            },
                            restaurant = Restaurant()
                        )
                    }
                }
            }


            //bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color(0xFFfbf9ed),
                            RoundedCornerShape(15.dp)
                        )
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = Color(0xFFd5a82d),
                                modifier = Modifier
                                    .size(28.dp)
                            )

                            Text(
                                text = "Thanh điều hướng",
                                color = Color(0xFFd5a82d),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Bạn có thể thay đổi tùy chọn giao diện thanh điều hướng",
                                color = Color.Black,
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                            )

                            Icon(
                                Icons.Rounded.ArrowForwardIos,
                                contentDescription = null,
                                tint = Color.Black,
                            )
                        }

                    }
                }
            }

            //hổ trợ trung tâm hổ trợ
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Hổ trợ",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = 16.dp)
                ) {

                    Icon(
                        painterResource(R.drawable.ic_call_center),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Trung tâm hổ trợ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        Icons.Rounded.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }

            //setting
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Cài đặt chung",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = 16.dp)
                ) {

                    Icon(
                        painterResource(R.drawable.ic_language),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Ngôn ngữ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        Icons.Rounded.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }

            //ban co hai long
            item {
                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White.copy(0.6f),
                            RoundedCornerShape(20.dp)
                        )
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Favorite,
                            contentDescription = null,
                            tint = Color.Red.copy(0.6f),
                            modifier = Modifier.size(32.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {

                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Bạn có hài lòng ứng dụng chứ",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Phản hồi của bạn giúp chúng tôi ngày hoàng thiện hơn",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black.copy(0.5f),
                                    fontSize = 14.sp
                                )
                            }

                            Icon(
                                Icons.Rounded.ArrowForwardIos,
                                contentDescription = null,
                                modifier = Modifier
                                    .coloredShadow(
                                        Gray65,
                                        0.5f,
                                        10.dp,
                                        4.dp,
                                    )
                                    .size(34.dp)
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(8.dp)
                            )

                        }
                    }
                }
            }


            //log out
            item {
                Spacer(Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLogOut()
                        }
                        .padding(start = 16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_logout2),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
//                        tint = Color.Unspecified
                    )
                    Text(
                        text = "Đăng xuất",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        fontSize = 16.sp
                    )

                }
            }
        }
    }

//
//    TabRow(
//        selectedTabIndex = 0
//    ) { }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderProfileUser(
    uiState: ProfileUiState,

    ) {

    val context = LocalContext.current


    Spacer(Modifier.height(20.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, RoundedCornerShape(30.dp))
            .background(
                Color.White,
                RoundedCornerShape(30.dp),
            )
            .padding(horizontal = 10.dp, vertical = 15.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier

        ) {
            Box {
                Image(
                    painter = painterResource(R.drawable.ic_avatar1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            showToast(
                                context = context,
                                message = "Chưa hổ trợ tính năng đổi avatar"
                            )
                        }
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
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
                    .background(Gray100, RoundedCornerShape(8.dp))
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Hồ sơ",
                )


            }


        }
    }
}


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
        ),
        paddingValues = PaddingValues()
    )
}
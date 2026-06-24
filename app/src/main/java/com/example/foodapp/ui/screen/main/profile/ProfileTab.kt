package com.example.foodapp.ui.screen.main.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.activity.ComponentActivity
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
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.foodapp.R
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.presentation.state.EditProfileState
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.activity.MainActivity
import com.example.foodapp.ui.screen.main.profile.section.AddressProfileSection
import com.example.foodapp.ui.screen.main.profile.section.EvaluateProfileSection
import com.example.foodapp.ui.screen.main.profile.section.FavoriteProfileSection
import com.example.foodapp.ui.screen.main.profile.section.HeaderProfileSection
 import com.example.foodapp.ui.screen.main.profile.section.LogoutProfileSection
import com.example.foodapp.ui.screen.main.profile.section.NavBottomBarProfileSection
import com.example.foodapp.ui.screen.main.profile.section.PaymentProfileSection
import com.example.foodapp.ui.screen.main.profile.section.SettingProfileSection
import com.example.foodapp.ui.screen.main.profile.section.StatisticalProfileSection
import com.example.foodapp.ui.screen.main.profile.section.SupportSettingProfileSection
import com.example.foodapp.ui.screen.main.shared.FavoriteBottomSheet
import com.example.foodapp.ui.screen.shared.SnackBarSuccessOrder
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.DefaultBg1
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.Yellow3


@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTabRoute(
    onClickBack: () -> Unit,
     paddingValues: PaddingValues,
     onNavigationToInfoTab: () -> Unit,
) {
    val profileViewModel: UserProfileViewModel = hiltViewModel()
    val profileState by profileViewModel.uiStateProfile.collectAsStateWithLifecycle()
    val activity = LocalContext.current as ComponentActivity
    val authViewModel: AuthViewModel = hiltViewModel(activity)


    ProfileTab(
        onClickBack = onClickBack,
        paddingValues = paddingValues,
        profileUiState = profileState,
        onLogout = { authViewModel.logout() },
        onNavigationToInfoTab = onNavigationToInfoTab,

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTab(
    paddingValues: PaddingValues,
    onClickBack: () -> Unit,
    profileUiState: ProfileUiState,
    onLogout: () -> Unit,
    onNavigationToInfoTab: () -> Unit,
) {
    val screenH = LocalConfiguration.current.screenHeightDp
    val screenW = LocalConfiguration.current.screenWidthDp
    var showSnackBar by remember { mutableStateOf(false) }



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

            ) {
            item {
                HeaderProfileSection(
                    uiState = profileUiState,
                    onNavigationToInfoTab = onNavigationToInfoTab,
                )
            }

            //statistical box
            item {
                StatisticalProfileSection()
            }

            //favor - pay - add -> row
            item {
                //favorite
                var isShowFavorite by remember { mutableStateOf(false) }


                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    //favorite
                    FavoriteProfileSection(
                        onClickFavorite = {
                            isShowFavorite = true
                        },
                        modifier = Modifier.weight(1f)
                    )

                    //thanh toan
                    PaymentProfileSection(
                        modifier = Modifier.weight(1f),
                        showSnackBar = {
                            showSnackBar = true
                        }
                    )

                    //addres
                    AddressProfileSection(
                        modifier = Modifier.weight(1f)
                    )


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


            //option field
            item {

                NavBottomBarProfileSection()
            }

            //hổ trợ trung tâm hổ trợ
            item {
                Spacer(Modifier.height(20.dp))
                SupportSettingProfileSection()
            }

            //setting
            item {
                Spacer(Modifier.height(20.dp))
                SettingProfileSection()
            }

            //ban co hai long
            item {
                Spacer(Modifier.height(20.dp))
                EvaluateProfileSection() //danh gia
            }


            //log out
            item {
                Spacer(Modifier.height(20.dp))

                LogoutProfileSection(
                    onLogout = onLogout
                )
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
//            .background(Color.Red)
    ) {
        SnackBarSuccessOrder(
            showSnackBar = showSnackBar,
            onValueChange = {
                showSnackBar = false
            }
        )
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
        paddingValues = PaddingValues(),
        onLogout = {},
        onNavigationToInfoTab = {},
    )
}
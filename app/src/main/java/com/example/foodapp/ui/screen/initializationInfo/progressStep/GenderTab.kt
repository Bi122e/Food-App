package com.example.foodapp.ui.screen.initializationInfo.progressStep

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coloredShadow
import com.example.foodapp.R
import com.example.foodapp.domain.model.User
 import com.example.foodapp.presentation.state.EditProfileState
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.DefaultBg
import com.example.foodapp.ui.theme.DefaultBg1
import com.example.foodapp.ui.theme.Gray
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray2
import com.example.foodapp.ui.theme.Gray65
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay

@Composable
fun GenderTab(
    onValidStep: () -> Unit,
    updateUserProfile: () -> Unit,
    profileUiState: ProfileUiState,
    setClickedUpdate: () -> Unit,
    setGender: (String) -> Unit,
    onLoggedIn: (updateUser: User) -> Unit,
    ) {

    //tranh  race condition đọc state cũ
    LaunchedEffect(profileUiState.successMessage, profileUiState.user) {
        if (profileUiState.successMessage != null && profileUiState.user != null) {
            Log.d("check_profile_loggedIn", "GENDER")
            onLoggedIn(profileUiState.user)
        }
    }

    Log.d("set_gender_check", "state before = ${profileUiState.editProfile.gender}")

    val items = listOf(
        GenderItems("Anh", R.drawable.ic_male3),
        GenderItems("Chị", R.drawable.ic_female3),
        GenderItems("Không muốn tiết lộ", R.drawable.ic_other2),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = "Bạn muốn được xưng hô thế nào?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        )
        Text(
            text = "Danh xưng của bạn chọn sẽ trải nghiệm tốt hơn.",
            fontWeight = FontWeight.SemiBold,
            color = Color.Black.copy(0.4f),
            fontSize = 12.sp,
        )

        Spacer(Modifier.height(20.dp))


        Column(
            modifier = Modifier
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            items.forEachIndexed { index, item ->

                Box() {
//box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onValidStep()
                                setGender(item.name)
                                Log.d("set_gender_check", "click item$item, state = ${profileUiState.editProfile.gender}")
                            }
                            .size(100.dp)
                            .border(
                                BorderStroke(
                                    1.dp,
                                    if (item.name == profileUiState.editProfile.gender)
                                        Blue1
                                    else
                                        Color.Unspecified
                                ),
                                RoundedCornerShape(10.dp)
                            )
                            .coloredShadow(
                                listOf(Color.Black),
                                0.1f,
                                10.dp,
                                10.dp
                            )
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFFe9f1f7),
//                                        Color(0xFFdaf2f2),
                                        Color.White
                                    ),
                                ),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 25.dp)
                                .zIndex(1f),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(0.6f)
                            )

                            Spacer(Modifier.weight(1f))


                        }

                    }
                    Image(
                        painterResource(item.image),
                        contentDescription = null,

                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(100.dp)
                            .offset(y = (-30).dp)
                            .zIndex(1f)
                    )
                }


            }
        }


        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clickable(
                    enabled = profileUiState.editProfile.gender.isNotEmpty()
                ) {
                    updateUserProfile()
                    Log.d("check_profile_loggedIn", "GENDER dk: ${profileUiState.successMessage != null && profileUiState.user != null}")
                }
                .background(
                    if (profileUiState.editProfile.gender.isEmpty())
                        Blue1.copy(0.4f)
                    else Blue1,
                    RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hoàn tất",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

data class GenderItems(
    val name: String,
    val image: Int,
)


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewGenderTab() {

    GenderTab(
        onValidStep = {},
        setClickedUpdate = {},
        updateUserProfile = {},
        profileUiState = ProfileUiState(
            editProfile = EditProfileState(
                gender = "Anh"
            )
        ),
        setGender = {},
        onLoggedIn = {},

    )

}
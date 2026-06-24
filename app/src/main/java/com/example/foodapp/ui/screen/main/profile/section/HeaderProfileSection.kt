package com.example.foodapp.ui.screen.main.profile.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.ui.theme.Gray100


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderProfileSection(
    uiState: ProfileUiState,
    onNavigationToInfoTab: () -> Unit,

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
                    text = uiState.editProfile.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "+${uiState.editProfile.phone}",
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }

            Spacer(Modifier.weight(1f))


            Box(
                modifier = Modifier
                    .shadow(0.5.dp, RoundedCornerShape(8.dp))
                    .background(Gray100, RoundedCornerShape(8.dp))
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .clickable(
                        onClick = onNavigationToInfoTab
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Hồ sơ",
                )


            }


        }
    }
}
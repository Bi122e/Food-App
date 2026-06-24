package com.example.foodapp.ui.screen.main.complete


import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material.icons.rounded.SentimentNeutral
import androidx.compose.material.icons.rounded.SentimentSatisfied
import androidx.compose.material.icons.rounded.SentimentVeryDissatisfied
import androidx.compose.material.icons.rounded.SentimentVerySatisfied
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coloredShadow
import com.example.foodapp.R
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.Review
import com.example.foodapp.presentation.state.CompleteUiState
import com.example.foodapp.ui.screen.main.complete.section.BottomCompleteBar
import com.example.foodapp.ui.screen.main.complete.section.HeaderCompleteSection
import com.example.foodapp.ui.screen.main.complete.section.MessageCompleteSelection
import com.example.foodapp.ui.screen.main.complete.section.PreviewTagSelection
import com.example.foodapp.ui.screen.main.complete.section.RatingCompleteSection
import com.example.foodapp.ui.screen.main.complete.section.TopCompleteBar
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.BrightOrange
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.Yellow0

@Composable
fun CompleteTab(
    onNavigationToBack: () -> Unit,
    completeUiState: CompleteUiState,
    onAddPreviewTag: (String) -> Unit,
    onRemovePreviewTag: (String) -> Unit,
    onChangedPrivate: (Boolean) -> Unit,
    onChangedMessage: (String) -> Unit,
    onChangedRating: (Int) -> Unit,
    onCreateComplete: () -> Unit,

    ) {

    Log.d("check_value_complete", "preview tag = ${completeUiState.previewTags}")
    Log.d("check_value_complete", "private =  ${completeUiState.isPrivateName}")
    Log.d("check_value_complete", "message = ${completeUiState.message}")
    Log.d("check_value_complete", "rating = ${completeUiState.rating}")

    val screenH = LocalConfiguration.current.screenHeightDp
    val screenW = LocalConfiguration.current.screenWidthDp

    Box(
        modifier = Modifier
            .fillMaxSize()

            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to BrightOrange, 0.50f to Color.White, 1f to Color.White
                    )
                )
            )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color.White.copy(0.65f),
                        0.60f to Color.White,
                    )
                )
            )
    ) {
        Scaffold(
            topBar = {
                TopCompleteBar(
                    onNavigationToBack
                )
//                onNavBack = onNavBack,

            }, bottomBar = {
                BottomCompleteBar(
                    uiState = completeUiState,
                    onChangedPrivate = onChangedPrivate,
                    onCreateComplete = onCreateComplete,
                )
            }, containerColor = Color.Transparent


        ) { paddingValues ->


            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    HeaderCompleteSection(
                        uiState = completeUiState
                    )
                }

                item {
                    Spacer(Modifier.height(10.dp))
                }
                item {
                    RatingCompleteSection(
                        uiState = completeUiState,
                        onChangedRating = onChangedRating
                    )
                }

                item {
                    Spacer(Modifier.height(5.dp))
                }
                item {
                    PreviewTagSelection(
                        uiState = completeUiState,
                        onAddPreviewTag = onAddPreviewTag,
                        onRemovePreviewTag = onRemovePreviewTag
                    )
                }

                item {
                    Spacer(Modifier.height(5.dp))
                }

                item {
                    MessageCompleteSelection(
                        uiState = completeUiState,
                        onChangedMessage = onChangedMessage
                    )
                }

            }
        }

    }
}





@Preview
@Composable
fun PreviewCompleteTab() {
    CompleteTab(
//        onNavBack = {},
        onNavigationToBack = {},
        completeUiState = CompleteUiState(
            restaurantName = "Nước ép Trinh Trinh - Thới Hòa",
            rating = 5,
            previewTags = listOf(
                "Ngon đỉnh", "Giá phải chăng"
            ),
            message = "Ngon lam",
            isPrivateName = true
        ),
        onAddPreviewTag = {},
        onRemovePreviewTag = {},
        onChangedRating = {},
        onChangedMessage = {},
        onChangedPrivate = {},
        onCreateComplete = {},
    )
}
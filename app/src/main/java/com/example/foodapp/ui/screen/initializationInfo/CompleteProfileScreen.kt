package com.example.foodapp.ui.screen.initializationInfo

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.domain.model.User
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.ui.screen.initializationInfo.progressStep.GenderTab
import com.example.foodapp.ui.screen.initializationInfo.progressStep.NameStep
import com.example.foodapp.ui.screen.initializationInfo.progressStep.PhoneTab
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.DefaultBg1
import com.example.foodapp.ui.theme.Gray65
import kotlinx.coroutines.delay


@Composable
fun CompleteProfileScreen(
    onLoggedIn: (updateUser: User) -> Unit,
    profileUiState: ProfileUiState,
    updateUserProfile: () -> Unit,
    onFieldEditProfileChange: (field: String, value: String) -> Unit,
    onNextStep: () -> Unit,
    onPrevious: () -> Unit,
    setClickedUpdate: () -> Unit,
    resetClickedUpdate: () -> Unit,
    validatePhone: () -> Boolean,
    validateName: () -> Boolean,
    setGender: (String) -> Unit,
) {


    //delay de doi load xong effect
    LaunchedEffect(updateUserProfile) {
        delay(1500)
    }

    val progressItems by remember { mutableStateOf(listOf(1, 2, 3)) }
    var validStep by remember { mutableStateOf<Int>(-1) }

    Log.d("CompleteProfileScreen", "profileUiState = ${profileUiState}")
    Log.d("CompleteProfileScreen", "updateUserProfile = ${updateUserProfile}")
    Log.d(
        "CompleteProfileScreen",
        "onFieldEditProfileChange = ${onFieldEditProfileChange}"
    )

    Log.d("check_valid_step", "local current ${profileUiState.currentStep}")
    Scaffold(
        topBar = {
            TopInitializationInfoBar(
                profileUiState = profileUiState,
                onPrevious = onPrevious,
                validStep = {
                    if (validStep > -2) {
                        validStep -= 1
                    }
                }
            )
        },
        containerColor = Color.White,
    ) { paddingValues ->

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues)

        ) {

            Spacer(Modifier.height(20.dp))

            //progress


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            ) {

                progressItems.forEach { index ->

                    val color by animateColorAsState(
                        targetValue = if (profileUiState.currentStep >= index - 1)
                            Blue1
                        else
                            Gray65,
                        animationSpec = tween(
                            durationMillis = 1500
                        )
                    )


                    AnimatedContent(
                        targetState = validStep >= index - 1,
                        transitionSpec = {
                            fadeIn() + scaleIn() togetherWith
                                    fadeOut() + scaleOut()
                        },
                        label = ""
                    ) { isValid ->
                        Log.d("check_valid_step_check", "current  $validStep")
                        Icon(
                            imageVector = if (isValid)
                                Icons.Rounded.Check
                            else
                                Icons.Rounded.Circle,
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    color,
                                    CircleShape
                                )
                                .padding(
                                    if (isValid)
                                        2.dp
                                    else
                                        10.dp
                                ),

                            tint = Color.White
                        )
                    }


                    //icon
//                    Icon(
//                        Icons.Rounded.Check,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .background(
//                                iconColor,
//                                CircleShape
//                            )
//                            .padding(vertical = 3.dp, horizontal = 3.dp),
//                        tint = Color.White
//                    )

                    if (index != 3) {
                        Spacer(
                            modifier = Modifier
                                .width(50.dp)
                                .height(2.dp)
                                .background(color)
                        )
                    }
                }
            }
            when (profileUiState.currentStep) {

                0 ->

                    NameStep(
                        onFieldEditProfileChange = onFieldEditProfileChange,
                        onNextStep = onNextStep,
                        profileUiState = profileUiState,
                        resetClickedUpdate = resetClickedUpdate,
                        setClickedUpdate = setClickedUpdate,
                        validateName = validateName,
                        onValidStep = {
                            if (validStep <= 2) {
                                validStep += 1
                            }
                            Log.d(
                                "check_valid_step_check",
                                "name step: ${profileUiState.currentStep}"
                            )
                        },
                    )

                1 -> {
                    PhoneTab(
                        validatePhone = validatePhone,
                        onNextStep = onNextStep,
                        profileUiState = profileUiState,
                        onFieldEditProfileChange = onFieldEditProfileChange,
                        setClickedUpdate = setClickedUpdate,
                        resetClickedUpdate = resetClickedUpdate,
                        onValidStep = {
                            if (validStep <= 2) {
                                validStep += 1
                            }
                            Log.d(
                                "check_valid_step_check",
                                "phone tab validStep: ${profileUiState.currentStep}"
                            )
                        },
                    )
                }

                2 -> {
                    Log.d("check_valid_step", "nav gender")
                    GenderTab(
                        onValidStep = {
                            if (validStep < 2) {
                                validStep += 1
                            }
                            Log.d("check_valid_step", validStep.toString())
                        },
                        onLoggedIn = onLoggedIn,
                        updateUserProfile = updateUserProfile,
                        profileUiState = profileUiState,
                        setClickedUpdate = setClickedUpdate,
                        setGender = setGender,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopInitializationInfoBar(
    profileUiState: ProfileUiState,
    onPrevious: () -> Unit,
    validStep: () -> Unit,
) {

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        CenterAlignedTopAppBar(
            navigationIcon = {
                if (profileUiState.currentStep > 0) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                onPrevious()
                                validStep()
                            }
                    )
                }
            },
            title = {
                Text(
                    text = "Nhập thông tin tài khoản",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black.copy(0.1f))
        )
    }
}


@Preview
@Composable
fun PreviewInitializationInfoTab() {
    CompleteProfileScreen(
        profileUiState = ProfileUiState(currentStep = 0),
        updateUserProfile = {},
        onFieldEditProfileChange = { _, _ -> Unit },
        onPrevious = {},
        onNextStep = {},
        resetClickedUpdate = {},
        setClickedUpdate = { },
        validateName = { false },
        validatePhone = { false },
        setGender = { String },
        onLoggedIn = {},

        )
}
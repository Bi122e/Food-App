package com.example.foodapp.ui.screen.main.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
 import com.example.foodapp.ui.screen.main.profile.section.info.DeleteAccSection
import com.example.foodapp.ui.screen.main.profile.section.info.EmailInfoSelection
import com.example.foodapp.ui.screen.main.profile.section.info.GenderSection
import com.example.foodapp.ui.screen.main.profile.section.info.NameInfoSelection
import com.example.foodapp.ui.screen.main.profile.section.info.PhoneInfoSelection
import com.example.foodapp.ui.screen.main.profile.section.info.TopBarInfoSelection
import com.example.foodapp.ui.screen.main.profile.section.info.UpdateInfoSelection
import com.example.foodapp.ui.screen.shared.LoadingScreen
import com.example.foodapp.ui.screen.shared.SnackBar
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray65

@SuppressLint("ContextCastToActivity")
@Composable
fun InfoRoute(
    onNavigationToBack: () -> Unit,
) {

    val activity = LocalContext.current as ComponentActivity
    val userProfileViewModel: UserProfileViewModel = hiltViewModel(activity)
    val profileUiState by userProfileViewModel.uiStateProfile.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(
        Unit
    ) {
        userProfileViewModel.loadCurrentUser()
    }

    var snackBarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        userProfileViewModel.eventSuccess.collect { event ->
            Log.d("SnackTest", "event = $event")
            snackBarMessage = event
        }
    }

    when {

        profileUiState.user != null -> {

            Log.d("check_update_gender", "${profileUiState.editProfile}")


            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                InfoTab(
                    uiState = profileUiState,
                    onGenderChange = { gender ->
                        userProfileViewModel.onFieldEditProfileChange("gender", gender)
                    },
                    onExpandedChange = userProfileViewModel::onExpandedChange,
                    setExpandedChange = userProfileViewModel::setExpandedChange,
                    onValidProfile = userProfileViewModel::validateProfile,
                    onNameChange = { name ->
                        userProfileViewModel.onFieldEditProfileChange("name", name)
                    },
                    setClickedUpdate = userProfileViewModel::setClickedUpdate,
                    resetClickedUpdate = userProfileViewModel::resetClickedUpdate,
                    onPhoneNumberChange = {
                        userProfileViewModel.onFieldEditProfileChange("phone", it)
                    },
                    onEmailChange = {
                        userProfileViewModel.onFieldEditProfileChange("email", it)
                    },
                    onNavigationToBack = onNavigationToBack,
                    onUpdateUserProfile = userProfileViewModel::updateUserProfile
                )

                SnackBar(
                    onTurnOffSnackBar = {
                        snackBarMessage = null
                    },
                    showMessage = snackBarMessage != null,
                    message = snackBarMessage ?: "...",
                )

            }
        }

        profileUiState.isLoading -> {
            showToast(context = context, "loading")
            LoadingScreen()
        }

    }

}

@Composable
fun InfoTab(
    uiState: ProfileUiState,
    onGenderChange: (String) -> Unit,
    onExpandedChange: () -> Unit,
    onValidProfile: () -> Map<String, String>,
    setExpandedChange: () -> Unit,
    onNameChange: (String) -> Unit,
    setClickedUpdate: () -> Unit,
    resetClickedUpdate: () -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onNavigationToBack: () -> Unit,
    onUpdateUserProfile: () -> Unit,
) {
    Scaffold(
        topBar = { TopBarInfoSelection(onNavigationToBack = onNavigationToBack) },
        containerColor = Color.White,

        ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item { }

            item {
                NameInfoSelection(
                    uiState = uiState,
                    onNameChange = onNameChange,
                    hasError = uiState.errorMessage,
                    isClickUpdate = uiState.isClickedUpdate,
                    resetClickedUpdate = resetClickedUpdate,
                )
            }


            //gender
            item {

                GenderSection(
                    expanded = uiState.expanded,
                    onExpandedChange = {
                        onExpandedChange()
                    },
                    onSelectedGenderChange = onGenderChange,
                    gender = uiState.editProfile.gender,
                    setExpandedChange = setExpandedChange,
                    isClickedUpdate = uiState.isClickedUpdate,
                    resetClickedUpdate = resetClickedUpdate,

                    )
            }
            //phone
            item {
                PhoneInfoSelection(
                    resetClickedUpdate = resetClickedUpdate,
                    onPhoneNumberChange = onPhoneNumberChange,
                    phoneNumber = uiState.editProfile.phone,
                    hasError = uiState.errorMessage,
                    isClickedUpdate = uiState.isClickedUpdate,
                )
            }

            //email
            item {
                EmailInfoSelection(
                    uiState = uiState,
                    onEmailChange = onEmailChange,
                    hasError = uiState.errorMessage,
                    isClickUpdate = uiState.isClickedUpdate,
                    resetClickedUpdate = resetClickedUpdate,
                )
            }

            item {
                Spacer(Modifier.height(10.dp))
            }
            item {
                // setClickedUpdate: () -> Unit,
                //    isSavedEnable: Boolean,
                //    hasError: Map<String, String?>,

                UpdateInfoSelection(
                    setClickedUpdate = {
                        setClickedUpdate()
                        val error = onValidProfile()
                        if (error.isEmpty()) {
                            Log.d(
                                "test_flow_updated_",
                                "test o info error mty, ${uiState.errorMessage}"
                            )
                            onUpdateUserProfile()
                        }
                    },
                    isSavedEnable = uiState.isSavedEnable,

                    )
            }

            item {
                DeleteAccSection()
             }

        }
    }


}


@Preview(showBackground = true)
@Composable
fun InfoPreview() {

    InfoTab(
        uiState = ProfileUiState(),
//        updateUserProfile = {},
        onGenderChange = { _ -> Unit },
        onExpandedChange = {},
        setExpandedChange = {},
        onValidProfile = { mapOf() },
        onNameChange = {},
        setClickedUpdate = {},
        resetClickedUpdate = {},
        onPhoneNumberChange = {},
        onEmailChange = {},
        onNavigationToBack = {},
        onUpdateUserProfile = {},
    )
    SnackBar(
        onTurnOffSnackBar = {
         },
        showMessage = true,
        message = "fd"
    )

}
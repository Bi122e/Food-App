package com.example.foodapp.ui.screen.home.tab

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.presentation.state.CheckoutUiState
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray65


@Composable
fun PaymentTab(
    onNavBack: () -> Unit,
    checkoutUiState: CheckoutUiState,
    onSelectPayment: (PaymentMethod) -> Unit,
) {

    Scaffold(
        topBar = {
            TopPaymentBar(onNavBack = onNavBack)
        },
        containerColor = Color.White,
    ) { paddingValues ->



        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {

            item {
                Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Phương thức khác",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                        )
                }
//                val img = Image(imageVector = )

            items(PaymentMethod.entries) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {onSelectPayment(item)}
                            )
                    ) {
                        Log.d("checkoutUiState.paymentMethod", checkoutUiState.paymentMethod.toString())
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(Modifier.width(10.dp))

                            Text(
                                text = item.displayName,
                            )

                            Spacer(Modifier.weight(1f))

                            Box(
                                modifier = Modifier
                                    .border(
                                        width = if (checkoutUiState.paymentMethod == item) 7.dp else 2.dp,
                                        color = if (checkoutUiState.paymentMethod == item) Blue1 else Gray65,
                                        shape = CircleShape
                                    )
                                    .size(24.dp)
                            )


                        }
                    }
                }

            }
        }
    }


@Preview (showBackground = true)
@Composable
fun PreviewPayment() {
    PaymentTab(
        onNavBack = {},
        checkoutUiState = CheckoutUiState(),
        onSelectPayment = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopPaymentBar(
    onNavBack: () -> Unit
) {
    Column()
    {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            title = {
                Text(
                    text = "Phương thức thanh toán",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onNavBack()
                    }
                )
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray100)
        )
    }

}
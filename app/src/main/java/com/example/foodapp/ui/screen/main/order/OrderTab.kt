package com.example.foodapp.ui.screen.main.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.presentation.state.OrderUiState
import com.example.foodapp.ui.fakeData.PreviewDataOrderState
import com.example.foodapp.ui.screen.main.order.section.DriverOrderSelection
import com.example.foodapp.ui.screen.main.order.section.HeaderOrderSection
import com.example.foodapp.ui.screen.main.order.section.InformationOrderSection
import com.example.foodapp.ui.screen.main.order.section.SummaryOrderSection
import com.example.foodapp.ui.screen.shared.LoadingScreen
import com.example.foodapp.ui.theme.Gray85
import kotlinx.coroutines.delay

@Composable
fun OrderTab(
    orderUiState: OrderUiState, //lấy order có trong fb về
    orderId: String, //lấy orderId khi chuyển màn để so sánh với data fb
) {
    var detailState by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
//     var loading by remember {
//        mutableStateOf(true)
//    }

    /*
    flow chạy launched 1 lần delay 10s, nếu order có dữ liệu và launched chạy xong thì flow
    chạy xuống, ko thì chạy loading đến khi có dữ liệu
    * */
    var timerFinished by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
//        if (orderUiState.order.isNotEmpty()) {
        delay(500)
        timerFinished = true
//        }
    }
    var order = if (!isPreview) {
        orderUiState.order.find {
            it.orderId == orderId
        }
    } else {
        PreviewDataOrderState.previewOrder()
    }


//    val loading = !timerFinished && order == null
    val loading = !timerFinished || order == null

    if (loading) {
        LoadingScreen()
        Log.d("Check_Order_State", orderUiState.order.toString())
        Log.d("Check_Order_State", "timerFinished $timerFinished")
        return
    }
    Log.d("Check_Order_State", "loading = $loading")




    Scaffold(
        topBar = {
            TopBarOrderDetail()
        },
        containerColor = Color(0xFFe1edea)
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues, modifier = Modifier
                .fillMaxSize()
//                .padding(horizontal = 16.dp, vertical = 10.dp),
                .padding(start = 16.dp, end = 16.dp, top = 10.dp),

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (order == null) {
                return@LazyColumn
            }
            item {
                HeaderOrderSection(
                    order = order
                )
            }

            item {
                DriverOrderSelection(order = order)
            }


            //information
            item {
                InformationOrderSection(
                    order = order
                )
            }


            //summary
            item {
                SummaryOrderSection(order = order, detailState = detailState) {
                    detailState = !detailState
                }
             }
            item {
                Spacer(Modifier.padding(bottom = 15.dp))
            }

            item {
//                SuccessOrderSection(
//                    order = order
//                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
     OrderTab(
        orderUiState = OrderUiState(),
        orderId = "sdfs",
    )
//    LoadingScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarOrderDetail() {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.background(Color.White)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Chi tiết đơn hàng", fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), navigationIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack, contentDescription = null
                )
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,

                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Gray85)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)

    )
}





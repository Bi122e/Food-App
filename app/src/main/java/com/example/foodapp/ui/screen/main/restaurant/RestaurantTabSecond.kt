package com.example.foodapp.ui.screen.main.restaurant

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R

import com.example.foodapp.ui.theme.White
import org.w3c.dom.Text

@Composable
fun RestaurantTabSecond(
) {



    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = White
    ) { paddingValue ->

        Column(modifier = Modifier.padding(paddingValue)) {

            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_empty_food),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )

                    Text(
                        text ="Hiện chưa cập nhật",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp

                        )
                }

            }


        }
    }






}


@Preview(showBackground = true)
@Composable
fun RestaurantTabSecondPreview() {
    RestaurantTabSecond()
}


package com.example.foodapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CounterContent(
    value: Int,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
) {
    Box(
        Modifier.fillMaxSize(),
        Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                14.dp,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onMinus) {
                Text("-")
            }
            Text(value.toString())
            Button(onClick = onPlus) {
                Text("+")
            }
        }
    }
}
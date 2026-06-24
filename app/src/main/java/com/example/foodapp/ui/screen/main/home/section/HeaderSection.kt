package com.example.foodapp.ui.screen.main.home.section

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.core.utils.displayAddress

@Composable
fun HeaderSection(
    address: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            tint = Color.White,
            modifier = Modifier.size(32.dp),
            contentDescription = null
        )
        Spacer(Modifier.width(4.dp))


        Text(
            text = address.displayAddress(),
            maxLines = 1,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
        Spacer(Modifier.weight(1f))

        IconButton(onClick = {}) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Notifications,
                tint = Color.White,
                contentDescription = null,
            )
        }

    }
}

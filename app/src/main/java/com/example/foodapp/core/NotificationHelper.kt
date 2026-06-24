package com.example.foodapp.core

import android.Manifest
import android.R
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.foodapp.ui.activity.MainActivity

object NotificationHelper {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showOrderCompleted(
        context: Context,
        orderId: String
    ) {

        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("orderId", orderId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "order_channel")
            .setSmallIcon(R.drawable.btn_radio)
            .setContentTitle("Đơn hàng đã hoàn thành")
            .setContentText("Hãy đánh giá đơn hàng")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(1001, notification)
    }
}
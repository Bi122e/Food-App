package com.example.foodapp.service

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.foodapp.R
import com.example.foodapp.core.NotificationHelper
import com.example.foodapp.ui.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM_RECEIVED", "ONNEW $token") //goi token de luu
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM_RECEIVED", "Message receivedONEFF")

        Log.d("FCM_TITLE", "TITTLE ${message.notification?.title}")
        Log.d("FCM_BODY", "BODY ${message.notification?.body }")
        Log.d("FCM_SUCCESS", "FCM_SUCCCS ${message.data}")

        when (message.data["type"]) {

            "ORDER_COMPLETED" -> {

                NotificationHelper.showOrderCompleted(
                    context = this,
                    message.data["orderId"] ?: "________"
                )
            }
        }
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showOrderCompletedNotification(orderId: String) {

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("orderId", orderId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            this,
            "order_channel"
        )
            .setSmallIcon(R.drawable.ic_check2)
            .setContentTitle("Đơn hàng đã hoàn thành")
            .setContentText("Hãy đánh giá đơn hàng")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this)
            .notify(1001, notification)
    }




}



package com.example.exactalarmdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat


class MyNotificationPublisher: BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("lol", " received")
        val message = intent.getStringExtra("textExtra").toString()
        val title = intent.getStringExtra("titleExtra").toString()
        val notification =
            NotificationCompat.Builder(context, "channelID1001").setSmallIcon(androidx.core.R.drawable.notification_bg)
                .setContentText(message).setContentTitle(title).build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, notification)
    }
}
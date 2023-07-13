package com.example.exactalarmdemo

import android.app.*
import android.content.ContentValues
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.text.format.DateFormat
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnDate: Button = findViewById(R.id.btDate)
        val intent = Intent()
        val packageName = packageName
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }
        btnDate.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val calendar = Calendar.getInstance()
                //10 is for how many seconds from now you want to schedule also you can create a custom instance of Callender to set on exact time
                calendar.add(Calendar.MINUTE, 20)
                //function for Creating [Notification Channel][1]
                createNotificationChannel()
                //function for scheduling the notification
                val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    when {
                        // If permission is granted, proceed with scheduling exact alarms.
                        alarmManager.canScheduleExactAlarms() -> {
                            scheduleNotification(calendar)
                        }
                        else -> {
                            // Ask users to go to exact alarm page in system settings.
                            startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                        }
                    }
                } else{
                    scheduleNotification(calendar)
                }

            }
        }
    }

    fun scheduleNotification(calendar: Calendar) {
        Log.d(ContentValues.TAG, "scheduleAlarm:current time ${System.currentTimeMillis()}")
        Log.d(ContentValues.TAG, "scheduleAlarm:current time ${calendar.timeInMillis }")
        val timeinmilli = calendar.timeInMillis //will give tou time in ms


        val formatter = SimpleDateFormat("dd/MM/yyyy") //format of the date, choose what you want


        val resultdate = Date(timeinmilli) //create an object date associated to the time in ms

        Log.e("lol", resultdate.toString())


        val newTime: Long

        val intent = Intent(applicationContext, MyNotificationPublisher::class.java)
        intent.putExtra("titleExtra", "Dynamic Title")
        intent.putExtra("textExtra", "Dynamic Text Body")
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        Toast.makeText(applicationContext, "Scheduled ", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val id = "channelID1001"
        val name = "Daily Alerts"
        val des = "Channel Description A Brief"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)
        channel.description = des
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
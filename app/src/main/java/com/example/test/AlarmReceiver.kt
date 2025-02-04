package com.example.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm received for task")
        val taskName = intent?.getStringExtra("taskName") ?: "Задача"

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("taskName", taskName)
        }
        context?.startForegroundService(serviceIntent)
    }
}

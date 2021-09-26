package com.example.task1new.app.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LocationTrackingService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
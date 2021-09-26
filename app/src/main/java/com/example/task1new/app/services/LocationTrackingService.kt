package com.example.task1new.app.services

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.task1new.R
import java.lang.Exception

class LocationTrackingService: Service(), LocationListener {

    companion object {
        const val SERVICE_ID = 19291832
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 0
        const val MIN_TIME_BW_UPDATES = (1000 * 10 * 1).toLong()
        const val NEW_LOCATION_ACTION = "NEW_LOCATION_ACTION"
    }

    var mCheckIsGPSTurnedOn = false
    var mCheckNetworkIsTurnedOn = false
    var mCanGetLocation = false

    var mLocation: Location? = null
    var mLatitude = 0.0
    var mLongitude = 0.0

    protected var mLocationManager: LocationManager? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (!intent.hasExtra("kill_self")) {
                initLocationScan()
                initNotification()
            } else {
                killSelf()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun initNotification() {
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.foreground_channel_name)
            val importance = NotificationManager.IMPORTANCE_MIN
            val mChannel = NotificationChannel(
                getString(R.string.foreground_channel_id), name, importance
            )
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(
                NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        // Create notification builder.
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this, getString(
                R.string.foreground_channel_id
            )
        )

        // Make notification show big text.
        val bigTextStyle: NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(getString(R.string.foreground_notification_name))

        // Set big text style.
        builder.setStyle(bigTextStyle)

        builder.setWhen(System.currentTimeMillis())

        // Make the notification max priority.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.priority = NotificationManager.IMPORTANCE_MIN
        } else {
            builder.priority = Notification.PRIORITY_MIN
        }
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true)

        // Add Play button intent in notification.
        // Build the notification.

        // Add Play button intent in notification.
        // Build the notification.
        val notification: Notification = builder.build()

        // Start mForeground service.
        startForeground(SERVICE_ID, notification)
    }

    private fun initLocationScan(): Location? {
        try {
            mLocationManager =
                applicationContext?.getSystemService(LOCATION_SERVICE) as LocationManager
            // get GPS status
            mCheckIsGPSTurnedOn =
                mLocationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
            // get network provider status
            mCheckNetworkIsTurnedOn =
                mLocationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
            if (!mCheckIsGPSTurnedOn) {
                Log.e("hz", "GPS turned off")
            } else {
                mCanGetLocation = true
                // if GPS Enabled get lat/long using GPS Services
                applicationContext?.let {
                    if (mCheckIsGPSTurnedOn) {
                        if (ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            mLocationManager?.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                            )
                            if (mLocationManager != null) {
                                mLocation =
                                    mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                if (mLocation != null) {
                                    mLatitude = mLocation?.latitude ?: 0.0
                                    mLongitude = mLocation?.longitude ?: 0.0
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mLocation
    }

    private fun stopListening() {
        if (mLocationManager != null) {
            mLocationManager?.let { manager ->
                applicationContext?.let { context ->
                    manager.removeUpdates(this@LocationTrackingService)
                }
            }
        }
    }

    private fun killSelf() {
        stopListening()
        stopForeground(true)
        stopSelf()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        killSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        val intent = Intent()
        intent.action = NEW_LOCATION_ACTION
        intent.putExtra("lat", location.latitude)
        intent.putExtra("long", location.longitude)
        sendBroadcast(intent)
    }

}
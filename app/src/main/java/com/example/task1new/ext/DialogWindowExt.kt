package com.example.task1new.ext

import android.app.Dialog
import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Dialog.applyImmersiveMode() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
        this.window?.decorView?.windowInsetsController.let { controller ->
            this.window?.let {
                WindowInsetsControllerCompat(it, it.decorView).apply {
                    this.hide(WindowInsetsCompat.Type.statusBars())
                    this.hide(WindowInsetsCompat.Type.navigationBars())
                    this.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        }
    } else {
        @Suppress("DEPRECATION")
        this.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                //or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}

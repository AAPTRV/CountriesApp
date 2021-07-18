package com.example.task1new.ext

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

fun Activity.showSimpleDialog() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Country details")
    builder.setMessage("Your are on country details fragment")
    builder.setPositiveButton("Ok") { dialog, which ->
        dialog.dismiss()
    }
    val dialog = builder.create()
    dialog.show()
}
package com.example.data.ext

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

fun Activity.showSimpleDialogNetworkError() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Network error")
    builder.setMessage("Unable to load data from network")
    builder.setPositiveButton("Ok") { dialog, which ->
        dialog.dismiss()
    }
    val dialog = builder.create()
    dialog.show()
}
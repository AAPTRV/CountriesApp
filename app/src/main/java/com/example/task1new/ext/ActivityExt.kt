package com.example.task1new.ext

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity.BOTTOM
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1new.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


val DIALOG_WIDTH_DELTA_7: Float = 0.7F

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text resource.
 */

private fun createDialog(activity: Activity): Dialog {
    val dialog = Dialog(activity)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.let {
        it.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                //                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
    return dialog
}

private fun Activity.initBaseOneButtonContent(
    title: String?,
    description: String?
): Pair<Dialog, View> {
    val dialog = createDialog(this)
    dialog.setCanceledOnTouchOutside(false)
    val contentView = LayoutInflater.from(this)
        .inflate(R.layout.dialog_with_one_button, null)

    val tvTitle: TextView = contentView.findViewById(R.id.tvTitle)
    title?.let {
        tvTitle.text = it
        tvTitle.visibility = View.VISIBLE
    }

    val tvDescription: TextView = contentView.findViewById(R.id.tvDescription)
    description?.let {
        tvDescription.text = it
        tvDescription.visibility = View.VISIBLE
    }
    return Pair(dialog, contentView)
}

private fun setContentView(dialog: Dialog, contentView: View) {
    dialog.setContentView(contentView)
    val window = dialog.window
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val resources = dialog.context
        .resources
    val params = contentView.layoutParams as FrameLayout.LayoutParams
    params.width = ((resources.displayMetrics.widthPixels * DIALOG_WIDTH_DELTA_7).toInt())
    contentView.layoutParams = params
}

fun Activity.showDialogWithOneButton(
    title: String?, description: String?,
    @StringRes leftButtonTextId: Int,
    leftClickListener: View.OnClickListener?
): Dialog {
    val (dialog, contentView) = initBaseOneButtonContent(title, description)

    val btnLeft: Button = contentView.findViewById(R.id.btnOk)
    btnLeft.setText(leftButtonTextId)
    btnLeft.setOnClickListener {
        dialog.dismiss()
        leftClickListener?.onClick(it)
    }

    setContentView(dialog, contentView)
    if (!this.isFinishing) {
        dialog.show()
    }
    return dialog
}

fun Activity.showAlertDialog() {
    val alertDialog = AlertDialog.Builder(this)
        .setTitle(getString(R.string.error))
        .setMessage(getString(R.string.error_default))
        .setPositiveButton(getString(R.string.dialog_ok)) { dialog, _ ->
            dialog.dismiss()
        }
    alertDialog?.show()
}

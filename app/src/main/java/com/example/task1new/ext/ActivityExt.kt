package com.example.task1new.ext

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.StringRes
import com.example.task1new.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


val DIALOG_WIDTH_DELTA_7: Float = 0.7F

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text resource.
 */

fun Activity.showAlertDialog() {
    val alertDialog = MaterialAlertDialogBuilder(this)
        .setTitle("Alert")
        .setMessage("Alert message to be shown")
        .setBackground(resources.getDrawable(R.drawable.ic_launcher_background))
        .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        .setNegativeButton("NO") { dialog, which -> dialog.dismiss() }
    alertDialog.show()
}

private fun createDialog(activity: Activity): Dialog {
    val dialog = Dialog(activity)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.applyImmersiveMode()

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
    window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
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

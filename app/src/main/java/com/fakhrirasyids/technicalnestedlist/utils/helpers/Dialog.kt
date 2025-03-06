package com.fakhrirasyids.technicalnestedlist.utils.helpers

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fakhrirasyids.technicalnestedlist.R
import com.fakhrirasyids.technicalnestedlist.utils.enums.DialogType
import com.google.android.material.button.MaterialButton

class Dialog(private val context: Context) {

    private var alertDialog: AlertDialog? = null

    fun showDialog(
        dialogType: DialogType,
        message: String = "",
        onDismiss: (() -> Unit)? = null
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_custom_dialog, null)

        val ivSentiment = dialogView.findViewById<ImageView>(R.id.iv_sentiment)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tv_message)
        val btnClose = dialogView.findViewById<MaterialButton>(R.id.btn_close)

        ivSentiment.setImageDrawable(
            ContextCompat.getDrawable(
                context, when (dialogType) {
                    DialogType.INFO -> R.drawable.ic_happy
                    else -> R.drawable.ic_sad
                }
            )
        )

        tvMessage.text = message

        alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnClose.setOnClickListener {
            onDismiss?.invoke()
            hideDialog()
        }

        alertDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog?.show()

    }

    private fun hideDialog() {
        alertDialog?.dismiss()
        alertDialog = null
    }
}

package com.app.loanserviceapp.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.app.loanserviceapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Simple api for AlertDialog in android so that
 * it will be centralized throughout application
 * any API changes would be easier to maintain
 * */
class AlertManagerUtils {
    companion object {
        private const val ALERT_TITLE = R.string.alert_manager_alert_title
        private const val DEFAULT_POSITIVE_BUTTON = R.string.alert_manager_positive_button
        private const val DEFAULT_NEGATIVE_BUTTON = R.string.alert_manager_negative_button
        private fun getAlertDialogBuilder(context: Context): AlertDialog.Builder {
            return MaterialAlertDialogBuilder(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar)
        }

        /**
         * This method will expose many of the features that a alert dialog can show
         * @param context Context is the application context from where alert dialog will be called
         * @param title (Optional) if it is not provided then it will diSplay ALERT_TITLE
         * @param message (Optional) if it is not provided then it will diSplay nothing
         * @param isPosButtonEnabled (optional)default value is false if true then positive button will be shown
         * @param isNegativeButtonEnabled (optional) default is false if true then negative button will be shown
         * @param negText (optional) Negative button text if not then it will show the default one DEFAULT_NEGATIVE_BUTTON
         * @param posText (Optional) Positive button text if not then it will shown the default one DEFAULT_POSITIVE_BUTTON
         * @param posListener (optional) functional parameter to execute specific function after clicking into the positive button else it will dismiss the dialog
         * @param negListener (optional) functional parameter to execute specific function after clicking into the negative button else it will dismiss the dialog
         * @param options (Optional) if passed then it will show the items instead of message
         * @param optionCallBack (Optional) if passed then it will invoke the function to handle the item actions
         * */
        fun showAlertDialog(
            context: Context,
            title: String? = null,
            message: String? = null,
            isPosButtonEnabled: Boolean = false,
            isNegativeButtonEnabled: Boolean = false,
            isCancelable: Boolean = false,
            negText: String? = null,
            posText: String? = null,
            posListener: ((dialog: DialogInterface) -> Unit)? = null,
            negListener: ((dialog: DialogInterface) -> Unit)? = null,
            options: Array<String>? = null,
            optionCallBack: ((dialog: DialogInterface, item: Int) -> Unit)? = null
        ): AlertDialog {
            val alertBuilder = getAlertDialogBuilder(context).apply {
                setTitle(title ?: context.resources.getString(ALERT_TITLE))
                setMessage(message)
                if (options != null) {
                    setItems(options) { dialog, item ->
                        optionCallBack?.invoke(dialog, item)
                    }
                }
                if (isPosButtonEnabled) {
                    setPositiveButton(posText ?: context.resources.getString(DEFAULT_POSITIVE_BUTTON)) { dialog, _ ->
                        if (posListener != null) posListener.invoke(dialog) else dialog.dismiss()
                    }
                }
                if (isNegativeButtonEnabled) {
                    setNegativeButton(negText ?: context.resources.getString(DEFAULT_NEGATIVE_BUTTON)) { dialog, _ ->
                        if (negListener != null) negListener.invoke(dialog) else dialog.dismiss()
                    }
                }
                setCancelable(isCancelable)
            }
            val alert = alertBuilder.create()
            alert.show()
            return alert
        }
    }
}

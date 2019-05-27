package com.riteshakya.core.extension

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.riteshakya.core.platform.BaseActivity
import com.riteshakya.core.platform.BaseFragment

/**
 * Show snackbar for success and normal messages
 * @param length duration of message, default Snackbar.LENGTH_LONG
 */
fun BaseActivity.showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
    when {
        window.decorView.rootView != null -> {
            val snackbar = Snackbar.make(window.decorView.rootView, message, length)

            snackbar.view.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_green_dark)
            )

            val tvSnackbar =
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            tvSnackbar.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            tvSnackbar.maxLines = 3
            snackbar.show()
        }
    }
}

/**
 * Show snackbar for error messages
 * @param length duration of message, default Snackbar.LENGTH_LONG
 */
fun BaseActivity.showError(message: String, length: Int = Snackbar.LENGTH_LONG) {
    when {
        window.decorView.rootView != null -> {
            val snackbar = Snackbar.make(window.decorView.rootView, message, length)

            snackbar.view.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_red_dark)
            )

            val snackbarTxt =
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackbarTxt.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            snackbarTxt.maxLines = 3
            snackbar.show()
        }
    }
}

/**
 * Show snackbar for error messages
 * @param length duration of message, default Snackbar.LENGTH_LONG
 */
fun BaseFragment.showError(message: String, length: Int = Snackbar.LENGTH_LONG) {
    when {
        activity != null && activity!!.window.decorView.rootView != null -> {
            val snackbar = Snackbar.make(activity!!.window.decorView.rootView, message, length)

            snackbar.view.setBackgroundColor(
                ContextCompat.getColor(activity!!, android.R.color.holo_red_dark)
            )

            val snackbarTxt =
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackbarTxt.setTextColor(ContextCompat.getColor(activity!!, android.R.color.white))
            snackbarTxt.maxLines = 3
            snackbar.show()
        }
    }
}
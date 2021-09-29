package com.example.mycinemaapp.view

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.show() {
    this.visibility = View.VISIBLE
}

fun  View.hide() {
    this.visibility = View.GONE
}

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar
        .make(
            this,
            text,
            Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText) { action(this) }
        .show()
}
package br.com.mapsmarker.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

fun Activity.closeKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun TextView.clearText() {
    this.text = ""
}

fun View.display(status: Boolean) {
    this.visibility = if (status) View.VISIBLE else View.GONE
}

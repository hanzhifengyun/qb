package com.hzfy.ui.view

import android.app.Service
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}



fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setVisible(isVisible: Boolean) {
    if (isVisible) toVisible() else toGone()
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.setInputTypeHidden() {
    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    this.setSelection(this.text.toString().length);
}

fun EditText.setInputTypeShow() {
    this.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    this.setSelection(this.text.toString().length);
}


fun View.setOnSingleClickListener(listener: (v: View) -> Unit) {
    setOnClickListener(object : com.hzfy.ui.view.PreventDoubleClickListener() {
        override fun doClick(view: View?) {
            listener.invoke(view!!)
        }
    })
}
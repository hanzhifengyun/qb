package com.hzfy.common.toast

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.hzfy.common.R
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ToastView @Inject constructor(@ActivityContext val context: Context): IToastView() {

    override fun showToast(info: ToastInfo?) {
        if (info == null || context == null) {
            return
        }
        var duration = Toast.LENGTH_SHORT
        if (info.isShortType) {
            duration = Toast.LENGTH_SHORT
        } else if (info.isLongType) {
            duration = Toast.LENGTH_LONG
        }
        var text = info.text
        if (text == null) {
            text = ""
        }

        val toast: Toast = Toast.makeText(this.context, text, duration)
        val textView = LayoutInflater.from(context).inflate(R.layout.custom_toast, null) as TextView
        textView.text = text
        toast.view = textView
        toast.duration = duration
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}
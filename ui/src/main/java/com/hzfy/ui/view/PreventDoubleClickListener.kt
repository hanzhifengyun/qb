package com.hzfy.ui.view

import android.view.View

/**
 * 防止用户快速点击多次按钮
 */
abstract class PreventDoubleClickListener : View.OnClickListener {
    override fun onClick(view: View) {
        if (enabled) {
            enabled = false
            view.postDelayed(ENABLE_AGAIN, MIN_CLICK_DELAY_TIME)
            doClick(view)
        }
    }

    /**
     * 响应用户点击事件
     *
     * @param view view
     */
    abstract fun doClick(view: View?)

    companion object {
        /**
         * 限制用户不能连续点击的间隔时间
         */
        const val MIN_CLICK_DELAY_TIME: Long = 500

        private var enabled = true

        private val ENABLE_AGAIN = Runnable { enabled = true }
    }
}

package com.hzfy.common.toast;

/**
 * 信息提示
 */
public abstract class IToastView {


    public abstract void showToast(ToastInfo info);


    public void showShortToast(CharSequence text) {
        showToast(ToastInfo.getShortToast(text));
    }

    public void showLongToast(CharSequence text) {
        showToast(ToastInfo.getShortToast(text));
    }

}

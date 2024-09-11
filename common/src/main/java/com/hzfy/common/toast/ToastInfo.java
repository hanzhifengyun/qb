package com.hzfy.common.toast;

public class ToastInfo {
    private CharSequence text;
    private int toastTimeType = TOAST_SHORT;

    public static final int TOAST_SHORT = 1;
    public static final int TOAST_LONG = 2;

    public static ToastInfo getShortToast(CharSequence text) {
        return new ToastInfo(text, TOAST_SHORT);
    }

    public static ToastInfo getLongToast(CharSequence text) {
        return new ToastInfo(text, TOAST_LONG);
    }

    private ToastInfo(CharSequence text, int toastTimeType) {
        this.text = text;
        this.toastTimeType = toastTimeType;
    }

    public CharSequence getText() {
        return text;
    }

    public boolean isShortType() {
        return toastTimeType == TOAST_SHORT;
    }

    public boolean isLongType() {
        return toastTimeType == TOAST_LONG;
    }


    public int getToastTimeType() {
        return toastTimeType;
    }
}

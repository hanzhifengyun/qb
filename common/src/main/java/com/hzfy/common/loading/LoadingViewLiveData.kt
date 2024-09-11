package com.hzfy.common.loading

import androidx.lifecycle.MutableLiveData

class LoadingViewLiveData: MutableLiveData<Boolean>() {

    fun post(isCancelable: Boolean = true) {
        postValue(isCancelable)
    }
}
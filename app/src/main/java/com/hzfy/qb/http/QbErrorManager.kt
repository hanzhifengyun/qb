package com.hzfy.qb.http

import android.content.Context
import com.hzfy.common.api.BaseErrorManager
import com.hzfy.library.net.exception.RemoteServiceException
import com.hzfy.qb.R
import com.hzfy.qb.api.result.QbResult

class QbErrorManager(context: Context): BaseErrorManager(context) {

    override fun getRemoteServiceExceptionMessage(e: RemoteServiceException): String {
        if (e.code == QbResult.CODE_QB_ERROR) {
            return context.getString(R.string.qb_operate_failure)
        }
        return super.getRemoteServiceExceptionMessage(e)
    }
}
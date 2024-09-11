package com.hzfy.common.loading


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.hzfy.common.R
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LoadingView @Inject constructor(@ActivityContext val context: Context) : ILoadingView {

    private val mLoadingDialog: LoadingDialog

    init {
        mLoadingDialog = LoadingDialog()
    }

    override fun showLoadingView(isCancelable: Boolean) {
        if (context == null) {
            return
        }
        context as FragmentActivity
        mLoadingDialog.isCancelable = isCancelable
        mLoadingDialog.show(context.supportFragmentManager, LoadingDialog.TAG)
    }

    override fun hideLoadingView() {
        if (context == null) {
            return
        }
        try {
            mLoadingDialog.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 加载中
     */
    open class LoadingDialog : DialogFragment() {
        companion object {
            const val TAG = "HzfyLoadingDialog"
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val view: View = inflater.inflate(R.layout.fragment_dialog_loading, container)
            return view
        }

    }
}
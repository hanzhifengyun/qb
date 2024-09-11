package com.hzfy.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.hzfy.common.jetpack.BaseViewModel
import com.hzfy.common.loading.ILoadingView
import com.hzfy.common.navigation.INavigation
import com.hzfy.common.toast.IToastView
import com.hzfy.library.util.StatusBarUtil
import javax.inject.Inject


abstract class HzfyBaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {

    protected abstract val mViewModel: VM

    @Inject
    lateinit var mToastView: IToastView
    @Inject
    lateinit var mLoadingView: ILoadingView
    @Inject
    lateinit var mNavigation: INavigation

    protected val mBinding: VB by lazy(mode = LazyThreadSafetyMode.NONE) { createVB() }

    abstract fun createVB(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)


        setStatusBar()
        initView()


        initObserve()

        mViewModel.onStart()
    }


    open fun setStatusBar() {
        StatusBarUtil.setStatusBar(this, true, translucent = false)
    }

    abstract fun initObserve()

    abstract fun initView()

    protected fun <T> observe(liveData: LiveData<T>, observer: Observer<T>) {
        liveData.observe(this, observer)
    }

    protected fun showToast(text: CharSequence) {
        showShortToast(text)
    }

    protected fun showShortToast(text: CharSequence) {
        mToastView.showShortToast(text)
    }

    protected fun showLongToast(text: CharSequence) {
        mToastView.showLongToast(text)
    }

    protected fun showLoadingView(isCancelable: Boolean = true) {
        mLoadingView.showLoadingView(isCancelable)
    }

    protected fun hideLoadingView() {
        mLoadingView.hideLoadingView()
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
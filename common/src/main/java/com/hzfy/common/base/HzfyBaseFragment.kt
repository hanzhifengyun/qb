package com.hzfy.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.hzfy.common.jetpack.BaseViewModel
import com.hzfy.common.loading.ILoadingView
import com.hzfy.common.loading.LoadingView
import com.hzfy.common.navigation.INavigation
import com.hzfy.common.toast.IToastView
import com.hzfy.common.toast.ToastView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class HzfyBaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    protected abstract val mViewModel: VM

    @Inject
    lateinit var mToastView: IToastView
    @Inject
    lateinit var mLoadingView: ILoadingView
    @Inject
    lateinit var mNavigation: INavigation

    private var mBinding: VB? = null

    abstract fun createVB(): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = createVB()
        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()
        initObserve()
        mViewModel.onStart()
    }

    abstract fun initObserve()

    abstract fun initView()

    protected fun <T> observe(liveData: LiveData<T>, observer: Observer<T>) {
        liveData.observe(this, observer)
    }

    protected fun showToast(text: CharSequence){
        showShortToast(text)
    }

    protected fun showShortToast(text: CharSequence){
        mToastView.showShortToast(text)
    }

    protected fun showLongToast(text: CharSequence){
        mToastView.showLongToast(text)
    }

}
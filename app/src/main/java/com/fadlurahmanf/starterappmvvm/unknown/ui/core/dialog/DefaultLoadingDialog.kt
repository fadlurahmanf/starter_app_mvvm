package com.fadlurahmanf.starterappmvvm.unknown.ui.core.dialog

import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.core.base.BaseDialog
import com.fadlurahmanf.starterappmvvm.databinding.DialogDefaultLoadingBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.CoreComponent

class DefaultLoadingDialog: BaseDialog<DialogDefaultLoadingBinding>(DialogDefaultLoadingBinding::inflate) {

    lateinit var component: CoreComponent

    companion object{
        const val IS_CANCELABLE = "CANCELABLE"
    }

    private var isDialogCancelable:Boolean = false
    private var isDialogCancelTouchOutside:Boolean = false

    override fun inject() {
        component = (requireActivity().applicationContext as BaseApp).applicationComponent.coreComponent().create()
        component.inject(this)
    }

    override fun setup() {
        initData()
        isCancelable = isDialogCancelable
    }

    private fun initData() {
        isDialogCancelable = arguments?.getBoolean(IS_CANCELABLE, false) ?: false
    }
}
package com.fadlurahmanf.starterappmvvm.ui.core.dialog

import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.base.BaseDialog
import com.fadlurahmanf.starterappmvvm.databinding.DialogDefaultLoadingBinding
import com.fadlurahmanf.starterappmvvm.di.component.CoreComponent

class DefaultLoadingDialog:BaseDialog<DialogDefaultLoadingBinding>(DialogDefaultLoadingBinding::inflate) {

    lateinit var component:CoreComponent

    override fun inject() {
        component = (requireActivity().applicationContext as BaseApp).applicationComponent.coreComponent().create()
        component.inject(this)
    }

    override fun setup() {

    }
}
package com.fadlurahmanf.starterappmvvm.base


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

typealias InflateActivity<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB:ViewBinding>(
    var inflate:InflateActivity<VB>
):AppCompatActivity() {

    private var _binding:VB ?= null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        initLayout()
        internalSetup()
        initSetup()
    }

    open fun internalSetup(){}

    abstract fun initSetup()

    private fun initLayout(){
        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
    }

    abstract fun inject()

    fun addSubscription(disposable: Disposable) = CompositeDisposable().add(disposable)

    fun removeStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}
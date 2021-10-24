package com.fadlurahmanf.starterappmvvm.base

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseActivity():AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        initLayout()
        internalSetup()
        initSetup()
    }

    open fun internalSetup(){}

    abstract fun initSetup()

    abstract fun initLayout()

    abstract fun inject()

    fun addsubscription(disposable: Disposable) = CompositeDisposable().add(disposable)

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission(activity:Activity, manifestPermission:String){
        if (ContextCompat.checkSelfPermission(activity, manifestPermission)!=PackageManager.PERMISSION_GRANTED) requestPermissions(arrayOf(manifestPermission), 1)
    }
}
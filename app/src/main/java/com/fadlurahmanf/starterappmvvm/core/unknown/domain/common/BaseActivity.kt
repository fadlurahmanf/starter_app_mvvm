package com.fadlurahmanf.starterappmvvm.core.unknown.domain.common

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.chuckerteam.chucker.api.Chucker
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.event.RxEvent
import com.fadlurahmanf.starterappmvvm.core.unknown.external.helper.RxBus
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ApplicationComponent
import com.fadlurahmanf.starterappmvvm.unknown.ui.core.dialog.DefaultLoadingDialog
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

typealias InflateActivity<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding>(
    var inflate: InflateActivity<VB>
) : AppCompatActivity() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    private var _appComponent: ApplicationComponent? = null
    val appComponent get() = _appComponent!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _appComponent = (applicationContext as BaseApp).applicationComponent
        inject()
        super.onCreate(savedInstanceState)
        setLayout()
        internalSetup()
        initSetup()
    }

    fun remoteConfig() = (this.application as BaseApp).remoteConfig

    open fun internalSetup() {}

    abstract fun initSetup()

    private fun setLayout() {
        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
    }

    abstract fun inject()

    fun removeStatusBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private var loadingDialog: DefaultLoadingDialog? = null
    fun showLoadingDialog(isCancelable: Boolean = false) {
        dismissLoadingDialog()
        if (loadingDialog == null) {
            loadingDialog = DefaultLoadingDialog()
            loadingDialog!!.arguments = Bundle().apply {
                putBoolean(DefaultLoadingDialog.IS_CANCELABLE, isCancelable)
            }
            loadingDialog?.show(supportFragmentManager, DefaultLoadingDialog::class.java.simpleName)
        }
    }

    fun dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    private var mSnackbar: Snackbar? = null
    fun showSnackBar(view: View?, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        if (mSnackbar != null) {
            mSnackbar?.dismiss()
            mSnackbar = null
        }
        mSnackbar = Snackbar.make(view ?: binding.root, message, duration)
        mSnackbar?.show()
    }

    private var mToast: Toast? = null
    fun showToast(message: String) {
        if (mToast != null) {
            mToast?.cancel()
            mToast = null
        }
        mToast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        mToast?.show()
    }

    override fun onResume() {
        super.onResume()
        listenRxBus()
    }

    private val compositeDisposable = CompositeDisposable()

    open fun onLanguageChange(event: RxEvent.ChangeLanguageEvent) {}

    private fun listenRxBus() {
        compositeDisposable.add(RxBus.listen(RxEvent.ChangeLanguageEvent::class.java).subscribe {
            onLanguageChange(it)
            compositeDisposable.clear()
        })
    }
}
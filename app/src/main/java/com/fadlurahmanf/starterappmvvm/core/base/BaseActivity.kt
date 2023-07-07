package com.fadlurahmanf.starterappmvvm.core.base

import android.content.Context
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
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.di.component.ApplicationComponent
import com.fadlurahmanf.starterappmvvm.ui.core.dialog.DefaultLoadingDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.Math.sqrt

typealias InflateActivity<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB:ViewBinding>(
    var inflate: InflateActivity<VB>
):AppCompatActivity() {

    private var _binding:VB ?= null
    val binding get() = _binding!!

    private var _appComponent:ApplicationComponent? = null
    val appComponent get() = _appComponent!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _appComponent = (applicationContext as BaseApp).applicationComponent
        inject()
        super.onCreate(savedInstanceState)
        setLayout()
        initRemoteConfigFirebase()
        internalSetup()
        initSetup()
    }

    private fun initShakingListener() {
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager!!.registerListener(sensorListener, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }

    lateinit var remoteConfig: FirebaseRemoteConfig
    open fun initRemoteConfigFirebase(){
        remoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(60)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_firebase_configuration)
    }

    open fun internalSetup(){}

    abstract fun initSetup()

    private fun setLayout(){
        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
    }

    abstract fun inject()

    fun addSubscription(disposable: Disposable) = CompositeDisposable().add(disposable)

    fun removeStatusBar(){
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    private var loadingDialog:DefaultLoadingDialog ?= null
    fun showLoadingDialog(isCancelable:Boolean = false){
        dismissLoadingDialog()
        if (loadingDialog == null){
            loadingDialog = DefaultLoadingDialog()
            loadingDialog!!.arguments = Bundle().apply {
                putBoolean(DefaultLoadingDialog.IS_CANCELABLE, isCancelable)
            }
            loadingDialog?.show(supportFragmentManager, DefaultLoadingDialog::class.java.simpleName)
        }
    }

    fun dismissLoadingDialog(){
        if (loadingDialog != null){
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }

    private var mSnackbar:Snackbar? = null
    fun showSnackBar(view:View?, message:String, duration: Int = Snackbar.LENGTH_SHORT){
        if (mSnackbar != null){
            mSnackbar?.dismiss()
            mSnackbar = null
        }
        mSnackbar = Snackbar.make(view ?: binding.root, message, duration)
        mSnackbar?.show()
    }

    private var mToast:Toast? = null
    fun showToast(message: String){
        if (mToast != null){
            mToast?.cancel()
            mToast = null
        }
        mToast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        mToast?.show()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        initShakingListener()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 35) {
                val intent = Chucker.getLaunchIntent(this@BaseActivity)
                startActivity(intent)
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
}
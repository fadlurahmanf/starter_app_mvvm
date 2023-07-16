package com.fadlurahmanf.starterappmvvm.feature.nsd.presentation

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Handler
import android.os.Looper
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleNsdBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

class ExampleNsdActivity :
    BaseActivity<ActivityExampleNsdBinding>(ActivityExampleNsdBinding::inflate) {

    private var _nsdManager: NsdManager? = null
    private fun nsdManager(): NsdManager {
        return if (_nsdManager == null) {
            _nsdManager = getSystemService(Context.NSD_SERVICE) as NsdManager
            _nsdManager!!
        } else {
            _nsdManager!!
        }
    }

    override fun initSetup() {
        listenSocket()
        registerService()
        discoverService()
    }

    private var _serviceInfo: NsdServiceInfo? = null
    private fun nsdServiceInfo(): NsdServiceInfo? {
        return if (_serviceInfo == null) {
            _serviceInfo = NsdServiceInfo().apply {
                serviceName = "LocalNetworkingApp"
                serviceType = "_LocalNetworkingApp._tcp."
                port = this@ExampleNsdActivity.port
            }
            _serviceInfo
        } else {
            _serviceInfo
        }
    }

    var connectedClients: ArrayList<Socket> = arrayListOf()
    var serverSocket: ServerSocket? = null
    private val handler = Handler(Looper.getMainLooper())
    private var port: Int = -1
    private fun listenSocket() {
        serverSocket = ServerSocket(0).also {
            logConsole.d("SERVER SOCKET PORT ${it.localPort} ")
            port = it.localPort
        }
        handler.post(socketRunnable)
    }

    private val socketRunnable = object : Runnable {
        override fun run() {
            try {
                serverSocket?.accept()?.let {
                    logConsole.d("SOCKET ACCEPTED -> PORT: ${it.port} & LOCAL PORT: ${it.localPort}")
                    connectedClients.add(it)
                }
            } catch (e: SocketException) {
                logConsole.e("SOCKET EXCEPTION ${e.message}")
            } catch (e: Throwable) {
                logConsole.e("SOCKET THROWABLE ${e.message}")
            }
            handler.removeCallbacks(this)
            handler.postDelayed(this, 3000L)
        }
    }

    var socket: Socket? = null
    private val resolveListener = object : NsdManager.ResolveListener {
        override fun onResolveFailed(p0: NsdServiceInfo?, p1: Int) {
            logConsole.d("onServiceResolved -> SERVICE NAME ${p0?.serviceName} & PORT: ${p0?.port} & SERVICE TYPE: ${p0?.serviceType}")
        }

        override fun onServiceResolved(p0: NsdServiceInfo?) {
            p0?.let {
                logConsole.d("onServiceResolved -> SERVICE NAME ${p0.serviceName} & PORT: ${p0.port} & SERVICE TYPE: ${p0.serviceType}")
                if (socket != null) {
                    logConsole.d("onServiceResolved: SOCKET ALREADY CONNECTED LOCAL PORT:${socket?.localPort} & PORT: ${socket?.port}")
                    return
                }
                try {
                    socket = Socket(p0.host, p0.port)
                    logConsole.d("onServiceResolved: SUCCESS CONNECTED SOCKET")
                } catch (e: Throwable) {
                    logConsole.e("onServiceResolved: ${e.message}")
                }
            }
        }

    }

    private fun resolveService(nsdServiceInfo: NsdServiceInfo) {
        nsdManager().resolveService(nsdServiceInfo, resolveListener)
    }

    private val discoveryListener = object : NsdManager.DiscoveryListener {
        override fun onStartDiscoveryFailed(p0: String?, p1: Int) {
            logConsole.e("onStartDiscoveryFailed: $p0 & $p1")
        }

        override fun onStopDiscoveryFailed(p0: String?, p1: Int) {
            logConsole.e("onStopDiscoveryFailed: $p0 & $p1")
        }

        override fun onDiscoveryStarted(p0: String?) {
            logConsole.d("onDiscoveryStarted: $p0")
        }

        override fun onDiscoveryStopped(p0: String?) {
            logConsole.d("onDiscoveryStopped: $p0")
        }

        override fun onServiceFound(p0: NsdServiceInfo?) {
            logConsole.d("onServiceFound: $p0")
            if (p0 != null) {
                resolveService(p0)
            }
        }

        override fun onServiceLost(p0: NsdServiceInfo?) {
            logConsole.d("onServiceFound: $p0")
        }
    }

    private fun discoverService() {
        nsdManager().discoverServices(
            "_LocalNetworkingApp._tcp.",
            NsdManager.PROTOCOL_DNS_SD,
            discoveryListener
        )
    }

    private val registrationListener = object : NsdManager.RegistrationListener {
        override fun onRegistrationFailed(p0: NsdServiceInfo?, p1: Int) {
            logConsole.e("onRegistrationFailed: ${p0?.port}")
        }

        override fun onUnregistrationFailed(p0: NsdServiceInfo?, p1: Int) {
            logConsole.e("onUnregistrationFailed: ${p0?.port}")
        }

        override fun onServiceRegistered(p0: NsdServiceInfo?) {
            logConsole.d("onServiceRegistered: ${p0?.port}")
        }

        override fun onServiceUnregistered(p0: NsdServiceInfo?) {
            logConsole.d("onServiceUnregistered: ${p0?.port}")
        }
    }

    private fun registerService() {
        nsdManager().registerService(
            nsdServiceInfo(),
            NsdManager.PROTOCOL_DNS_SD,
            registrationListener
        )
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}
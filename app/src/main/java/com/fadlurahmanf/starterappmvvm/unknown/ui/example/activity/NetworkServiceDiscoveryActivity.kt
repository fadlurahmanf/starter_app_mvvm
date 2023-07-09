package com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.ResolveListener
import android.net.nsd.NsdServiceInfo
import android.provider.Settings.Secure
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.databinding.ActivityNetworkServiceDiscoveryBinding
import okio.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

class NetworkServiceDiscoveryActivity :
    BaseActivity<ActivityNetworkServiceDiscoveryBinding>(ActivityNetworkServiceDiscoveryBinding::inflate) {
    private lateinit var deviceId: String
    override fun initSetup() {
        deviceId = Secure.getString(contentResolver, Secure.ANDROID_ID)
        registerListener()
        initializeServerSocket()
        registerService()
        binding.btn.setOnClickListener {
            val thread = Thread {
                sendMessage(remoteHost, remotePort, "tes tes")
            }
            thread.start()
        }
    }

    private var localServiceType = "_my_service._tcp."
    private lateinit var nsdManager: NsdManager
    private lateinit var localServiceName: String
    private lateinit var serviceInfo: NsdServiceInfo
    private var localPort: Int = 0
    private fun registerService() {
        serviceInfo = NsdServiceInfo().apply {
            serviceName = "my_service_$deviceId"
            localServiceName = serviceName
            serviceType = localServiceType
            port = localPort
            setAttribute("message", "tes tes")
        }

        nsdManager = getSystemService(Context.NSD_SERVICE) as NsdManager
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
        nsdManager.discoverServices(localServiceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    private var serverSocket: ServerSocket? = null
    private fun initializeServerSocket() {
        serverSocket = ServerSocket()
        serverSocket!!.reuseAddress = true
        serverSocket!!.bind(InetSocketAddress(37577))
        localPort = serverSocket!!.localPort
    }

    override fun onDestroy() {
        nsdManager.unregisterService(registrationListener)
        nsdManager.stopServiceDiscovery(discoveryListener)
        resolveListener = null
        serverSocket?.close()
        super.onDestroy()
    }

    private lateinit var registrationListener: NsdManager.RegistrationListener
    private lateinit var discoveryListener: NsdManager.DiscoveryListener
    private var resolveListener: ResolveListener? = null
    private fun registerListener() {
        registrationListener = object : NsdManager.RegistrationListener {
            override fun onRegistrationFailed(p0: NsdServiceInfo?, p1: Int) {
                logConsole.d("onRegistrationFailed")
            }

            override fun onUnregistrationFailed(p0: NsdServiceInfo?, p1: Int) {
                logConsole.d("onUnregistrationFailed")
            }

            override fun onServiceRegistered(p0: NsdServiceInfo?) {
                localServiceName = p0?.serviceName ?: ""
                logConsole.d("onServiceRegistered $localServiceName")
            }

            override fun onServiceUnregistered(p0: NsdServiceInfo?) {
                logConsole.d("onServiceUnregistered")
            }
        }

        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onStartDiscoveryFailed(p0: String?, p1: Int) {
                logConsole.d("onStartDiscoveryFailed")
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onStopDiscoveryFailed(p0: String?, p1: Int) {
                logConsole.d("onStopDiscoveryFailed")
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onDiscoveryStarted(p0: String?) {
                logConsole.d("onDiscoveryStarted")
            }

            override fun onDiscoveryStopped(p0: String?) {
                logConsole.d("onDiscoveryStopped")
            }

            override fun onServiceFound(p0: NsdServiceInfo?) {
                logConsole.d("onServiceFound ${p0?.serviceName} & ${p0?.serviceType} ${p0?.port}")
                if (!p0?.serviceType.equals(localServiceType)) {
                    logConsole.d("Unknown Service Type ${p0?.serviceType}")
                } else if (p0?.serviceName.equals(localServiceName)) {
                    logConsole.d("Same Machine: ${p0?.serviceName}")
                } else if (p0?.serviceName?.contains("my_service_") == true) {
                    logConsole.d("Local Service Name $localServiceName")
                    logConsole.d("Another Service ${p0.serviceName}")
                    nsdManager.resolveService(p0, resolveListener)
                }
            }

            override fun onServiceLost(p0: NsdServiceInfo?) {
                logConsole.d("onServiceLost")
            }
        }

        resolveListener = object : NsdManager.ResolveListener {
            override fun onResolveFailed(p0: NsdServiceInfo?, p1: Int) {
                logConsole.d("onResolveFailed")
            }

            override fun onServiceResolved(p0: NsdServiceInfo?) {
                logConsole.d("onServiceResolved ${p0?.host} & ${p0?.port} ${p0?.attributes?.get("message")}")
                val host = p0?.host
                val port = p0?.port
                if (p0?.serviceName != localServiceName) {
                    if (host != null && port != null) {
                        remoteHost = host
                        remotePort = port
                        if (serverSocket != null) {
                            val listenThread = Thread {
                                startListening(port)
                            }
                            listenThread.start()
                        }
                        sendMessage(host, port, message = "INIT MESSAGE PERTAMA")
                    }
                } else {
                    logConsole.d("Same IP ${p0.serviceName}")
                }
            }

        }
    }

    private lateinit var remoteHost: InetAddress
    private var remotePort: Int = 0
    private fun sendMessage(host: InetAddress, port: Int, message: String) {
        var socket: Socket? = null
        var printWriter: PrintWriter? = null
        try {
            socket = Socket(host, port)
            socket.reuseAddress = true
            printWriter = PrintWriter(socket.getOutputStream(), true)
            printWriter.println(message)
            logConsole.d("Success Send Message")
        } catch (e: IOException) {
            // Handle exception
            logConsole.e("failed send message ${e.message}")
        } finally {
            printWriter?.close()
            try {
                socket?.close()
            } catch (e: IOException) {
                logConsole.e("failed close socket ${e.message}")
            }
        }
    }

    private fun startListening(port: Int) {
        try {
            while (true) {
                logConsole.d("start Listening Socket")
                val client = serverSocket!!.accept()
                handleClient(client)
            }
        } catch (e: IOException) {
            logConsole.e("failed listening socket ${e.message}")
            // Handle exception
        } finally {
            try {
                serverSocket?.close()
            } catch (e: IOException) {
                logConsole.e("failed close socket ${e.message}")
                // Handle exception
            }
        }
    }

    private fun handleClient(client: Socket) {
        var bufferedReader: BufferedReader? = null
        try {
            bufferedReader = BufferedReader(InputStreamReader(client.getInputStream()))
            while (true) {
                logConsole.d("handle buffered socket")
                val message = bufferedReader.readLine() ?: break
                // Handle incoming message
                logConsole.d("success get message $message")
            }
        } catch (e: IOException) {
            // Handle exception
            logConsole.e("failed get message ${e.message}")
        } finally {
            bufferedReader?.close()
            try {
                client.close()
            } catch (e: IOException) {
                logConsole.e("failed close socket ${e.message}")
                // Handle exception
            }
        }
    }

    override fun inject() {

    }
}
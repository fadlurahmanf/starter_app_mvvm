package com.fadlurahmanf.starterappmvvm.feature.nsd.presentation

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleNsdBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
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

    override fun initSetup() {
        listenSocket()
        registerService()
        discoverService()
        binding.btnSendMessage.setOnClickListener {
            Thread(Runnable {
                try {
                    printerClient?.print("TES TES DARI CLIENT" + "\r\n")
                    printerClient?.flush()

                    printerServer?.print("TES TES DARI SERVER" + "\r\n")
                    printerServer?.flush()
                } catch (e: IOException) {
                    // If the writer fails to initialize there was an io problem, close your connection
                    logConsole.e("ERROR SEND MESSAGE: ${e.message}")
                }
            }).start()
        }
    }

    var connectedClients: ArrayList<Socket> = arrayListOf()
    var serverSocket: ServerSocket? = null
    var printerServer: PrintWriter? = null
    private var port: Int = -1

    private fun listenSocket() {
        serverSocket = ServerSocket(0).also {
            logConsole.d("SERVER SOCKET PORT -> LOCAL PORT: ${it.localPort} ")
            port = it.localPort
        }
        Thread(Runnable {
            while (serverSocket != null) {
                try {
                    logConsole.d("SERVER SOCKET -> LOCAL PORT:${serverSocket?.localPort} & ${serverSocket?.inetAddress}")
                    serverSocket?.accept()?.let {
                        logConsole.d("SOCKET ACCEPTED -> PORT: ${it.port} & LOCAL PORT: ${it.localPort}")
                        printerServer = PrintWriter(it.getOutputStream())
                        connectedClients.add(it)
                        Thread(ClientReader(it)).start()
                    }
                } catch (e: SocketException) {
                    logConsole.e("SOCKET EXCEPTION ${e.message}")
                    break
                }
            }
        }).start()
    }

    var socket: Socket? = null
    var printerClient: PrintWriter? = null
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
                    printerClient = PrintWriter(socket!!.getOutputStream())
                    Thread(ServerReader(socket!!)).start()
                    logConsole.d("onServiceResolved: SUCCESS CONNECTED SOCKET")
                } catch (e: Throwable) {
                    logConsole.e("onServiceResolved: ${e.message}")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        printerServer?.close()
        printerClient?.close()
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
            "_LocalNetworkingApp._tcp.", NsdManager.PROTOCOL_DNS_SD, discoveryListener
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
            nsdServiceInfo(), NsdManager.PROTOCOL_DNS_SD, registrationListener
        )
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

    inner class ClientReader(private val client: Socket) : Runnable {

        override fun run() {
            logConsole.d("CLIENT READER -> PORT: ${client.port} LOCAL PORT: ${client.localPort}")
            var line: String?
            val reader: BufferedReader

            try {
                reader = BufferedReader(InputStreamReader(client.getInputStream()))
            } catch (e: IOException) {
                logConsole.e("ERROR ClientReader BufferedReader: ${e.message}")
                connectedClients.remove(client)
                return
            }

            while (true) {
                try {
                    line = reader.readLine()
                    logConsole.d("CLIENT READER -> READ LINE $line")
                    if (line == null) {
                        connectedClients.remove(client)
                        break
                    }
                    logConsole.d("SUCCESS ClientReader -> READ LINE $line")

                } catch (e: IOException) {
                    connectedClients.remove(client)
                    break
                }
            }
        }
    }

    inner class ServerReader(private val server: Socket) : Runnable {
        override fun run() {
            logConsole.d("SERVER READER -> PORT: ${server.port} & LOCAL PORT: ${server.localPort}")
            var line: String?
            val reader: BufferedReader

            try {
                reader = BufferedReader(InputStreamReader(server.getInputStream()))
            } catch (e: IOException) {
                logConsole.e("ERROR SERVER READER BufferedReader: ${e.message}")
                serverSocket = null
                return
            }

            while (true) {
                try {
                    line = reader.readLine()

                    logConsole.d("SERVER READER -> READ LINE: $line")

                    if (line == null) {
                        serverSocket = null
                        break
                    }

                    logConsole.d("SERVER READER SUCCESS -> READ LINE: $line")

                } catch (e: IOException) {
                    serverSocket = null
                    break
                }
            }
        }
    }

}
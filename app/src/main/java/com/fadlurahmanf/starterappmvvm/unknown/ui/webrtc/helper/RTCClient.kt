package com.fadlurahmanf.starterappmvvm.unknown.ui.webrtc.helper

import android.app.Application
import android.content.Context
import android.util.Log
import okhttp3.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.PeerConnectionFactory
import java.lang.Exception
import java.net.URI

class RTCClient(var context: Context) {
    private val rootEglBase: EglBase = EglBase.create()
    lateinit var socketClient: WebSocketClient

    init {
    }

    private fun initWebSocket(url:String){
        socketClient = object : WebSocketClient(URI(url)){
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d("RTCClient", "initWebSocket: onOpen ...")
            }

            override fun onMessage(message: String?) {
                Log.d("RTCClient", "initWebSocket: message $message")
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d("RTCClient", "initWebSocket: onClose")
            }

            override fun onError(ex: Exception?) {
                Log.d("RTCClient", "initWebSocket: onError -> ${ex?.message}")
            }

        }
    }

    private fun initPeerConnectionFactory(context:Context){
        val option = PeerConnectionFactory.InitializationOptions.builder(context)
            .setEnableInternalTracer(true)
            .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
            .createInitializationOptions()
        PeerConnectionFactory.initialize(option)
    }

    private fun buildPeerConnectionFactory(): PeerConnectionFactory {
        return PeerConnectionFactory
            .builder()
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(rootEglBase.eglBaseContext))
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(rootEglBase.eglBaseContext, true, true))
            .setOptions(PeerConnectionFactory.Options().apply {
                disableEncryption = true
                disableNetworkMonitor = true
            })
            .createPeerConnectionFactory()
    }
}
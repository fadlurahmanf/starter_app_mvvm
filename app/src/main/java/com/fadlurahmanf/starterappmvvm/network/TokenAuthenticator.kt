package com.fadlurahmanf.starterappmvvm.network

import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator: Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("TokenAuthenticator", "authenticate: ${response.body?.string()}")
        val request = response.request
        return request.newBuilder().build()
    }
}
package com.fadlurahmanf.starterappmvvm.feature.encrypt.data.model

open class CryptoKey(
    /**
     * base64 encoded private key
     **/
    open val privateKey: String,
    /**
     * base64 encoded public key
     **/
    open val publicKey: String
)

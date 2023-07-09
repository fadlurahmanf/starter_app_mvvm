package com.fadlurahmanf.starterappmvvm.feature.encrypt.data.model

open class CryptoKey(
    /**
     * encoded private key
     **/
    open val privateKey: String,
    /**
     * encoded public key
     **/
    open val publicKey: String
)

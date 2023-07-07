package com.fadlurahmanf.starterappmvvm.encrypt.data.model

data class CryptoKey(
    /**
     * encoded private key
     **/
    val privateKey: String,
    /**
     * encoded public key
     **/
    val publicKey: String
)

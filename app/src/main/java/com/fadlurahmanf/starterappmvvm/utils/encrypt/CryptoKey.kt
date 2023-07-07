package com.fadlurahmanf.starterappmvvm.utils.encrypt

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

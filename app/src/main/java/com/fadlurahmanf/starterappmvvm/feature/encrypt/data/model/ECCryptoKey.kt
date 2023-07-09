package com.fadlurahmanf.starterappmvvm.feature.encrypt.data.model

import java.math.BigInteger

class ECCryptoKey(
    override val privateKey: String,
    override val publicKey: String,
    val x: BigInteger,
    val y: BigInteger,
) : CryptoKey(privateKey, publicKey) {}

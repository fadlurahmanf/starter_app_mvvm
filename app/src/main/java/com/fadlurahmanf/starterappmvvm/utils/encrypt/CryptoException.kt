package com.fadlurahmanf.starterappmvvm.utils.encrypt

class CryptoException(
    val code: String? = null,
    override val message: String? = null
) : Throwable(message) {
}
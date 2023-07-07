package com.fadlurahmanf.starterappmvvm.utils.encrypt

enum class AESMethod {
    CBC,
    ECB,
}

enum class PaddingScheme {
    NoPadding,
    PKCS1,
    PKCS5,
    PKCS7,
}

abstract class EncryptTools {
    open fun getPaddingScheme(scheme: PaddingScheme): String {
        return when (scheme) {
            PaddingScheme.PKCS7 -> {
                "PKCS7Padding"
            }

            PaddingScheme.PKCS5 -> {
                "PKCS5Padding"
            }

            PaddingScheme.NoPadding -> {
                "NoPadding"
            }

            else -> {
                throw CryptoException("PADDING SCHEME NOT FOUND")
            }
        }
    }
}
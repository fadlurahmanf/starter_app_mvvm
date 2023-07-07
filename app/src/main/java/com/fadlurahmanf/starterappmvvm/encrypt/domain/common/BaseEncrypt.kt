package com.fadlurahmanf.starterappmvvm.encrypt.domain.common

import android.os.Build
import com.fadlurahmanf.starterappmvvm.encrypt.data.exception.CryptoException
import java.util.Base64

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

abstract class BaseEncrypt {

    open fun encode(byte: ByteArray): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(byte)
        } else {
            android.util.Base64.encodeToString(byte, android.util.Base64.DEFAULT)
        }
    }

    open fun decode(text: String): ByteArray {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(text.toByteArray())
        } else {
            android.util.Base64.decode(text.toByteArray(), android.util.Base64.DEFAULT)
        }
    }

    open fun decode(byte: ByteArray): ByteArray {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(byte)
        } else {
            android.util.Base64.decode(byte, android.util.Base64.DEFAULT)
        }
    }

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
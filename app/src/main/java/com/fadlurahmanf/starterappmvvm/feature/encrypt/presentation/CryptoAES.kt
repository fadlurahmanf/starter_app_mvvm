package com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation

import com.fadlurahmanf.starterappmvvm.core.external.constant.logConsole
import com.fadlurahmanf.starterappmvvm.feature.encrypt.data.exception.CryptoException
import com.fadlurahmanf.starterappmvvm.feature.encrypt.domain.common.AESMethod
import com.fadlurahmanf.starterappmvvm.feature.encrypt.domain.common.BaseEncrypt
import com.fadlurahmanf.starterappmvvm.feature.encrypt.domain.common.PaddingScheme
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoAES : BaseEncrypt() {
    fun encrypt(
        plainText: String,
        secretKey: String,
        method: AESMethod = AESMethod.CBC,
        padding: PaddingScheme = PaddingScheme.PKCS7
    ): String? {

        try {
            if (plainText.isEmpty()) {
                throw CryptoException(message = "TEXT CANNOT BE EMPTY")
            }

            if (secretKey.length != 32) {
                throw CryptoException(message = "KEY MUST BE 32 LENGTH")
            }

            return when (method) {
                AESMethod.ECB -> {
                    encryptECB(plainText, secretKey, padding)
                }

                AESMethod.CBC -> {
                    encryptCBC(plainText, secretKey, padding)
                }

                else -> {
                    throw CryptoException(message = "AES METHOD NOT FOUND")
                }
            }
        } catch (e: CryptoException) {
            logConsole.d("ENCRYPT ERROR: ${e.message}")
            return null
        } catch (e: Throwable) {
            logConsole.d("ENCRYPT ERROR: ${e.message}")
            return null
        }
    }

    private fun encryptCBC(
        plainText: String, secretKey: String, padding: PaddingScheme
    ): String {
        val key = SecretKeySpec(secretKey.toByteArray(), "AES")
        val iv = IvParameterSpec(ByteArray(16))
        val cipher = Cipher.getInstance("AES/CBC/${getPaddingScheme(padding)}")
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val encryptedBytes = when (padding) {
            PaddingScheme.NoPadding -> {
                cipher.doFinal(padPlaintext(plainText).toByteArray())
            }

            PaddingScheme.PKCS1 -> {
                throw CryptoException(message = "NO SUPPORTED PADDING")
            }

            PaddingScheme.PKCS5 -> {
                cipher.doFinal(plainText.toByteArray())
            }

            PaddingScheme.PKCS7 -> {
                cipher.doFinal(plainText.toByteArray())
            }
        }
        return encode(encryptedBytes)
    }

    private fun encryptECB(
        plainText: String, secretKey: String, padding: PaddingScheme
    ): String {
        val key = SecretKeySpec(secretKey.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/ECB/${getPaddingScheme(padding)}")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = when (padding) {
            PaddingScheme.NoPadding -> {
                cipher.doFinal(padPlaintext(plainText).toByteArray())
            }

            PaddingScheme.PKCS1 -> {
                throw CryptoException(message = "NO SUPPORTED PADDING")
            }

            PaddingScheme.PKCS5 -> {
                cipher.doFinal(plainText.toByteArray())
            }

            PaddingScheme.PKCS7 -> {
                cipher.doFinal(plainText.toByteArray())
            }
        }
        return encode(encryptedBytes)
    }

    fun decrypt(
        encryptedText: String,
        secretKey: String,
        method: AESMethod = AESMethod.CBC,
        padding: PaddingScheme = PaddingScheme.PKCS7
    ): String? {
        try {
            if (encryptedText.isEmpty()) {
                throw CryptoException(message = "ENCRYPTED TEXT CANNOT BE EMPTY")
            }

            if (secretKey.length != 32) {
                throw CryptoException(message = "KEY MUST BE 32 LENGTH")
            }

            return when (method) {
                AESMethod.ECB -> {
                    decryptECB(encryptedText, secretKey, padding)
                }

                AESMethod.CBC -> {
                    decryptCBC(encryptedText, secretKey, padding)
                }

                else -> {
                    throw CryptoException(message = "AES METHOD NOT FOUND")
                }
            }
        } catch (e: CryptoException) {
            logConsole.d("DECRYPT ERROR: ${e.message}")
            return null
        } catch (e: Throwable) {
            logConsole.d("DECRYPT ERROR: ${e.message}")
            return null
        }
    }

    private fun decryptCBC(
        encryptedText: String, secretKey: String, padding: PaddingScheme
    ): String {
        val key = SecretKeySpec(secretKey.toByteArray(), "AES")
        val iv = IvParameterSpec(ByteArray(16))
        val cipher = Cipher.getInstance("AES/CBC/${getPaddingScheme(padding)}")
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        return String(
            cipher.doFinal(
                decode(encryptedText)
            )
        )
    }

    private fun decryptECB(
        encryptedText: String, secretKey: String, padding: PaddingScheme
    ): String {
        val key = SecretKeySpec(secretKey.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/ECB/${getPaddingScheme(padding)}")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return String(
            cipher.doFinal(
                decode(encryptedText)
            )
        )
    }

    private fun padPlaintext(plaintext: String): String {
        val blockSize = 16
        val paddingLength = blockSize - (plaintext.length % blockSize)
        val paddingChar = paddingLength.toChar()
        return plaintext + paddingChar.toString().repeat(paddingLength)
    }
}
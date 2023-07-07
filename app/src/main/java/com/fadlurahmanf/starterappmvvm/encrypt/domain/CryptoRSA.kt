package com.fadlurahmanf.starterappmvvm.encrypt.domain

import android.os.Build
import com.fadlurahmanf.starterappmvvm.encrypt.data.exception.CryptoException
import com.fadlurahmanf.starterappmvvm.encrypt.data.model.CryptoKey
import com.fadlurahmanf.starterappmvvm.encrypt.domain.common.BaseEncrypt
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

class CryptoRSA : BaseEncrypt() {

    fun generateKey(): CryptoKey {
        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(1024)
        val keyPair = keyGen.generateKeyPair()
        val publicKey = encode(keyPair.public.encoded)
        val privateKey = encode(keyPair.private.encoded)
        return CryptoKey(privateKey = privateKey, publicKey = publicKey)
    }

    private fun loadPublicKey(encodedPublicKey: String): PublicKey {
        val data = decode(encodedPublicKey.toByteArray())
        val spec = X509EncodedKeySpec(data)
        val fact: KeyFactory = KeyFactory.getInstance("RSA")
        return fact.generatePublic(spec)
    }

    private fun loadPrivateKey(encodedPrivateKey: String): PrivateKey {
        val data = decode(encodedPrivateKey.toByteArray())
        val spec = PKCS8EncodedKeySpec(data)
        val fact: KeyFactory = KeyFactory.getInstance("RSA")
        return  fact.generatePrivate(spec)
    }

    fun encrypt(plainText: String, encodedPublicKey: String): String? {
        return try {
            if (plainText.isEmpty()) {
                throw CryptoException(message = "TEXT CANNOT BE EMPTY")
            }

            if (encodedPublicKey.isEmpty()) {
                throw CryptoException(message = "KEY CANNOT BE EMPTY")
            }

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey(encodedPublicKey))
            val encryptedBytes = cipher.doFinal(plainText.toByteArray())
            return encode(encryptedBytes)
        } catch (e: CryptoException) {
            null
        } catch (e: Throwable) {
            println("masuk sini ${e.message}")
            null
        }
    }


    fun decrypt(encrypted: String, encodedPrivateKey: String): String? {
        return try {
            if (encrypted.isEmpty()) {
                throw CryptoException(message = "TEXT CANNOT BE EMPTY")
            }

            if (encodedPrivateKey.isEmpty()) {
                throw CryptoException(message = "KEY CANNOT BE EMPTY")
            }

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey(encodedPrivateKey))
            return String(cipher.doFinal(decode(encrypted)))
        } catch (e: Throwable) {
            null
        }
    }
}

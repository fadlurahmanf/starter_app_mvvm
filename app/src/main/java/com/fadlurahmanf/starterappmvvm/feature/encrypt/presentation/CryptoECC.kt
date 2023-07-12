package com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation

import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.feature.encrypt.data.model.CryptoKey
import com.fadlurahmanf.starterappmvvm.feature.encrypt.domain.common.BaseEncrypt
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Security
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


class CryptoECC : BaseEncrypt() {

    fun generateKey(): CryptoKey {
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())
        val keyPairGenerator = KeyPairGenerator.getInstance("ECDH")
        keyPairGenerator.initialize(ECGenParameterSpec("secp521r1"))
        val keyPair = keyPairGenerator.generateKeyPair()
        return CryptoKey(encode(keyPair.private.encoded), encode(keyPair.public.encoded))
    }

    private fun loadPublicKey(encodedPublicKey: String): PublicKey {
        val data = decode(encodedPublicKey.toByteArray())
        val spec = X509EncodedKeySpec(data)
        val fact: KeyFactory = KeyFactory.getInstance("ECDH")
        return fact.generatePublic(spec)
    }

    private fun loadPrivateKey(encodedPrivateKey: String): PrivateKey {
        val data = decode(encodedPrivateKey.toByteArray())
        val spec = PKCS8EncodedKeySpec(data)
        val fact: KeyFactory = KeyFactory.getInstance("ECDH")
        return fact.generatePrivate(spec)
    }

    fun encrypt(plainText: String, encodedPublicKey: String): String? {
        return try {
            val cipherEnc = Cipher.getInstance("ECIES")
            cipherEnc.init(Cipher.ENCRYPT_MODE, loadPublicKey(encodedPublicKey))
            val ciphertext = cipherEnc.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            encode(ciphertext)
        } catch (e: Throwable) {
            logConsole.e("ENCRYPT ECC: ${e.message}")
            null
        }
    }

    fun decrypt(encrypted: String, encodedPrivateKey: String): String? {
        return try {
            val cipherDec = Cipher.getInstance("ECIES")
            cipherDec.init(Cipher.DECRYPT_MODE, loadPrivateKey(encodedPrivateKey))
            val decrypted = cipherDec.doFinal(decode(encrypted))
            return String(decrypted, StandardCharsets.UTF_8)
        } catch (e: Throwable) {
            logConsole.e("DECRYPT ECC: ${e.message}")
            null
        }
    }
}
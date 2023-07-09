package com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation

import com.fadlurahmanf.starterappmvvm.feature.encrypt.data.model.CryptoKey
import com.fadlurahmanf.starterappmvvm.feature.encrypt.domain.common.BaseEncrypt
import org.bouncycastle.jce.ECPointUtil
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import org.bouncycastle.math.ec.ECPoint
import org.bouncycastle.util.test.FixedSecureRandom
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Security
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher


class CryptoECC : BaseEncrypt() {

    fun generateKey(): CryptoKey {
//        Security.removeProvider("BC")
//        Security.addProvider(BouncyCastleProvider())
        val keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "SC")
        keyPairGenerator.initialize(ECGenParameterSpec("secp224k1"))
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
        return  fact.generatePrivate(spec)
    }


    fun tes() {
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())
        val keyPairGenerator = KeyPairGenerator.getInstance("ECDH")
        keyPairGenerator.initialize(ECGenParameterSpec("secp521r1"))
        val keyPair = keyPairGenerator.generateKeyPair()
        // Encryption
        val plaintext =
            "The quick brown fox jumps over the lazy dog".toByteArray(StandardCharsets.UTF_8)
        val cipherEnc = Cipher.getInstance("ECIES")
        cipherEnc.init(
            Cipher.ENCRYPT_MODE,
            loadPublicKey(encode(keyPair.public.encoded))
        ) // In practice, the public key of the recipient side is used
        val ciphertext = cipherEnc.doFinal(plaintext)
        val encrypted = Base64.getEncoder().encodeToString(ciphertext)
        println("masuk encrypted ${encrypted}")

        // Decryption
        val cipherDec = Cipher.getInstance("ECIES")
        cipherDec.init(Cipher.DECRYPT_MODE, loadPrivateKey(encode(keyPair.private.encoded)))
        val decrypted = cipherDec.doFinal(decode(encrypted))
        println("masuk decrypted ${String(decrypted, StandardCharsets.UTF_8)}")
    }
}
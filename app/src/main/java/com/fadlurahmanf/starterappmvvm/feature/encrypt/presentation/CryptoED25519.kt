package com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation

import com.fadlurahmanf.starterappmvvm.feature.encrypt.data.model.CryptoKey
import com.fadlurahmanf.starterappmvvm.feature.encrypt.domain.common.BaseEncrypt
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import java.security.SecureRandom
import java.util.Base64


class CryptoED25519 : BaseEncrypt() {
    private fun generateKey2(): AsymmetricCipherKeyPair {
        val secureRandom = SecureRandom()
        val keyPairGenerator = Ed25519KeyPairGenerator()
        keyPairGenerator.init(Ed25519KeyGenerationParameters(secureRandom))
        return keyPairGenerator.generateKeyPair()
    }

    fun generateKey(): CryptoKey {
        val secureRandom = SecureRandom()
        val keyPairGenerator = Ed25519KeyPairGenerator()
        keyPairGenerator.init(Ed25519KeyGenerationParameters(secureRandom))
        val key = keyPairGenerator.generateKeyPair()
        val privateKey = key.private as Ed25519PrivateKeyParameters
        val publicKey = key.public as Ed25519PublicKeyParameters

        val privateKeyEncoded = encode(privateKey.encoded)
        val publicKeyEncoded = encode(publicKey.encoded)
        return CryptoKey(privateKeyEncoded, publicKeyEncoded)
    }

    fun generateSignature(plainText: String, encodedPrivateKey: String): String? {
        return try {
            val privateKey = Ed25519PrivateKeyParameters(decode(encodedPrivateKey), 0)
            val signer = Ed25519Signer()
            signer.init(true, privateKey)
            signer.update(plainText.toByteArray(), 0, plainText.length)
            val signature =  signer.generateSignature()
            encode(signature)
        } catch (e: Throwable) {
            null
        }
    }

    fun verifySignature(text: String, signature: String, encodedPublicKey: String): Boolean {
        val publicKey = Ed25519PublicKeyParameters(decode(encodedPublicKey), 0)
        val verifierDerived = Ed25519Signer()
        verifierDerived.init(false, publicKey)
        val message = text.toByteArray()
        verifierDerived.update(message, 0, text.length)
        return verifierDerived.verifySignature(decode(signature))
    }
}
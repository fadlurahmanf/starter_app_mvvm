package com.fadlurahmanf.starterappmvvm.core.helper

import android.os.Build
import com.fadlurahmanf.starterappmvvm.BuildConfig
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


class RSAHelper {
    enum class METHOD{
        PKCS1PEM,
        PKCS8PEM,
        Base64
    }


    companion object {
        private const val PKCS1PEM_PREFIX_PUBLIC = "-----BEGIN RSA PUBLIC KEY-----"
        private const val PKCS1PEM_SUFFIX_PUBLIC = "-----END RSA PUBLIC KEY-----"
        private const val PKCS1PEM_PREFIX_PRIVATE = "-----BEGIN RSA PRIVATE KEY-----"
        private const val PKCS1PEM_SUFFIX_PRIVATE = "-----END RSA PRIVATE KEY-----"

        private const val PKCS8PEM_PREFIX_PUBLIC = "-----BEGIN PUBLIC KEY-----"
        private const val PKCS8PEM_SUFFIX_PUBLIC = "-----END PUBLIC KEY-----"
        private const val PKCS8PEM_PREFIX_PRIVATE = "-----BEGIN PRIVATE KEY-----"
        private const val PKCS8PEM_SUFFIX_PRIVATE = "-----END PRIVATE KEY-----"

        lateinit var method: METHOD
        lateinit var publicKey: PublicKey
        lateinit var privateKey: PrivateKey

        private var encodedPublicKey = when (BuildConfig.BUILD_TYPE) {
            "release" -> BuildConfig.PUBLIC_KEY_PRODUCTION
            "staging" -> BuildConfig.PUBLIC_KEY_STAGING
            else -> BuildConfig.PUBLIC_KEY_DEV
        }
        private var encodedPrivateKey = when (BuildConfig.BUILD_TYPE) {
            "release" -> BuildConfig.PRIVATE_KEY_PRODUCTION
            "staging" -> BuildConfig.PRIVATE_KEY_STAGING
            else -> BuildConfig.PRIVATE_KEY_DEV
        }
        fun generateKey(method: METHOD): KeyPair {
            this.method = method
            val keyGen = KeyPairGenerator.getInstance("RSA")
            keyGen.initialize(1024)
            val keypair = keyGen.generateKeyPair()
            publicKey = keypair.public
            privateKey = keypair.private
            return keypair
        }

        fun encodedPublicKey(publicKey: PublicKey):String?{
            return when (method) {
                METHOD.PKCS1PEM -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val mEncoded = Base64.getEncoder().encodeToString(publicKey.encoded)
                        val mPublicKeyString = "${PKCS1PEM_PREFIX_PUBLIC}\n${mEncoded}\n${PKCS1PEM_SUFFIX_PUBLIC}"
                        println("publicKey: $mPublicKeyString")
                        mPublicKeyString
                    } else {
                        val mEncoded = android.util.Base64.encodeToString(publicKey.encoded, android.util.Base64.DEFAULT)
                        val mPublicKeyString = "${PKCS1PEM_PREFIX_PUBLIC}\n${mEncoded}\n${PKCS1PEM_SUFFIX_PUBLIC}"
                        println("publicKey: $mPublicKeyString")
                        mPublicKeyString
                    }
                }
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Base64.getEncoder().encodeToString(publicKey.encoded)
                    }else{
                        android.util.Base64.encodeToString(publicKey.encoded, android.util.Base64.DEFAULT)
                    }
                }
            }
        }

        fun encodedPrivateKey(privateKey: PrivateKey):String?{
            return when(method){
                METHOD.PKCS1PEM -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val mEncoded = Base64.getEncoder().encodeToString(privateKey.encoded)
                        val mPrivateKeyString = "${PKCS1PEM_PREFIX_PRIVATE}\n${mEncoded}\n${PKCS1PEM_SUFFIX_PRIVATE}"
                        println("privateKey: $mPrivateKeyString")
                        return mPrivateKeyString
                    } else {
                        val mEncoded = android.util.Base64.encodeToString(privateKey.encoded, android.util.Base64.DEFAULT)
                        val mPrivateKeyString = "${PKCS1PEM_PREFIX_PRIVATE}\n${mEncoded}\n${PKCS1PEM_SUFFIX_PRIVATE}"
                        println("privateKey: $mPrivateKeyString")
                        return mPrivateKeyString
                    }
                }
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Base64.getEncoder().encodeToString(privateKey.encoded)
                    }else{
                        android.util.Base64.encodeToString(privateKey.encoded, android.util.Base64.DEFAULT)
                    }
                }
            }
        }

        private fun loadPublicKey(): PublicKey? {
            return when(method){
                METHOD.PKCS1PEM -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val mKey = encodedPublicKey
                        val data = Base64.getDecoder().decode(mKey.toByteArray())
                        val spec = X509EncodedKeySpec(data)
                        val fact: KeyFactory = KeyFactory.getInstance("RSA")
                        return fact.generatePublic(spec)
                    }else{
                        return null
                    }
                }
                else -> {
                    null
                }
            }
        }

        private fun loadPrivateKey():PrivateKey? {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val mKey = encodedPrivateKey
                val data = Base64.getDecoder().decode(mKey.toByteArray())
                val spec = PKCS8EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                return fact.generatePrivate(spec)
            }else{
                val mKey = encodedPrivateKey
                val data = android.util.Base64.decode(mKey.toByteArray(), android.util.Base64.DEFAULT)
                val spec = PKCS8EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                return fact.generatePrivate(spec)
            }
        }

        fun encrypt(plainText:String):String?{
            method = METHOD.PKCS1PEM
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey())
                val encryptedBytes = cipher.doFinal(plainText.toByteArray());
                return Base64.getEncoder().encodeToString(encryptedBytes)
            } else {
                return null
            }
        }


        fun decrypt(encrypted:String):String?{
            method = METHOD.PKCS1PEM
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey());
                return String(cipher.doFinal(Base64.getDecoder().decode(encrypted)))
            } else {
                return null
            }
        }
    }
}
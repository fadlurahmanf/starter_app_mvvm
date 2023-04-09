package com.fadlurahmanf.starterappmvvm.utils.encrypt

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
        PKCS8PEM
    }

    /**
     1. generate key with RSA Method
     2. copy the public key & private key & save to secret directory ( seek help to devops ). mine in local properties and set config field in app build gradle
     3. you can use encrypt now
     */

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

        private var encodedPublicKey = BuildConfig.PUBLIC_KEY

        private var encodedPrivateKey = BuildConfig.PRIVATE_KEY

        fun generateKey(method: METHOD = METHOD.PKCS1PEM): KeyPair {
            Companion.method = method
            val keyGen = KeyPairGenerator.getInstance("RSA")
            keyGen.initialize(1024)
            val keypair = keyGen.generateKeyPair()
            publicKey = keypair.public
            privateKey = keypair.private
            return keypair
        }

        fun encodedPublicKey(publicKey: PublicKey):String{
            return when (method) {
                METHOD.PKCS1PEM -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val mEncoded = Base64.getEncoder().encodeToString(publicKey.encoded)
                        val mPublicKeyString = "$PKCS1PEM_PREFIX_PUBLIC\n${mEncoded}\n$PKCS1PEM_SUFFIX_PUBLIC"
                        mPublicKeyString
                    } else {
                        val mEncoded = android.util.Base64.encodeToString(publicKey.encoded, android.util.Base64.DEFAULT)
                        val mPublicKeyString = "$PKCS1PEM_PREFIX_PUBLIC\n${mEncoded}\n$PKCS1PEM_SUFFIX_PUBLIC"
                        mPublicKeyString
                    }
                }
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val mEncoded = Base64.getEncoder().encodeToString(publicKey.encoded)
                        val mPublicKeyString = "$PKCS8PEM_PREFIX_PUBLIC\n${mEncoded}\n$PKCS8PEM_SUFFIX_PUBLIC"
                        mPublicKeyString
                    } else {
                        val mEncoded = android.util.Base64.encodeToString(publicKey.encoded, android.util.Base64.DEFAULT)
                        val mPublicKeyString = "$PKCS8PEM_PREFIX_PUBLIC\n${mEncoded}\n$PKCS8PEM_SUFFIX_PUBLIC"
                        mPublicKeyString
                    }
                }
            }
        }

        fun encodedPrivateKey(privateKey: PrivateKey):String?{
            return when(method){
                METHOD.PKCS1PEM -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val mEncoded = Base64.getEncoder().encodeToString(privateKey.encoded)
                        return "$PKCS1PEM_PREFIX_PRIVATE\n${mEncoded}\n$PKCS1PEM_SUFFIX_PRIVATE"
                    } else {
                        val mEncoded = android.util.Base64.encodeToString(
                            privateKey.encoded,
                            android.util.Base64.DEFAULT
                        )
                        return "$PKCS1PEM_PREFIX_PRIVATE\n${mEncoded}\n$PKCS1PEM_SUFFIX_PRIVATE"
                    }
                }
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val mEncoded = Base64.getEncoder().encodeToString(privateKey.encoded)
                        return "$PKCS1PEM_PREFIX_PRIVATE\n${mEncoded}\n$PKCS1PEM_SUFFIX_PRIVATE"
                    } else {
                        val mEncoded = android.util.Base64.encodeToString(
                            privateKey.encoded,
                            android.util.Base64.DEFAULT
                        )
                        return "$PKCS8PEM_PREFIX_PRIVATE\n${mEncoded}\n$PKCS8PEM_SUFFIX_PRIVATE"
                    }
                }
            }
        }

        private fun loadPublicKey(): PublicKey? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mKey = encodedPublicKey
                val data = Base64.getDecoder().decode(mKey.toByteArray())
                val spec = X509EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                fact.generatePublic(spec)
            } else {
                val mKey = encodedPublicKey
                val data = android.util.Base64.decode(mKey.toByteArray(), android.util.Base64.DEFAULT)
                val spec = X509EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                fact.generatePublic(spec)
            }
        }

        private fun loadPrivateKey():PrivateKey? {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val mKey = encodedPrivateKey
                val data = Base64.getDecoder().decode(mKey.toByteArray())
                val spec = PKCS8EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                fact.generatePrivate(spec)
            }else{
                val mKey = encodedPrivateKey
                val data = android.util.Base64.decode(mKey.toByteArray(), android.util.Base64.DEFAULT)
                val spec = PKCS8EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                fact.generatePrivate(spec)
            }
        }

        fun encrypt(plainText:String):String?{
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey())
                val encryptedBytes = cipher.doFinal(plainText.toByteArray());
                Base64.getEncoder().encodeToString(encryptedBytes)
            } else {
                null
            }
        }


        fun decrypt(encrypted:String):String?{
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey());
                String(cipher.doFinal(Base64.getDecoder().decode(encrypted)))
            } else {
                null
            }
        }
    }
}
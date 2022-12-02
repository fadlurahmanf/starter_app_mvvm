package com.fadlurahmanf.starterappmvvm.core.helper

import android.os.Build
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


class CryptoHelper {

    companion object {
        lateinit var publicKey: PublicKey
        lateinit var privateKey: PrivateKey
        fun generateKey(): KeyPair {
            val keyGen = KeyPairGenerator.getInstance("RSA")
            keyGen.initialize(1024)
            val keypair = keyGen.generateKeyPair()
            publicKey = keypair.public
            privateKey = keypair.private
//            val encrypted = encrypt("tes tes")
//            println("masuk ${encrypted}")
//            val decrypted = decrypt(encrypted?:"")
//            println("masuk ${decrypted}")
            return keypair
        }

        fun encodedPublicKey(publicKey: PublicKey):String?{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val encoded = Base64.getEncoder().encodeToString(publicKey.encoded)
                return "-----BEGIN RSA PUBLIC KEY-----\n$encoded\n-----END RSA PUBLIC KEY-----"
            } else {
                return null;
            }
        }

        fun encodedPrivateKey(privateKey: PrivateKey):String?{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val encoded = Base64.getEncoder().encodeToString(privateKey.encoded)
                return "-----BEGIN RSA PRIVATE KEY-----\n$encoded\n-----END RSA PRIVATE KEY-----"
            } else {
                return null
            }
        }

        private fun loadPublicKey(): PublicKey? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val content = hardocedpublic.replace("-----BEGIN RSA PUBLIC KEY-----", "").replace("\n", "").replace("-----END RSA PUBLIC KEY-----", "")
                val data = Base64.getDecoder().decode(content.toByteArray())
                val spec = X509EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                return fact.generatePublic(spec)
            }else{
                return null;
            }
        }

        private fun loadPrivateKey():PrivateKey? {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val content = hardcodedPrivateKey.replace("\n", "").replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "")
                val data = Base64.getDecoder().decode(content.toByteArray())
                val spec = PKCS8EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                return fact.generatePrivate(spec)
            }else{
                return null;
            }
        }

        const val hardocedpublic = "-----BEGIN RSA PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCM0hvqGKJNa9H39Lp+KpmYpdDXSkriyU9WSMVMkNo7gVt7b98tq5TEoKOKZ4UBjR2u1yYP0AD5qLvoZ4fLPZQxFDoVXq+8NFiJBEdHQkr7HyU7jef2h7o/i++gTETOZr/hl+Ubyqyeulb/zqM8TQgl9oVZ0pCTYdZbLt6W+3JXlQIDAQAB\n" +
                "-----END RSA PUBLIC KEY-----"
        fun encrypt(plainText:String):String?{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey())
                val encryptedBytes = cipher.doFinal(plainText.toByteArray());
                return Base64.getEncoder().encodeToString(encryptedBytes)
            } else {
                return null
            }
        }

        val hardcodedPrivateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIzSG+oYok1r0ff0un4qmZil0NdKSuLJT1ZIxUyQ2juBW3tv3y2rlMSgo4pnhQGNHa7XJg/QAPmou+hnh8s9lDEUOhVer7w0WIkER0dCSvsfJTuN5/aHuj+L76BMRM5mv+GX5RvKrJ66Vv/OozxNCCX2hVnSkJNh1lsu3pb7cleVAgMBAAECgYAL+7MxaNai4cQOQBtm/uc8J9waMST0Bu9DGrhjZOFiD+0OsRa1gBtBEjrO3WYVm2iruklvlG+iaLsfMnQwfA7lb/PaxoXPqQq2SC/4vu+QSPaj+fEkput/6aoP35bBS3N1nY3/rvTHcWg4yDW2XGJ9Kw+TqoBAupjkm11Oi4cXrwJBAMMDmO7YXHfLg0cZjO9CBUNkMLopcF8URp1kbMIcBnlhs+OaebhKZQBTW+WO5nPyQJUnGYW044fERIyFqGfMVUsCQQC42+mOMq+vOMK3EP7GiF/Izve2sg1kgRng5dPADtwklTfx2QBg2TeJo1Nk32Q077mGvHseGY8eyGYsYQY5h1qfAkEAmsrcNZnfq+cKiQEq8S8CZn+8fcguvgmmE4CHI3yDVw3KcDtJqpLCoQOJyiknm5kK0ll61LrEcBvGHZRqHBoBCQJARx7khn7dtK5Dy6bQPpWtNFbcWv+5w1cgG7SQeS2+aJy4P2XbjWdExMF/jimcJfmtCTdurq/qrk8vcBeMC4oVxQJBAJUkteeTTV6Quh7wH1BP/4XHtPBExGpJv8ZTiD2wqyM3Nvs3buhDNc9aFXMsa+Yhqy+6XWMEkGUgtjutXbWmxfY=\n" +
                "-----END RSA PRIVATE KEY-----"
        fun decrypt(encrypted:String):String?{
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
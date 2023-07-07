import android.os.Build
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

abstract class EncryptTools {}

class CryptoException(
    val code: String? = null,
    override val message: String? = null
) : Throwable(message)

data class RSAKey(
    /**
     * encoded private key
     **/
    val privateKey: String,
    /**
     * encoded public key
     **/
    val publicKey: String
)

class CryptoRSA : EncryptTools() {

    companion object {
        private fun encodedPublicKey(key: PublicKey): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().encodeToString(key.encoded)
            } else {
                return android.util.Base64.encodeToString(
                    key.encoded,
                    android.util.Base64.DEFAULT
                )
            }
        }

        private fun encodedPrivateKey(key: PrivateKey): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().encodeToString(key.encoded)
            } else {
                android.util.Base64.encodeToString(
                    key.encoded,
                    android.util.Base64.DEFAULT
                )
            }
        }

        fun generateKey(): RSAKey {
            val keyGen = KeyPairGenerator.getInstance("RSA")
            keyGen.initialize(1024)
            val keyPair = keyGen.generateKeyPair()
            val publicKey = encodedPublicKey(keyPair.public)
            val privateKey = encodedPrivateKey(keyPair.private)
            return RSAKey(privateKey = privateKey, publicKey = publicKey)
        }

        private fun loadPublicKey(encodedPublicKey: String): PublicKey {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val data = Base64.getDecoder().decode(encodedPublicKey.toByteArray())
                val spec = X509EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                fact.generatePublic(spec)
            } else {
                val data =
                    android.util.Base64.decode(
                        encodedPublicKey.toByteArray(),
                        android.util.Base64.DEFAULT
                    )
                val spec = X509EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                fact.generatePublic(spec)
            }
        }

        private fun loadPrivateKey(encodedPrivateKey: String): PrivateKey {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val data = Base64.getDecoder().decode(encodedPrivateKey.toByteArray())
                val spec = PKCS8EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                fact.generatePrivate(spec)
            } else {
                val data =
                    android.util.Base64.decode(
                        encodedPrivateKey.toByteArray(),
                        android.util.Base64.DEFAULT
                    )
                val spec = PKCS8EncodedKeySpec(data)
                val fact: KeyFactory = KeyFactory.getInstance("RSA")
                fact.generatePrivate(spec)
            }
        }

        fun encrypt(plainText: String, encodedPublicKey: String): String? {
            return try {
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey(encodedPublicKey))
                val encryptedBytes = cipher.doFinal(plainText.toByteArray())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Base64.getEncoder().encodeToString(encryptedBytes)
                } else {
                    android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
                }
            } catch (e: Throwable) {
                null
            }
        }


        fun decrypt(encrypted: String, encodedPrivateKey: String): String? {
            return try {
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey(encodedPrivateKey))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String(cipher.doFinal(Base64.getDecoder().decode(encrypted)))
                } else {
                    String(
                        cipher.doFinal(
                            android.util.Base64.decode(
                                encrypted,
                                android.util.Base64.DEFAULT
                            )
                        )
                    )
                }
            } catch (e: Throwable) {
                null
            }
        }
    }
}

enum class AESMethod {
    CBC,
    ECB,
}

enum class PaddingScheme {
    PKCS5,
    PKCS7,
}

class CryptoAES : EncryptTools() {
    companion object {

        private fun getPaddingScheme(scheme: PaddingScheme): String {
            return when (scheme) {
                PaddingScheme.PKCS7 -> {
                    "PKCS7Padding"
                }

                PaddingScheme.PKCS5 -> {
                    "PKCS5Padding"
                }

                else -> {
                    throw CryptoException(code = "03", "PADDING SCHEME NOT FOUND")
                }
            }
        }

        fun encrypt(
            plainText: String,
            secretKey: String,
            method: AESMethod = AESMethod.CBC,
            padding: PaddingScheme = PaddingScheme.PKCS7
        ): String? {
            try {
                if (plainText.isEmpty()) {
                    throw CryptoException(code = "01", message = "TEXT CANNOT BE EMPTY")
                }

                if (secretKey.length != 32) {
                    throw CryptoException(code = "00", message = "KEY MUST BE 32 LENGTH")
                }

                return when (method) {
                    AESMethod.ECB -> {
                        encryptECB(plainText, secretKey, padding)
                    }

                    AESMethod.CBC -> {
                        encryptCBC(plainText, secretKey, padding)
                    }

                    else -> {
                        throw CryptoException(code = "03", message = "AES METHOD NOT FOUND")
                    }
                }
            } catch (e: CryptoException) {
                return null
            } catch (e: Throwable) {
                return null
            }
        }

        private fun encryptCBC(
            plainText: String,
            secretKey: String,
            padding: PaddingScheme
        ): String {
            val key = SecretKeySpec(secretKey.toByteArray(), "AES")
            val iv = IvParameterSpec(ByteArray(16))
            val cipher = Cipher.getInstance("AES/CBC/${getPaddingScheme(padding)}")
            cipher.init(Cipher.ENCRYPT_MODE, key, iv)
            val encryptedBytes = cipher.doFinal(plainText.toByteArray());
            val encodedEncryptedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().encodeToString(encryptedBytes)
            } else {
                android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
            }
            return encodedEncryptedText
        }

        private fun encryptECB(
            plainText: String,
            secretKey: String,
            padding: PaddingScheme
        ): String {
            val key = SecretKeySpec(secretKey.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/ECB/${getPaddingScheme(padding)}")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val encryptedBytes = cipher.doFinal(plainText.toByteArray());
            val encodedEncryptedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().encodeToString(encryptedBytes)
            } else {
                android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
            }
            return encodedEncryptedText
        }

        fun decrypt(
            encryptedText: String,
            secretKey: String,
            method: AESMethod = AESMethod.CBC,
            padding: PaddingScheme = PaddingScheme.PKCS7
        ): String? {
            try {
                if (encryptedText.isEmpty()) {
                    throw CryptoException(code = "01", message = "ENCRYPTED TEXT CANNOT BE EMPTY")
                }

                if (secretKey.length != 32) {
                    throw CryptoException(code = "00", message = "KEY MUST BE 32 LENGTH")
                }

                return when (method) {
                    AESMethod.ECB -> {
                        decryptECB(encryptedText, secretKey, padding)
                    }

                    AESMethod.CBC -> {
                        decryptCBC(encryptedText, secretKey, padding)
                    }

                    else -> {
                        throw CryptoException(code = "03", message = "AES METHOD NOT FOUND")
                    }
                }
            } catch (e: CryptoException) {
                return null
            } catch (e: Throwable) {
                return null
            }
        }

        private fun decryptCBC(
            encryptedText: String,
            secretKey: String,
            padding: PaddingScheme
        ): String {
            val key = SecretKeySpec(secretKey.toByteArray(), "AES")
            val iv = IvParameterSpec(ByteArray(16))
            val cipher = Cipher.getInstance("AES/CBC/${getPaddingScheme(padding)}")
            cipher.init(Cipher.DECRYPT_MODE, key, iv)
            val decrypted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String(
                    cipher.doFinal(
                        Base64.getDecoder().decode(encryptedText)
                    )
                )
            } else {
                String(
                    cipher.doFinal(
                        android.util.Base64.decode(
                            encryptedText,
                            android.util.Base64.DEFAULT
                        )
                    )
                )
            }
            return decrypted
        }

        private fun decryptECB(
            encryptedText: String,
            secretKey: String,
            padding: PaddingScheme
        ): String {
            val key = SecretKeySpec(secretKey.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/ECB/${getPaddingScheme(padding)}")
            cipher.init(Cipher.DECRYPT_MODE, key)
            val decrypted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String(
                    cipher.doFinal(
                        Base64.getDecoder().decode(encryptedText)
                    )
                )
            } else {
                String(
                    cipher.doFinal(
                        android.util.Base64.decode(
                            encryptedText,
                            android.util.Base64.DEFAULT
                        )
                    )
                )
            }
            return decrypted
        }
    }
}

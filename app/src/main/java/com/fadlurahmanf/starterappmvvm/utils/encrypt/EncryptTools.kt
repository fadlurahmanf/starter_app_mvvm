import android.os.Build
import com.fadlurahmanf.starterappmvvm.utils.encrypt.RSAHelper
import com.fadlurahmanf.starterappmvvm.utils.logging.logd
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

abstract class EncryptTools {}

class EncryptException(
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

class EncryptRSA : EncryptTools() {

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
                val encryptedBytes = cipher.doFinal(plainText.toByteArray());
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
                    cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey(encodedPrivateKey))
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

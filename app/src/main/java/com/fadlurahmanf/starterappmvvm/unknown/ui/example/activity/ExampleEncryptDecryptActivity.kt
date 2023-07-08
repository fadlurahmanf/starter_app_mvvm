package com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity

import com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation.CryptoRSA
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation.RSAHelper
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleEncryptDecryptBinding
import com.fadlurahmanf.starterappmvvm.feature.encrypt.domain.common.AESMethod
import com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation.CryptoED25519
import com.fadlurahmanf.starterappmvvm.feature.encrypt.domain.common.PaddingScheme
import java.util.Random

class ExampleEncryptDecryptActivity :
    BaseActivity<ActivityExampleEncryptDecryptBinding>(ActivityExampleEncryptDecryptBinding::inflate) {
    override fun initSetup() {
        initAction()
    }

    override fun inject() {

    }

    private fun initAction() {
        binding.btnGenerateKey.setOnClickListener {
            RSAHelper.generateKey(RSAHelper.METHOD.PKCS1PEM)
            val public = RSAHelper.encodedPublicKey(RSAHelper.publicKey)
            val private = RSAHelper.encodedPrivateKey(RSAHelper.privateKey)
            binding.generatedKey.text = "$public⭐⭐⭐$private"

            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("GeneratedKey", binding.generatedKey.text)
            cm.setPrimaryClip(clipData)
        }

        binding.btnEncrypt.setOnClickListener {
            if (binding.etPlainText.text.isEmpty()) return@setOnClickListener

            val result = RSAHelper.encrypt(binding.etPlainText.text.toString())
            binding.tvEncrypted.text = "Encrypted:$result"

            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Encrypted", result)
            cm.setPrimaryClip(clipData)
        }

        binding.btnDecrypt.setOnClickListener {
            if (binding.etEncryptedText.text.isEmpty()) return@setOnClickListener

            val result = RSAHelper.decrypt(binding.etEncryptedText.text.toString())
            binding.tvDecrypted.text = "Decrypted:$result"

            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Decrypted", result)
            cm.setPrimaryClip(clipData)
        }

        binding.btnEncryptRsa.setOnClickListener {
            val rsa = CryptoRSA()
            val key = rsa.generateKey()
            val message = "TES TES OI"
            val encrypted = rsa.encrypt(message, key.publicKey)
            println("MASUK ENCRYPTED: $encrypted")
            val decrypted = rsa.decrypt(encrypted ?: "", key.privateKey)
            println("MASUK DECRYPT KEDUA: $decrypted")
        }

        binding.btnEncryptAes.setOnClickListener {
            val aes = CryptoAES()
            val key = List(32, init = {
                Random().nextInt(9).toString()
            }).joinToString("")
            val encrypted = aes.encrypt("TES TES", key, AESMethod.CBC, PaddingScheme.PKCS7)
            println("masuk encrypted: $encrypted")
            val decrypted = aes.decrypt(encrypted ?: "", key, AESMethod.CBC, PaddingScheme.PKCS7)
            println("masuk decrypted: $decrypted")
        }

        binding.btnEncryptEdd.setOnClickListener {
            val ed25519 = CryptoED25519()
            val key = ed25519.generateKey()
            val signature = ed25519.generateSignature("TES SIGNATURE", key.privateKey)
            println(
                "MASUK VERIFY: ${
                    ed25519.verifySignature(
                        "TES SIGNATURE",
                        signature ?: "",
                        key.publicKey
                    )
                }"
            )
        }
    }

}
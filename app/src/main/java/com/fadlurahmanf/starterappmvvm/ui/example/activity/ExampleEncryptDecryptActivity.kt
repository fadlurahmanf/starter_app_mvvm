package com.fadlurahmanf.starterappmvvm.ui.example.activity

import CryptoRSA
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.utils.encrypt.RSAHelper
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleEncryptDecryptBinding
import com.fadlurahmanf.starterappmvvm.utils.encrypt.AESMethod
import com.fadlurahmanf.starterappmvvm.utils.encrypt.CryptoAES
import com.fadlurahmanf.starterappmvvm.utils.encrypt.PaddingScheme

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
            val key = "12345678901234567890123456789012"
            val encrypted = aes.encrypt("TES TES", key, AESMethod.CBC, PaddingScheme.NoPadding)
            println("masuk encrypted: $encrypted")
            val decrypted = aes.decrypt(encrypted ?: "", key, AESMethod.CBC, PaddingScheme.NoPadding)
            println("masuk decrypted: $decrypted")
        }
    }

}
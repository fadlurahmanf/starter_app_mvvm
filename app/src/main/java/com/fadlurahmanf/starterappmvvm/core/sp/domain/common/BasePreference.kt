package com.fadlurahmanf.starterappmvvm.core.sp.domain.common

import android.content.Context
import android.content.SharedPreferences
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.exception.CustomException
import com.fadlurahmanf.starterappmvvm.feature.encrypt.data.model.CryptoKey
import com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation.CryptoRSA
import com.fadlurahmanf.starterappmvvm.core.sp.data.constant.SpConstant
import com.google.gson.Gson

abstract class BasePreference(context: Context, private val crypto: CryptoRSA) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
    private val cryptoKey: CryptoKey

    companion object {
        const val SP_KEY: String = SpConstant.SP_KEY
        private const val PRIVATE_KEY: String = SpConstant.PRIVATE_KEY
        private const val PUBLIC_KEY: String = SpConstant.PUBLIC_KEY
    }

    init {
        val privateKey = getRawString(PRIVATE_KEY, null)
        val publicKey = getRawString(PUBLIC_KEY, null)
        cryptoKey = if (privateKey != null && publicKey != null) {
            CryptoKey(privateKey, publicKey)
        } else {
            val key = crypto.generateKey()
            saveRawString(PRIVATE_KEY, key.privateKey)
            saveRawString(PUBLIC_KEY, key.publicKey)
            key
        }
    }

    /**
     * input plain text string, later will be saved without encrypted
     * */
    protected fun saveRawString(key: String, value: String) {
        try {
            sharedPreferences.edit().putString(key, value).apply()
        } catch (e: Throwable) {
            logConsole.e("SAVE RAW STRING: ${e.message}")
        }
    }

    /**
     * save encrypted string
     * */
    private fun saveEncryptedString(key: String, value: String) {
        if (value.isEmpty()) {
            throw CustomException()
        }
        val encrypted = crypto.encrypt(value, cryptoKey.publicKey)
        sharedPreferences.edit().putString(key, encrypted).apply()
    }

    /**
     * input plain text string, later will be saved into encrypted text
     * */
    protected fun saveString(key: String, value: String) {
        try {
            saveEncryptedString(key, value)
        } catch (e: Throwable) {
            logConsole.e("SAVE STRING: ${e.message}")
        }
    }

    /**
     * get existing raw string, without decrypted
     * */
    protected fun getRawString(key: String, default: String? = null): String? {
        return try {
            sharedPreferences.getString(key, default)
        } catch (e: Throwable) {
            logConsole.e("GET RAW STRING: ${e.message}")
            null
        }
    }

    /**
     * get decrypted string
     * */
    private fun getDecryptedString(key: String): String? {
        val raw = getRawString(key, null)
        if (raw.isNullOrEmpty()) {
            throw CustomException()
        }
        return crypto.decrypt(raw, cryptoKey.privateKey)
    }

    /**
     * get decrypted string. If the existing value is un-encrypted string, will thrown error or return null
     * */
    protected fun getString(key: String): String? {
        return try {
            getDecryptedString(key)
        } catch (e: Throwable) {
            logConsole.e("GET STRING: ${e.message}")
            null
        }
    }

    // save raw int
    protected fun saveRawInt(key: String, value: Int) {
        try {
            sharedPreferences.edit().putInt(key, value).apply()
        } catch (e: Throwable) {
            logConsole.e("SAVE INT: ${e.message}")
        }
    }

    // save encrypted int
    protected fun saveInt(key: String, value: Int) {
        try {
            saveEncryptedString(key, value.toString())
        } catch (e: Throwable) {
            logConsole.e("SAVE INT: ${e.message}")
        }
    }

    // get raw int
    private fun getRawInt(key: String, default: Int = 0): Int {
        return try {
            sharedPreferences.getInt(key, default)
        } catch (e: Throwable) {
            logConsole.e("GET RAW: ${e.message}")
            default
        }
    }

    protected fun getInt(key: String): Int? {
        return try {
            val decrypted = getDecryptedString(key)
            return decrypted?.toInt()
        } catch (e: Throwable) {
            logConsole.e("GET INT: ${e.message}")
            null
        }
    }

    // save raw long
    protected fun saveRawLong(key: String, value: Long) {
        try {
            sharedPreferences.edit().putLong(key, value).apply()
        } catch (e: Throwable) {
            logConsole.e("SAVE RAW LONG: ${e.message}")
        }
    }

    protected fun saveLong(key: String, value: Long) {
        try {
            saveEncryptedString(key, value.toString())
        } catch (e: Throwable) {
            logConsole.e("SAVE LONG: ${e.message}")
        }
    }

    protected fun getRawLong(key: String, default: Long = 0L): Long {
        return try {
            sharedPreferences.getLong(key, default)
        } catch (e: Throwable) {
            0L
        }
    }

    protected fun getLong(key: String): Long? {
        return try {
            val decrypted = getDecryptedString(key)
            decrypted?.toLong()
        } catch (e: Throwable) {
            logConsole.e("GET LONG: ${e.message}")
            null
        }
    }

    protected fun saveRawFloat(key: String, value: Float) {
        try {
            sharedPreferences.edit()?.putFloat(key, value)?.apply()
        } catch (e: Throwable) {
            logConsole.e("SAVE RAW FLOAT: ${e.message}")
        }
    }

    protected fun saveFloat(key: String, value: Float) {
        try {
            saveEncryptedString(key, value.toString())
        } catch (e: Throwable) {
            logConsole.e("SAVE FLOAT: ${e.message}")
        }
    }

    protected fun getFloat(key: String): Float? {
        return try {
            val decrypted = getDecryptedString(key)
            decrypted?.toFloat()
        } catch (e: Throwable) {
            logConsole.e("GET FLOAT: ${e.message}")
            null
        }
    }

    protected fun <T> saveData(key: String, value: T?) {
        try {
            val json = Gson().toJson(value)
            if (json.isNullOrEmpty()) {
                throw CustomException()
            }
            saveEncryptedString(key, json)
        } catch (e: Throwable) {
            logConsole.e("SAVE DATA: ${e.message}")
        }
    }

    protected fun <T> getData(key: String, classOfT: Class<T>): T? {
        return try {
            val decrypted = getDecryptedString(key)
            if (decrypted.isNullOrEmpty()) {
                throw CustomException()
            }
            return Gson().fromJson(decrypted, classOfT)
        } catch (e: Throwable) {
            logConsole.e("GET DATA: ${e.message}")
            null
        }
    }

    protected fun <T> saveListData(key: String, value: ArrayList<T>) {
        try {
            val json = Gson().toJson(value)
            if (json.isNullOrEmpty()) {
                throw CustomException()
            }
            saveEncryptedString(key, json)
        } catch (e: Throwable) {
            logConsole.e("SAVE LIST DATA: ${e.message}")
        }
    }

    protected fun <T> getListData(key: String, classOfT: Class<T>): ArrayList<T>? {
        return try {
            val decrypted: String? = getDecryptedString(key)
            val list: ArrayList<T> = arrayListOf<T>()
            if (decrypted.isNullOrEmpty()) {
                throw CustomException()
            }
            val decryptedJSONArray = Gson().fromJson<ArrayList<T>>(decrypted, ArrayList::class.java)
            decryptedJSONArray.forEach {
                list.add(Gson().fromJson(Gson().toJson(it), classOfT))
            }
            list
        } catch (e: Throwable) {
            logConsole.e("GET LIST DATA: ${e.message}")
            null
        }
    }

    /**
     * Clear existing data
     * */
    protected fun clearData(key: String) {
        try {
            sharedPreferences.edit().remove(key).apply()
        } catch (e: Throwable) {
            logConsole.e("CLEAR DATA: ${e.message}")
        }
    }
}
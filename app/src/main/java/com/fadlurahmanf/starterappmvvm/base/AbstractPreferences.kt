package com.fadlurahmanf.starterappmvvm.base

import android.content.Context
import android.content.SharedPreferences
import com.f2prateek.rx.preferences.RxSharedPreferences
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import rx.Observable
import java.util.ArrayList

abstract class AbstractPreferences(context: Context) {

    private lateinit var sharedPreferences: SharedPreferences
    private var gson: Gson = Gson()
    private lateinit var rxSharedPreferences: RxSharedPreferences

    abstract fun getPreferencesGroup(): String

    init {
        initPreferences(context, getPreferencesGroup())
    }

    private fun initPreferences(context: Context, preferencesGroup: String) {
        sharedPreferences = context.getSharedPreferences(preferencesGroup, Context.MODE_PRIVATE)
        rxSharedPreferences = RxSharedPreferences.create(sharedPreferences)
    }

    protected fun getRxSharedPreferences(): RxSharedPreferences {
        return rxSharedPreferences
    }

    protected fun saveData(tag: String, value: String) {
        rxSharedPreferences.getString(tag).set(value)
    }

    protected fun saveData(tag: String, value: Long?) {
        rxSharedPreferences.getLong(tag).set(value)
    }

    protected fun saveData(tag: String, value: Boolean?) {
        rxSharedPreferences.getBoolean(tag).set(value)
    }

    protected fun saveData(tag: String, value: Int?) {
        rxSharedPreferences.getInteger(tag).set(value)
    }

    protected fun saveData(tag: String, value: Float?) {
        rxSharedPreferences.getFloat(tag).set(value)
    }

    protected fun <T> saveData(tag: String, obj: T) {
        saveData(tag, gson.toJson(obj))
    }

    protected fun <T> saveDataList(tag: String, objList: List<T>) {
        saveData(tag, gson.toJson(objList))
    }

    protected fun <T> getDataList(tag: String, classOfT: Class<T>): List<T>? {
        val rawData = rxSharedPreferences.getString(tag).get() ?: return null
        val dataList = ArrayList<T>()
        try {
            val jsonArray = JSONArray(rawData)
            for (i in 0 until jsonArray.length()) {
                val row = jsonArray.getJSONObject(i)
                dataList.add(gson.fromJson<T>(row.toString(), classOfT))
            }
        } catch (e: JSONException) {
        }

        return dataList
    }

    protected fun <T> getData(tag: String, classOfT: Class<T>): T {
        return try {
            val rawData = rxSharedPreferences.getString(tag).get()
            gson.fromJson(rawData, classOfT)
        } catch (_: Exception) {
            clearData(tag)
            getData(tag, classOfT)
        }
    }

    protected fun <T> getDataAsObservable(tag: String, classOfT: Class<T>): Observable<T> {
        return rxSharedPreferences.getString(tag)
            .asObservable()
            .map { rawData -> gson.fromJson(rawData, classOfT) }
    }

    fun clearData(tag: String): Boolean {
        if (sharedPreferences != null) {
            sharedPreferences.edit()?.remove(tag)?.apply()
            return true
        }
        return false
    }

    protected fun getString(tag: String): String? {
        return rxSharedPreferences.getString(tag, null)?.get()
    }

    protected fun getLong(tag: String): Long {
        return rxSharedPreferences.getLong(tag).get() ?: return 0
    }

    protected fun getBoolean(tag: String): Boolean {
        return rxSharedPreferences.getBoolean(tag).get() ?: return false
    }

    protected fun getInteger(tag: String): Int {
        return rxSharedPreferences.getInteger(tag).get() ?: return 0
    }

    protected fun getFloat(tag: String): Float {
        return rxSharedPreferences.getFloat(tag).get() ?: return 0f
    }
}

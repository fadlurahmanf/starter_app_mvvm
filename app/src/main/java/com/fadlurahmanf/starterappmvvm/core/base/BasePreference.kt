package com.fadlurahmanf.starterappmvvm.core.base

import android.content.Context
import android.content.SharedPreferences
import com.fadlurahmanf.starterappmvvm.core.constant.SpKey
import com.google.gson.Gson
import org.json.JSONArray

abstract class BasePreference(context: Context) {

    private var sharedPreferences: SharedPreferences ?= null

    init {
        sharedPreferences = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
    }

    companion object{
        const val SP_KEY:String = SpKey.SP_KEY
    }

    protected fun saveString(key:String, value:String){
        sharedPreferences?.edit()?.putString(key, value)?.apply()
    }

    protected fun getString(key: String):String?{
        return sharedPreferences?.getString(key, null)
    }

    protected fun saveInt(key: String, value: Int){
        sharedPreferences?.edit()?.putInt(key, value)?.apply()
    }

    protected fun getInt(key: String):Int?{
        return sharedPreferences?.getInt(key, 0)
    }

    protected fun saveLong(key: String, value: Long){
        sharedPreferences?.edit()?.putLong(key, value)?.apply()
    }

    protected fun getLong(key: String):Long?{
        return sharedPreferences?.getLong(key, 0)
    }

    protected fun saveFloat(key: String, value: Float){
        sharedPreferences?.edit()?.putFloat(key, value)?.apply()
    }

    protected fun getFloat(key: String):Float?{
        return sharedPreferences?.getFloat(key, 0f)
    }

    protected fun <T> saveData(key: String, value:T?){
        if (!Gson().toJson(value).isNullOrEmpty()){
            sharedPreferences?.edit()?.putString(key, Gson().toJson(value))?.apply()
        }
    }

    protected fun <T> getData(key: String, classOfT:Class<T>): T?{
        return try {
            val rawString:String? = getString(key)
            if (rawString != null){
                Gson().fromJson(rawString, classOfT)
            }else{
                null
            }
        }catch (e:Exception){
            null
        }
    }

    protected fun <T> saveListData(key: String, value: ArrayList<T>){
        sharedPreferences?.edit()?.putString(key, Gson().toJson(value))?.apply()
    }

    protected fun <T> getListData(key: String, classOfT:Class<T>): ArrayList<T>?{
        try {
            val rawString:String? = getString(key)
            val list:ArrayList<T> = arrayListOf<T>()
            list.clear()
            return if (rawString!=null){
                val jsonArray = JSONArray(rawString)
                for (i in 0 until jsonArray.length()){
                    val row = jsonArray.getJSONObject(i)
                    list.add(Gson().fromJson(row.toString(), classOfT))
                }
                list
            }else{
                null
            }
        }catch (e:Exception){
            return null
        }
    }

    protected fun clearData(key: String){
        sharedPreferences?.edit()?.remove(key)?.apply()
    }
}

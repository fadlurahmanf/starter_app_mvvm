package com.fadlurahmanf.starterappmvvm.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.fadlurahmanf.starter_app_mvp.core.constant.ParamsKeySP
import com.google.gson.Gson
import org.json.JSONArray

abstract class BasePreference(context: Context) {
    companion object{
        const val SP_KEY:String = ParamsKeySP.PARAMS_SP_KEY
    }

    private var sharedPreferences: SharedPreferences?= null

    init {
        sharedPreferences = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
    }

    protected fun saveData(key:String, value:String?){
        sharedPreferences?.edit()?.putString(key, value)?.apply()
    }

    protected fun saveData(key: String, value: Int?){
        sharedPreferences?.edit()?.putInt(key, value?:0)?.apply()
    }

    protected fun saveData(key: String, value:Float?){
        sharedPreferences?.edit()?.putFloat(key, value?:0f)?.apply()
    }

    protected fun <T> saveData(key: String, value:T?){
        sharedPreferences?.edit()?.putString(key, Gson().toJson(value))?.apply()
    }

    protected fun <T> saveData(key: String, value: List<T>){
        sharedPreferences?.edit()?.putString(key, Gson().toJson(value))?.apply()
    }

    protected fun <T:String> getData(key: String, defValue: String?=null, classOfT:Class<T>) : String?{
        return sharedPreferences?.getString(key, defValue)
    }

    protected fun <T:Int> getData(key: String, devValue:Int?=null, classOfT:Class<T>): Int?{
        return sharedPreferences?.getInt(key, devValue?:0)
    }

    protected fun <T:Float> getData(key: String, defValue:Float?=null, classOfT:Class<T>): Float?{
        return sharedPreferences?.getFloat(key, defValue?:0f)
    }

    @SuppressLint("CommitPrefEdits")
    protected fun <T:Any> getData(key: String, classOfT:Class<T>) : T?{
        return try {
            var raw:String? = sharedPreferences?.getString(key, null)
            Gson().fromJson(raw, classOfT)
        }catch (e: Exception){
            sharedPreferences?.edit()?.remove(key)
            null
        }
    }

    protected fun <T> getDataList(key: String, classOfT:Class<T>): ArrayList<T>?{
        return try {
            var raw = sharedPreferences?.getString(key, null)
            var dataList : ArrayList<T> = arrayListOf<T>()
            dataList.clear()
            var jsonArray = JSONArray(raw)
            for (i in 0 until jsonArray.length()){
                var row = jsonArray.getJSONObject(i)
                dataList.add(Gson().fromJson(row.toString(), classOfT))
            }
            dataList
        }catch (e: Exception){
            null
        }
    }

    protected fun clearData(key: String){
        sharedPreferences?.edit()?.remove(key)
    }
}

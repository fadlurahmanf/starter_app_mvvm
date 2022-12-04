package com.fadlurahmanf.starterappmvvm.dto.exception

import android.content.Context
import androidx.annotation.StringRes
import com.fadlurahmanf.starterappmvvm.R
import okio.IOException

class CustomException(
    var statusCode:Int? = null,
    var rawMessage:String? = null,
    var rawMessageId:Int? = null,
    var properMessage:String? = null
) : IOException() {
    fun toProperMessage(context: Context):String{
        try {
            if(properMessage != null){
                return properMessage!!
            }

            if(rawMessage == null){
                return context.getString(R.string.exception_general)
            }

            val identifier = context.resources.getIdentifier(rawMessage, "string", context.packageName)
            if(identifier == 0){
                return context.getString(R.string.exception_general)
            }

            if(rawMessage == "exception_offline"){
                return context.getString(R.string.exception_offline)
            }else{
                return "Terjadi kesalahan"
            }
        }catch (e:Exception){
            return context.getString(R.string.exception_general)
        }
    }
}

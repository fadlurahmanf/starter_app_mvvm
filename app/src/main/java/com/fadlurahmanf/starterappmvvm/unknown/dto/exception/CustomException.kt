package com.fadlurahmanf.starterappmvvm.unknown.dto.exception

import android.content.Context
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.constant.ExceptionConstant
import com.fadlurahmanf.starterappmvvm.unknown.utils.logging.cLoge
import okio.IOException

class CustomException(
    var statusCode:Int? = null,
    var rawMessage:String? = null,
    var data:HashMap<String, Any>? = null,
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

            val identifierLowercase = context.resources.getIdentifier(rawMessage?.lowercase(), "string", context.packageName)
            val identifierUppercase = context.resources.getIdentifier(rawMessage?.uppercase(), "string", context.packageName)
            if(identifierLowercase == 0 && identifierUppercase == 0){
                return context.getString(R.string.exception_general)
            }

            return when(rawMessage){
                ExceptionConstant.offline -> context.getString(R.string.exception_offline)
                else -> {
                    if (identifierLowercase != 0) context.getString(identifierLowercase)
                    else context.getString(identifierUppercase)
                }
            }
        } catch (e:Exception) {
            context.cLoge(e)
            return context.getString(R.string.exception_general)
        }
    }
}

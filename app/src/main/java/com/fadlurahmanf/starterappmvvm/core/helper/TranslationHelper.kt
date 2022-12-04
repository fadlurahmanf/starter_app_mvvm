package com.fadlurahmanf.starterappmvvm.core.helper

import android.content.Context
import java.util.Locale

class TranslationHelper {
    companion object{
        fun getCurrentLocale(context: Context):Locale{
            val configuration = context.resources.configuration
            return configuration.locale
        }

        @Suppress("DEPRECATION")
        fun changeLanguage(context: Context, languageId:String){
            if(!checkIsSupported(languageId)) return

            val configuration = context.resources.configuration
            val local = Locale(languageId)
            Locale.setDefault(local)
            configuration.locale = local
            configuration.setLayoutDirection(local)
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

        }

        private fun checkIsSupported(languageId: String):Boolean{
            return languageId == "en" || languageId == "in";
        }
    }
}
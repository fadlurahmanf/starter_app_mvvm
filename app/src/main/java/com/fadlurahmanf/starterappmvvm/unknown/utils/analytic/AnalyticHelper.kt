package com.fadlurahmanf.starterappmvvm.unknown.utils.analytic

import android.content.Context
import android.os.Bundle
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.fadlurahmanf.starterappmvvm.unknown.utils.logging.logd
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticHelper {
    val firebase = Firebase()
    val appsFlyer = AppsFlyer()

    companion object {

        fun logEvent(context: Context, event: String, map: Map<String, Any>) {
            Firebase.logEvent(context, event, map)
            AppsFlyer.logEvent(context, event, map)
            Facebook.logEvent(context, event)
        }

        fun logEvent(context: Context, event: String) {
            Firebase.logEvent(context, event)
            AppsFlyer.logEvent(context, event)
            Facebook.logEvent(context, event)
        }

        private fun mapToBundle(map:Map<String, Any>):Bundle {
            val bundle = Bundle()
            map.keys.forEach {
                when (val v = map[it]) {
                    is String -> {
                        bundle.putString(it, v)
                    }
                    is Int -> {
                        bundle.putInt(it, v)
                    }
                    is Double -> {
                        bundle.putDouble(it, v)
                    }
                }
            }
            return bundle
        }
    }

    class Firebase {
        companion object {
            fun logEvent(context: Context, event: String, map: Map<String, Any>) {
                val bundle = mapToBundle(map)
                FirebaseAnalytics.getInstance(context).logEvent(event, bundle)
                logd("LOG ITEM $event SUCCESS SENT TO FIREBASE DASHBOARD")
            }

            fun logEvent(context: Context, event: String) {
                val bundle = Bundle()
                FirebaseAnalytics.getInstance(context).logEvent(event, bundle)
                logd("LOG ITEM $event SUCCESS SENT TO FIREBASE DASHBOARD")
            }
        }
    }

    class AppsFlyer {
        companion object {
            fun logEvent(context: Context, event: String, map: Map<String, Any>) {
                AppsFlyerLib.getInstance().logEvent(context, event, map, object : AppsFlyerRequestListener {
                    override fun onSuccess() {
                        logd("LOG ITEM $event SUCCESS SENT TO APPSFLYER DASHBOARD")
                    }

                    override fun onError(p0: Int, p1: String) {
                        logd("LOG ITEM $event FAILED SENT TO APPSFLYER DASHBOARD, CODE: $p0, MESSAGE: $p1")
                    }
                })
            }

            fun logEvent(context: Context, event: String) {
                AppsFlyerLib.getInstance().logEvent(context, event, mapOf(), object : AppsFlyerRequestListener {
                    override fun onSuccess() {
                        logd("LOG ITEM $event SUCCESS SENT TO APPSFLYER DASHBOARD")
                    }

                    override fun onError(p0: Int, p1: String) {
                        logd("LOG ITEM $event FAILED SENT TO APPSFLYER DASHBOARD, CODE: $p0, MESSAGE: $p1")
                    }
                })
            }
        }
    }

    class Facebook {
        companion object {
            fun logEvent(context: Context, event: String) {
                AppEventsLogger.newLogger(context).logEvent(event)
                logd("LOG ITEM $event SUCCESS SENT TO FACEBOOK APP EVENT DASHBOARD")
            }
        }
    }
}
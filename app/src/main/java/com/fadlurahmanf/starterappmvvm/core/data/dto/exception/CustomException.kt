package com.fadlurahmanf.starterappmvvm.core.data.dto.exception

import android.content.Context
import androidx.annotation.StringRes
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.constant.ExceptionConstant
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole

class CustomException(
    var httpStatusCode: Int? = null,
    var statusCode: String? = null,
    var rawMessage: String? = null,
    @StringRes var idRawMessage: Int? = null,
    override var message: String? = null,
    var title: String? = null,
    var data: HashMap<String, Any>? = null,
) : Throwable(message = message) {

    fun copyWith(
        httpStatusCode: Int? = null,
        statusCode: String? = null,
        rawMessage: String? = null,
        idRawMessage: Int? = null,
        message: String? = null,
        title: String? = null,
        data: HashMap<String, Any>? = null
    ): CustomException {
        return CustomException(
            httpStatusCode = httpStatusCode ?: this.httpStatusCode,
            statusCode = statusCode ?: this.statusCode,
            rawMessage = rawMessage ?: this.rawMessage,
            idRawMessage = idRawMessage ?: this.idRawMessage,
            message = message ?: this.message,
            title = title ?: this.title,
            data = data ?: this.data
        )
    }

    private fun toProperHttpStatusCode(context: Context): Int? {
        return try {
            httpStatusCode
        } catch (e: Throwable) {
            logConsole.e("ERROR toProperHttpStatusCode: ${e.message}")
            null
        }
    }

    private fun toProperStatusCode(context: Context): String? {
        return try {
            statusCode
        } catch (e: Throwable) {
            logConsole.e("ERROR toProperStatusCode: ${e.message}")
            null
        }
    }

    private fun toProperTitle(context: Context): String {
        return try {
            context.getString(R.string.oops)
        } catch (e: Throwable) {
            logConsole.e("ERROR toProperTitle: ${e.message}")
            context.getString(R.string.oops)
        }
    }

    fun toProperMessage(context: Context): String {
        try {
            message?.let {
                return it
            }

            if (rawMessage == null) {
                return context.getString(R.string.exception_general)
            }

            idRawMessage?.let {
                return context.getString(it)
            }

            val identifierLowercase = context.resources.getIdentifier(
                rawMessage?.lowercase(),
                "string",
                context.packageName
            )

            val identifierUppercase = context.resources.getIdentifier(
                rawMessage?.uppercase(),
                "string",
                context.packageName
            )

            if (identifierLowercase == 0 && identifierUppercase == 0) {
                return context.getString(R.string.exception_general)
            }

            return when (rawMessage) {
                ExceptionConstant.offline -> context.getString(R.string.exception_offline)
                else -> {
                    if (identifierLowercase != 0) context.getString(identifierLowercase)
                    else context.getString(identifierUppercase)
                }
            }
        } catch (e: Throwable) {
            logConsole.e("ERROR toProperMessage: ${e.message}")
            return context.getString(R.string.exception_general)
        }
    }

    fun toProperException(context: Context): CustomException {
        return try {
            if (rawMessage == null) {
                CustomException(
                    httpStatusCode = toProperHttpStatusCode(context),
                    statusCode = toProperStatusCode(context),
                    rawMessage = rawMessage,
                    idRawMessage = idRawMessage,
                    message = toProperMessage(context),
                    title = toProperTitle(context)
                )
            } else {
                this
            }
        } catch (e: Throwable) {
            logConsole.e("ERROR toProperException: ${e.message}")
            this
        }
    }
}

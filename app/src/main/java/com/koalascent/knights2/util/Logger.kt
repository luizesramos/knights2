package com.koalascent.knights2.util

import android.util.Log

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */
object Logger {

    private const val tag = "Knights"

    fun d(message: String) {
        Log.d(tag, message)
    }

    fun d(message: String, exception: Throwable) {
        Log.d(tag, message, exception)
    }

    fun e(message: String) {
        Log.e(tag, message)
    }

    fun e(message: String, exception: Throwable) {
        Log.e(tag, message, exception)
    }
}

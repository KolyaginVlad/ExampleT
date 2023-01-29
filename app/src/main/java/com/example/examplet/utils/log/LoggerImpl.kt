package com.example.examplet.utils.log

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class LoggerImpl @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
) : Logger {

    override fun error(throwable: Throwable) {
        throwable.printStackTrace()
        crashlytics.recordException(throwable)
    }

    override fun error(message: String) {
        Log.e(TAG, message)
    }

    override fun debug(message: String) {
        Log.d(TAG, message)
    }

    override fun info(message: String) {
        Log.i(TAG, message)
    }

    override fun event(
        event: String,
        vararg args: String
    ) {
        info("Sending event $event with args $args")
        //TODO Отправка в Analytics
    }

    companion object {
        const val TAG = "ExampleT"
    }
}
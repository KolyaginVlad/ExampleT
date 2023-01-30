package com.example.examplet.utils.log

import com.example.examplet.utils.analytics.AnalyticsEvent


interface Logger {

    fun error(throwable: Throwable)

    fun error(message: String)

    fun debug(message: String)

    fun info(message: String)

    fun event(
        event: AnalyticsEvent
    )
}
package com.example.examplet.utils.log


interface Logger {

    fun error(throwable: Throwable)

    fun error(message: String)

    fun debug(message: String)

    fun info(message: String)

    fun event(
        event: String,
        vararg args: String
    )
}
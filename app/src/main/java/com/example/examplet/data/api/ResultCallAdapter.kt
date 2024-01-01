package com.example.examplet.data.api

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Адаптер для поддержки [Result] в ответах
 */
internal class ResultCallAdapter<T>(
    private val successType: Type
) : CallAdapter<T, Call<Result<T>>> {

    override fun adapt(call: Call<T>): Call<Result<T>> = ResultCall(call, successType)

    override fun responseType(): Type = successType
}
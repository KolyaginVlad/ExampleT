package com.example.examplet.data.api

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

/**
 * Обертка Retrofit2 для поддержки [Result] в ответах
 */
internal class ResultCall<T>(
    private val delegate: Call<T>,
    private val successType: Type
) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) = delegate.enqueue(object : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback.onResponse(this@ResultCall, Response.success(response.toResult()))
        }

        private fun Response<T>.toResult(): Result<T> {
            if (!isSuccessful) {
                val errorBody = errorBody()?.string() ?: ""

                return Result.failure(Exception("REST Error (${code()}) for ${raw().request.url}. Error: $errorBody"))
            }

            body()?.let {
                return Result.success(it)
            }

            return if (successType == Unit::class.java) {
                @Suppress("UNCHECKED_CAST")
                Result.success(Unit) as Result<T>
            } else {
                Result.failure(UnknownError("Response body is null"))
            }
        }

        override fun onFailure(call: Call<T>, throwable: Throwable) {
            callback.onResponse(
                this@ResultCall,
                Response.success(Result.failure(throwable))
            )
        }
    })

    override fun execute() = throw UnsupportedOperationException()

    override fun isExecuted() = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled() = delegate.isCanceled

    override fun clone() = ResultCall(delegate, successType)

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

}
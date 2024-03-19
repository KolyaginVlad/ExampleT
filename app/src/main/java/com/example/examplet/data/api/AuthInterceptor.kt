package com.example.examplet.data.api

import com.example.examplet.domain.repositories.ApiRepository
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Provider

/**
 * Перехватчик, добавляющий Bearer токен для методов, отмеченных @Authorized
 * @see Authorized
 */
class AuthInterceptor @Inject constructor(
    private val authRepositoryProvider: Provider<ApiRepository>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.hasAnnotation(Authorized::class.java)) {
            val request = chain.request()

            var tryToRefreshToken = true
            while (true) {
                val token = authRepositoryProvider.get().getActualToken().getOrElse { t ->
                    return createFailedResponseFromRequest(request, t.message)
                }

                val response = chain.proceed(
                    request
                        .newBuilder()
                        .addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                        .build()
                )

                if (tryToRefreshToken && response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    tryToRefreshToken = false
                    response.close()
                    continue
                }

                return response
            }
        } else {
            return chain.proceed(chain.request())
        }
    }

    private fun createFailedResponseFromRequest(
        request: Request,
        message: String?,
    ): Response {
        val body = JsonObject()
        buildMap {
            put(ERROR_FIELD, JsonPrimitive(UNAUTHORIZED_ERROR_VALUE))
            if (message != null) {
                put(MESSAGE_FIELD, JsonPrimitive(message))
            }
        }
        body.apply {
            add(ERROR_FIELD, JsonPrimitive(UNAUTHORIZED_ERROR_VALUE))
            if (message != null) {
                add(MESSAGE_FIELD, JsonPrimitive(message))
            }
        }
        return Response.Builder()
            .code(HttpURLConnection.HTTP_UNAUTHORIZED)
            .body(body.toString().toResponseBody("application/json".toMediaType()))
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("Auth error")
            .build()
    }

    private companion object {
        const val BEARER_PREFIX = "Bearer "
        const val AUTHORIZATION_HEADER = "Authorization"
        const val ERROR_FIELD = "error"
        const val MESSAGE_FIELD = "message"
        const val UNAUTHORIZED_ERROR_VALUE = "authentication_failed"
    }
}
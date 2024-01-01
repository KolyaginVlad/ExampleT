package com.example.examplet.data.api

/**
 * Аннотация для Retrofit методов, для которых необходимо использование AuthInterceptor
 * @see AuthInterceptor
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorized
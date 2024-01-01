package com.example.examplet.data.api

import okhttp3.Interceptor
import retrofit2.Invocation

fun <T : Annotation> Interceptor.Chain.hasAnnotation(annotationClass: Class<T>): Boolean {
    return request().tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass) != null
}
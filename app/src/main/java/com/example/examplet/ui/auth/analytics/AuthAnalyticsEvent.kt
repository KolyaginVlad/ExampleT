package com.example.examplet.ui.auth.analytics

import com.example.examplet.utils.analytics.AnalyticsEvent

data class AuthAnalyticsEvent(
    val isSuccessful: Boolean,
) : AnalyticsEvent {
    override val name: String = "authorization"
    override val arguments: Map<String, Any?> = mapOf(
        "isSuccessful" to isSuccessful,
    )
}
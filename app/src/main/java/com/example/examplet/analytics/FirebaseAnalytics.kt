package com.example.examplet.analytics

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.Serializable
import javax.inject.Inject

class FirebaseAnalytics @Inject constructor(private val context: Context) {
    private val analytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    fun sendEvent(event: AnalyticsEvent) {
        val bundle = Bundle().apply {
            event.arguments.forEach { entry ->
                val value = entry.value
                if (value is Serializable) {
                    putSerializable(entry.key, value)
                } else {
                    putString(entry.key, value.toString())
                }
            }
        }
        analytics.logEvent(event.name, bundle)
        analytics.logEvent(
            FirebaseAnalytics.Event.SELECT_ITEM,
            bundleOf()
        )
    }
}

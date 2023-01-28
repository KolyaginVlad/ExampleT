package com.example.examplet.data.repositories

import com.example.examplet.analytics.FirebaseAnalytics
import com.example.examplet.data.models.mapToDtoe
import com.example.examplet.domain.models.UserCredentials
import com.example.examplet.domain.repositories.ApiRepository
import com.example.examplet.ui.auth.analytics.AuthAnalyticsEvent
import javax.inject.Inject
import kotlinx.coroutines.delay

class ApiRepositoryImpl @Inject constructor(
    private val analytics: FirebaseAnalytics,
) : ApiRepository {
    override suspend fun login(userCredentials: UserCredentials): Result<Unit> {
        val user = userCredentials.mapToDtoe()
        delay(1000)
        if (Math.random() > 0.5) {
            analytics.sendEvent(AuthAnalyticsEvent(true))
            return Result.success(Unit)
        } else {
            analytics.sendEvent(AuthAnalyticsEvent(false))
            return Result.failure(IllegalAccessException(
                "${user.login.takeIf { it.isNotBlank() } ?: "Man"}, you loser!")
            )
        }
    }
}

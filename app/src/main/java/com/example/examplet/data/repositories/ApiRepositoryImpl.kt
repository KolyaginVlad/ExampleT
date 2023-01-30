package com.example.examplet.data.repositories

import com.example.examplet.data.models.mapToDtoe
import com.example.examplet.domain.models.UserCredentials
import com.example.examplet.domain.repositories.ApiRepository
import javax.inject.Inject
import kotlinx.coroutines.delay

class ApiRepositoryImpl @Inject constructor() : ApiRepository {
    override suspend fun login(userCredentials: UserCredentials): Result<Unit> {
        val user = userCredentials.mapToDtoe()
        delay(1000)
        return if (Math.random() > 0.5) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalAccessException(
                "${user.login.takeIf { it.isNotBlank() } ?: "Man"}, you loser!")
            )
        }
    }
}

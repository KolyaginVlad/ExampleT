package com.example.examplet.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.examplet.data.AuthApi
import com.example.examplet.data.models.TokenDataExportDto
import com.example.examplet.data.models.mapToDtoe
import com.example.examplet.domain.models.Cat
import com.example.examplet.domain.models.Dog
import com.example.examplet.domain.models.JwtToken
import com.example.examplet.domain.models.UserCredentials
import com.example.examplet.domain.repositories.ApiRepository
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ApiRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val authApi: AuthApi
) : ApiRepository {

    private val tokenObtainmentLock = ReentrantLock()

    private var accessToken: JwtToken?
        get() = sharedPreferences.getString(JWT_KEY, null)?.let { JwtToken(it) }
        set(value) = sharedPreferences.edit {
            putString(JWT_KEY, value?.raw)
        }

    private var refreshToken: String?
        get() = sharedPreferences.getString(JWT_REFRESH_KEY, null)
        set(value) = sharedPreferences.edit {
            putString(JWT_KEY, value)
        }


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

    override fun getActualToken(): Result<String> {
        tokenObtainmentLock.withLock {
            return runBlocking {
                val token = accessToken
                if (token != null && token.isNotExpired()) {
                    return@runBlocking Result.success(token.raw)
                }
                val refreshToken = refreshToken
                    ?: return@runBlocking Result.failure(IllegalStateException("Refresh token is null"))
                return@runBlocking authApi.refreshBearer(TokenDataExportDto(refreshToken))
                    .map { (accessToken, refreshToken) ->
                        this@ApiRepositoryImpl.accessToken = JwtToken(accessToken)
                        this@ApiRepositoryImpl.refreshToken = refreshToken
                        accessToken
                    }
            }
        }
    }

    override suspend fun loadListOfDogs(): List<Dog> {
        delay(1000)
        return listOf(
            Dog(
                name = "Ralph",
                surname = "Kurok",
                price = 500.5f,
                imageUrl = "https://media.baamboozle.com/uploads/images/351028/1642483532_406091_url.jpeg"
            ),
            Dog(
                name = "Anton",
                surname = "Speed",
                price = 482.9f,
                imageUrl = "https://waggingmongrel.com/wp-content/uploads/2018/10/shutterstock_265071971" +
                        ".jpg"
            ),
        )
    }

    override suspend fun loadListOfCats(): List<Cat> {
        delay(2000)
        return listOf(
            Cat(
                name = "Барсик",
                surname = "Решала",
                price = 5000.5f,
                imageUrl = "https://krasivosti.pro/uploads/posts/2022-01/thumbs/1641419035_4-" +
                        "krasivosti-pro-p-kota-barsika-krasivo-foto-4.jpg"
            ),
            Cat(
                name = "Рыжик",
                surname = "Прыгун",
                price = 4082.9f,
                imageUrl = "https://img-fotki.yandex.ru/get/362774/36984010.62/0_b6339_813c0dd4_XL.jpg"
            ),
        )
    }

    companion object {
        private const val JWT_KEY = "jwt"
        private const val JWT_REFRESH_KEY = "jwt_refresh"
    }
}

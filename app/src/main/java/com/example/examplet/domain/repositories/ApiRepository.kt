package com.example.examplet.domain.repositories

import com.example.examplet.domain.models.UserCredentials

interface ApiRepository {
    suspend fun login(userCredentials: UserCredentials): Result<Unit>
}
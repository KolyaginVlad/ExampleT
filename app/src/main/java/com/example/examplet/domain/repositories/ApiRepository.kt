package com.example.examplet.domain.repositories

import com.example.examplet.domain.models.Cat
import com.example.examplet.domain.models.Dog
import com.example.examplet.domain.models.UserCredentials

interface ApiRepository {
    suspend fun login(userCredentials: UserCredentials): Result<Unit>

    suspend fun loadListOfDogs(): List<Dog>

    suspend fun loadListOfCats(): List<Cat>

    fun getActualToken(): Result<String>
}
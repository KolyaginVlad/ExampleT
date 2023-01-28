package com.example.examplet.data.models

import com.example.examplet.domain.models.UserCredentials

data class UserCredentialsDtoe(
    val login: String,
    val password: String
)

fun UserCredentials.mapToDtoe() =
    UserCredentialsDtoe(login, password)
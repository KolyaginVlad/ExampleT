package com.example.examplet.ui.auth

import com.example.examplet.utils.base.State

data class AuthScreenState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false
) : State()
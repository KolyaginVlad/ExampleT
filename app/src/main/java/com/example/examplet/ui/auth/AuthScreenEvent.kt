package com.example.examplet.ui.auth

import com.example.examplet.utils.base.Event

sealed class AuthScreenEvent : Event() {
    class ShowToast(val text: String) : AuthScreenEvent()
    object GoToList: AuthScreenEvent()
}
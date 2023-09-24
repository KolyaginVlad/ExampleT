package com.example.examplet.ui.sealed

import com.example.examplet.utils.base.Event

sealed class SealedScreenEvent : Event() {
    class ShowToast(val text: String) : SealedScreenEvent()
    object GoToList: SealedScreenEvent()
}
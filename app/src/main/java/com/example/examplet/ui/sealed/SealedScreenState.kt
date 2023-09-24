package com.example.examplet.ui.sealed

import com.example.examplet.utils.base.State

sealed class SealedScreenState : State() {
    object Init: SealedScreenState()
    data class StateFirst(val first: String): SealedScreenState()
    data class StateSecond(val second: Int): SealedScreenState()
}
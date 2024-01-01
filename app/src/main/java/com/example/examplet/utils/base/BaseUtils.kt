package com.example.examplet.utils.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Подписка на события от ViewModel. Получение событий привязано к lifecycle.
 * Взято из Orbit
 */
@Composable
fun <S: State, E: Event> BaseViewModel<S, E>.subscribeEvents(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffect: suspend (sideEffect: E) -> Unit
) {
    val sideEffectFlow = this.event
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            sideEffectFlow.collect { sideEffect(it) }
        }
    }
}

/**
 * Подписка на состояние от ViewModel. Получение состояния привязано к lifecycle.
 */
@Composable
fun <S: State, E: Event> BaseViewModel<S, E>.subscribeScreenState(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
) = screenState.collectAsStateWithLifecycle(
        lifecycleOwner = lifecycleOwner,
        minActiveState = minActiveState,
        context = context
    )
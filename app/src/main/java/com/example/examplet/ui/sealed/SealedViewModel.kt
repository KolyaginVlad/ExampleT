package com.example.examplet.ui.sealed

import com.example.examplet.utils.base.BaseViewModel
import com.example.examplet.utils.log.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SealedViewModel @Inject constructor(
    logger: Logger,
) : BaseViewModel<SealedScreenState, SealedScreenEvent>(SealedScreenState.Init, logger) {

    fun someThing(text: String) {
        val firstText = runWithState(onErrorState = ::wrong, block = ::getFirst)
        runWithState<SealedScreenState.StateFirst> {
            logger.debug(it.toString())
        }
        updateState<SealedScreenState.StateFirst> {
            it.copy(first =  text)
        }
    }

    private fun getFirst(state: SealedScreenState.StateFirst): String {
        return state.first
    }

    private fun wrong(state: SealedScreenState): String? {
        return when(state) {
            SealedScreenState.Init -> null
            is SealedScreenState.StateFirst -> state.first //never
            is SealedScreenState.StateSecond -> state.second.toString()
        }
    }
}

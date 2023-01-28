package com.example.examplet.ui.auth

import com.example.examplet.domain.models.UserCredentials
import com.example.examplet.domain.repositories.ApiRepository
import com.example.examplet.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
) : BaseViewModel<AuthScreenState, AuthScreenEvent>(AuthScreenState()) {

    fun onLoginChange(login: String) {
        updateState {
            it.copy(login = login)
        }
    }

    fun onPasswordChange(password: String) {
        updateState {
            it.copy(password = password)
        }
    }

    fun onAuth() = launchViewModelScope {
        updateState {
            it.copy(isLoading = true)
        }
        apiRepository.login(UserCredentials(currentState.login, currentState.password)).fold(
            onFailure = {
                sendEvent(AuthScreenEvent.ShowToast(it.message ?: "Something went wrong"))
            }, onSuccess = {
                sendEvent(AuthScreenEvent.ShowToast("All is Ok"))
            }
        )
        updateState {
            AuthScreenState() //Сбрасываю на начальное состояние
        }
    }
}
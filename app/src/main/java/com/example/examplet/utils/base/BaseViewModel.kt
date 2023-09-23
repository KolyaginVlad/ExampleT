package com.example.examplet.utils.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examplet.utils.log.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Базовая ViewModel
 *
 * Принимает типы состояния экрана и sealed class для событий экрана.
 * @property initialState Начальное состояние экрана
 * @see State
 * @see Event
 */
abstract class BaseViewModel<STATE : State, EVENT : Event>(
    initialState: STATE,
    val logger: Logger
) : ViewModel() {

    private val _screenState = MutableStateFlow(initialState)

    /** Состояние экрана */
    val screenState = _screenState.asStateFlow()

    private val _event = MutableSharedFlow<EVENT>(extraBufferCapacity = 1)

    /** События для экрана */
    val event = _event.asSharedFlow()

    /** Текущее состояние экрана */
    protected val currentState: STATE
        get() = _screenState.value

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    /**
     * Обновить состояние экрана
     * @param block Функция для создания нового состояния при помощи предыдущего
     */
    fun updateState(block: (STATE) -> STATE) =
        _screenState.update(block)

    /**
     * Отправить событие на экран
     * @param event Событие для экрана
     */
    suspend fun sendEvent(event: EVENT) =
        _event.emit(event)

    /**
     * Отправить событие на экран и получить результат отправки
     * @param event Событие для экрана
     * @return Отправлено ли значение?
     */
    fun trySendEvent(event: EVENT) =
        _event.tryEmit(event)

    /**
     * Запуск корутины в скопе viewModel с обработкой ошибок и настроенным контекстом выполнения
     * @param block Код корутины
     * @see handleException
     */
    protected fun launchViewModelScope(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            context = SupervisorJob() + Dispatchers.IO + exceptionHandler,
            block = block
        )

    /**
     * Запуск корутины с получением реззультата в скопе viewModel с обработкой ошибок и настроенным
     * контекстом выполнения
     * @param block Код корутины
     * @see handleException
     */
    protected fun <T> asyncViewModelScope(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ) =
        viewModelScope.async(
            context = SupervisorJob() + Dispatchers.IO + exceptionHandler,
            start = start,
            block = block
        )

    /**
     * Метод для обработки исключений, которые произошли при исполнении корутин
     *
     * Дефолтная реализация печатает исключение в лог и отправляет в Crashlytics
     * @param throwable [Throwable]
     */
    protected open fun handleException(throwable: Throwable) {
        logger.error(throwable)
    }

    /**
     * Подписка на Flow в рамках жизни viewModel с обработкой ошибок и настроенным
     * контекстом выполнения
     * @param onStart Вызов при старте
     * @param onComplete Вызов при завершении
     * @param onEach Вызов для каждого элемента
     * @see handleException
     */
    protected fun <T> Flow<T>.subscribe(
        onStart: suspend FlowCollector<T>.() -> Unit = {},
        onComplete: suspend FlowCollector<T>.(Throwable?) -> Unit = {},
        onEach: suspend (T) -> Unit
    ) =
        this.onStart(onStart)
            .onEach(onEach)
            .onCompletion(onComplete)
            .flowOn(Dispatchers.Main + exceptionHandler)
            .launchIn(viewModelScope)

    protected fun <T> Result<T>.handleExceptionOnFailure() =
        onFailure(this@BaseViewModel::handleException)
}
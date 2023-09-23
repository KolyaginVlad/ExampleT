package com.example.examplet.ui.list

import com.example.examplet.domain.repositories.ApiRepository
import com.example.examplet.utils.base.BaseViewModel
import com.example.examplet.utils.log.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    logger: Logger,
    private val apiRepository: ApiRepository
) : BaseViewModel<ListScreenState, ListScreenEvent>(ListScreenState(), logger) {

    init {
        launchViewModelScope {
            launch {
                val list = apiRepository.loadListOfDogs().map {
                    it.mapToItemState()
                }
                addItemsToList(list)
            }
            launch {
                val list = apiRepository.loadListOfCats().map {
                    it.mapToItemState()
                }
                addItemsToList(list)
            }
        }
    }

    fun onClickItem(index: Int) {
        trySendEvent(ListScreenEvent.ShowToast(currentState.listOfItems[index].title))
    }

    fun onBack() {
        trySendEvent(ListScreenEvent.GoBack)
    }

    private fun addItemsToList(list: List<ItemState>) {
        updateState {
            it.copy(
                listOfItems = (it.listOfItems + list).toImmutableList(),
                isLoading = false
            )
        }
    }
}
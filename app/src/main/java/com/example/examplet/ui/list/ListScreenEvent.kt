package com.example.examplet.ui.list

import com.example.examplet.utils.base.Event

sealed class ListScreenEvent: Event() {
    class ShowToast(val text: String): ListScreenEvent()
    object GoBack: ListScreenEvent()
}

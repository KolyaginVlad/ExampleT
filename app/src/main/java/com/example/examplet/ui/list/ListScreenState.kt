package com.example.examplet.ui.list

import com.example.examplet.domain.models.Cat
import com.example.examplet.domain.models.Dog
import com.example.examplet.utils.base.State
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ListScreenState(
    val listOfItems: ImmutableList<ItemState> = persistentListOf(),
    val isLoading: Boolean = true
): State()

data class ItemState(
    val title: String,
    val subTitle: String,
    val price: Float,
    val imageUrl: String
)

fun Dog.mapToItemState() =
    ItemState(
        title = name,
        subTitle = surname,
        price = price,
        imageUrl = imageUrl
    )

fun Cat.mapToItemState() =
    ItemState(
        title = name,
        subTitle = surname,
        price = price,
        imageUrl = imageUrl
    )
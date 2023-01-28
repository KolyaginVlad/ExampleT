package com.example.examplet.data.di

import com.example.examplet.data.repositories.ApiRepositoryImpl
import com.example.examplet.domain.repositories.ApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface DataModule {
    @Binds
    fun bindApiRepository(
        apiRepositoryImpl: ApiRepositoryImpl
    ): ApiRepository
}
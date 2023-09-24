package com.example.examplet.utils.di

import com.example.examplet.utils.base.BaseDispatchers
import com.example.examplet.utils.log.Logger
import com.example.examplet.utils.log.LoggerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface UtilsModule {

    @Binds
    fun bindLogger(loggerImpl: LoggerImpl): Logger

    companion object {

        @Provides
        fun provideBaseDispatchers() =
            BaseDispatchers()
    }
}
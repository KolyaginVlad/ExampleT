package com.example.examplet.utils.di

import com.example.examplet.utils.log.Logger
import com.example.examplet.utils.log.LoggerImpl
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
}

@InstallIn(SingletonComponent::class)
@Module
class UtilsModuleProvide {

    @Provides
    fun providesCrashlytics() =
        FirebaseCrashlytics.getInstance()
}
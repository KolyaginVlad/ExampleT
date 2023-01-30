package com.example.examplet.data.di

import android.content.Context
import com.example.examplet.data.repositories.ApiRepositoryImpl
import com.example.examplet.domain.repositories.ApiRepository
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(ViewModelComponent::class)
@Module
interface DataModule {
    @Binds
    fun bindApiRepository(
        apiRepositoryImpl: ApiRepositoryImpl,
    ): ApiRepository
}

@Module
@InstallIn(SingletonComponent::class)
class AnalyticsModule {

    @Provides
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
}

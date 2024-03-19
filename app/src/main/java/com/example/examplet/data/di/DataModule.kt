package com.example.examplet.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.examplet.BuildConfig
import com.example.examplet.data.AuthApi
import com.example.examplet.data.api.AuthInterceptor
import com.example.examplet.data.api.ResultCallAdapterFactory
import com.example.examplet.data.repositories.ApiRepositoryImpl
import com.example.examplet.domain.repositories.ApiRepository
import com.example.examplet.utils.Constants
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface DataModule {
    @Binds
    fun bindApiRepository(
        apiRepositoryImpl: ApiRepositoryImpl,
    ): ApiRepository

    companion object {
        private const val TIMEOUT_VALUE = 20L

        @Provides
        fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
            return FirebaseAnalytics.getInstance(context)
        }

        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit) =
            retrofit.create(AuthApi::class.java)

        @Provides
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)
        }

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient, factory: Converter.Factory): Retrofit {
            return Retrofit.Builder()
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(ResultCallAdapterFactory())
                .baseUrl(Constants.BASE_URL)
                .build()
        }

        @Provides
        fun provideConverter(): Converter.Factory =
            GsonConverterFactory.create()

        @Provides
        @Singleton
        fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(
                            HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            }
                        )
                    }
                }
                .addInterceptor(authInterceptor)
                .callTimeout(TIMEOUT_VALUE, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_VALUE, TimeUnit.SECONDS)
                .build()
        }
    }
}

package com.shamardn.podcasttime.di

import android.content.Context
import com.shamardn.podcasttime.data.local.datastore.AppConfiguration
import com.shamardn.podcasttime.data.local.datastore.AppConfigurator
import com.shamardn.podcasttime.data.local.datastore.DataStorePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStorePreferences(@ApplicationContext context: Context) =
        DataStorePreferences(context)


    @Singleton
    @Provides
    fun provideAppConfiguration(dataStorePreferences: DataStorePreferences): AppConfiguration =
        AppConfigurator(dataStorePreferences)

}
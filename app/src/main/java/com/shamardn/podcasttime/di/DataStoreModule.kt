package com.shamardn.podcasttime.di

import android.content.Context
import com.shamardn.podcasttime.data.datasource.local.datastore.AppConfiguration
import com.shamardn.podcasttime.data.datasource.local.datastore.AppConfigurator
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
    fun provideAppConfiguration(@ApplicationContext context: Context): AppConfiguration =
        AppConfigurator(context)

}
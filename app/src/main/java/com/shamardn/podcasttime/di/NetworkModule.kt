package com.shamardn.podcasttime.di

import com.shamardn.podcasttime.data.datasource.remote.ApiService
import com.shamardn.podcasttime.data.datasource.remote.IpApiService
import com.shamardn.podcasttime.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("itunesRetrofit")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("itunesRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("ipApiRetrofit")
    fun provideRetrofitForIpApi(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("https://ipapi.co/")
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideIpApiService(@Named("ipApiRetrofit") retrofit: Retrofit): IpApiService {
        return retrofit.create(IpApiService::class.java)
    }
}
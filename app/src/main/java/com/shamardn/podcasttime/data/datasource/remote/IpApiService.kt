package com.shamardn.podcasttime.data.datasource.remote

import com.shamardn.podcasttime.data.datasource.remote.dtos.IpApiResponse
import retrofit2.http.GET

interface IpApiService {
    @GET("/json/")
    suspend fun getUserLocation(): IpApiResponse
}


/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.location.network

import android.location.Location
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

internal interface HereService {

    @GET("/v1/revgeocode")
    suspend fun getPlaceLocation(
        @Query("at") locationString: String,
        @Query("apikey") apiKey: String
    ): HerePlaceResponse

    @GET("/v1/discover")
    suspend fun searchPlace(
        @Query("at") locationString: String,
        @Query("q") query: String,
        @Query("apikey") apiKey: String
    ): HerePlaceResponse
}
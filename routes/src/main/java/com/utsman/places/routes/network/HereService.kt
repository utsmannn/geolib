/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.network

import retrofit2.http.GET
import retrofit2.http.Query

internal interface HereService {

    @GET("/v8/routes")
    suspend fun getRoutes(
        @Query("transportMode") transportMode: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("return") returnResult: String,
        @Query("apikey") apiKey: String
    ): HereRouteResponse
}
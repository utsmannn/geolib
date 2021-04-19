/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.routes

import com.utsman.geolib.routes.data.Mapper
import com.utsman.geolib.routes.data.RouteData
import com.utsman.geolib.routes.data.RouteRequest
import com.utsman.geolib.routes.network.HereService
import com.utsman.geolib.core.*

internal class PlacesRouteImpl(
    private val hereMapsApi: String,
    override var enableLog: Boolean = false
) : PlacesRoute {

    private fun provideService(): HereService {
        return Network.retrofit(ConstantValues.BASE_URL, enableLog)
            .create(HereService::class.java)
    }

    override suspend fun searchRoute(request: RouteRequest.() -> Unit): Result<RouteData> {
        val routeRequest = RouteRequest().apply(request)

        val errorMessage = "Search route error!"
        return if (routeRequest.isNullSafe()) {
            val resultPolyline = fetch(errorMessage) {
                provideService().getRoutes(
                    transportMode = routeRequest.transportMode?.getString()!!,
                    origin = routeRequest.startLocation?.toStringService()!!,
                    destination = routeRequest.endLocation?.toStringService()!!,
                    returnResult = ConstantValues.ReturnResult.POLYLINE,
                    apiKey = hereMapsApi
                )
            }

            val resultLength = fetch(errorMessage) {
                provideService().getRoutes(
                    transportMode = routeRequest.transportMode?.getString()!!,
                    origin = routeRequest.startLocation?.toStringService()!!,
                    destination = routeRequest.endLocation?.toStringService()!!,
                    returnResult = ConstantValues.ReturnResult.LENGTH,
                    apiKey = hereMapsApi
                )
            }


            if (resultPolyline.isSuccess && resultLength.isSuccess) {
                val dataPolyline = Mapper.mapPolylineAlgorithm(resultPolyline.getOrNull()?.getPolyline())
                val dataLength = resultLength.getOrNull()
                val data = RouteData(
                    encodedPolyline = dataPolyline ?: "",
                    length = dataLength?.getLength() ?: 0f
                )
                Result.success(data)
            } else {
                val throwable = GeolibException(resultPolyline.exceptionOrNull()?.message ?: errorMessage)
                Result.failure(throwable)
            }
        } else {
            val throwable = GeolibException(errorMessage)
            Result.failure(throwable)
        }
    }

}
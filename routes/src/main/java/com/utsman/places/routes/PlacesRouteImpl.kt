/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes

import com.utsman.places.routes.data.RouteData
import com.utsman.places.routes.data.RouteRequest
import com.utsman.places.routes.network.HereService
import com.utsman.places.utils.*

internal class PlacesRouteImpl(
    private val hereMapsApi: String,
    override var enableLog: Boolean = false
) : PlacesRoute {

    private fun provideService(): HereService {
        return Network.retrofit(ConstantValues.BASE_URL, enableLog)
            .create(HereService::class.java)
    }

    override suspend fun searchRoute(request: RouteRequest.() -> Unit): ResultState<RouteData> {
        val routeRequest = RouteRequest().apply(request)

        val errorMessage = "Search route error!"
        return if (routeRequest.isNullSafe()) {
            val hereDataPolyline = fetch(errorMessage) {
                provideService().getRoutes(
                    transportMode = routeRequest.transportMode?.getString()!!,
                    origin = routeRequest.startLocation?.toStringService()!!,
                    destination = routeRequest.endLocation?.toStringService()!!,
                    returnResult = ConstantValues.ReturnResult.POLYLINE,
                    apiKey = hereMapsApi
                )
            }.mapData {
                it.getPolyline()
            }

            val dataPolyline = if (hereDataPolyline.mapToSuccess() != null) {
                hereDataPolyline.mapToSuccess()
            } else {
                null
            }

            logd("data --> ${dataPolyline}")

            val hereDataLength = fetch(errorMessage) {
                provideService().getRoutes(
                    transportMode = routeRequest.transportMode?.getString()!!,
                    origin = routeRequest.startLocation?.toStringService()!!,
                    destination = routeRequest.endLocation?.toStringService()!!,
                    returnResult = ConstantValues.ReturnResult.LENGTH,
                    apiKey = hereMapsApi
                )
            }.mapData {
                it.getLength()
            }

            val dataLength = if (hereDataLength.mapToSuccess() != null) {
                hereDataLength.mapToSuccess()
            } else {
                null
            }

            val data = RouteData(
                encodedPolyline = dataPolyline ?: "",
                length = dataLength ?: 0f
            )

            if (data.encodedPolyline != "") {
                ResultState.Success(data)
            } else {
                val throwable = hereDataPolyline.mapToException() ?: hereDataLength.mapToException() ?: GeolibException("")
                ResultState.Failure(throwable)
            }

        } else {
            val throwable =  GeolibException("Request must be filled, check documentation!")
            ResultState.Failure(throwable)
        }
    }

}
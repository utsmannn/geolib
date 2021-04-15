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

    override suspend fun searchRoute(request: RouteRequest.() -> Unit): RouteData? {
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

            /*val dataPolyline = if (hereDataPolyline.mapToSuccess() != null) {
                hereDataPolyline.mapToSuccess()
            } else {
                null
            }*/

            //logd("data --> ${dataPolyline}")

            val resultLength = fetch(errorMessage) {
                provideService().getRoutes(
                    transportMode = routeRequest.transportMode?.getString()!!,
                    origin = routeRequest.startLocation?.toStringService()!!,
                    destination = routeRequest.endLocation?.toStringService()!!,
                    returnResult = ConstantValues.ReturnResult.LENGTH,
                    apiKey = hereMapsApi
                )
            }

            /*val data = RouteData(
                encodedPolyline = dataPolyline ?: "",
                length = dataLength ?: 0f
            )*/

            if (resultPolyline is ResultState.Success && resultLength is ResultState.Success) {
                val dataPolyline = resultPolyline.data
                val dataLength = resultLength.data
                val data = RouteData(
                    encodedPolyline = dataPolyline.getPolyline() ?: "",
                    length = dataLength.getLength() ?: 0f
                )
                //ResultState.Success(data)

                data
            } else {
               null
            }

            /*if (data.encodedPolyline != "") {
                ResultState.Success(data)
            } else {
                val throwable = hereDataPolyline.mapToException() ?: hereDataLength.mapToException() ?: GeolibException("")

            }*/

        } else {
            null
        }
    }

}
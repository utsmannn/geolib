/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes

import com.utsman.places.routes.data.Mapper
import com.utsman.places.routes.data.RouteData
import com.utsman.places.routes.data.RouteRequest
import com.utsman.places.routes.network.HereService
import com.utsman.places.utils.HelperPlacesException
import com.utsman.places.utils.Network
import com.utsman.places.utils.fetch
import com.utsman.places.utils.toStringService

internal class PlacesRouteImpl(
    private val hereMapsApi: String,
    override var enableLog: Boolean = true
) : PlacesRoute {

    private fun provideService(): HereService {
        return Network.retrofit(ConstantValues.BASE_URL, enableLog)
            .create(HereService::class.java)
    }

    override suspend fun searchRoute(request: RouteRequest.() -> Unit): RouteData {
        val routeRequest = RouteRequest()
        request.invoke(routeRequest)

        return if (routeRequest.isNullSafe()) {
            val hereDataPolyline = fetch {
                provideService().getRoutes(
                    transportMode = routeRequest.transportMode?.getString()!!,
                    origin = routeRequest.startLocation?.toStringService()!!,
                    destination = routeRequest.endLocation?.toStringService()!!,
                    returnResult = ConstantValues.ReturnResult.POLYLINE,
                    apiKey = hereMapsApi
                )
            }?.getPolyline()

            val dataPolyline = Mapper.mapPolylineAlgorithm(hereDataPolyline)

            val dataLength = fetch {
                provideService().getRoutes(
                    transportMode = routeRequest.transportMode?.getString()!!,
                    origin = routeRequest.startLocation?.toStringService()!!,
                    destination = routeRequest.endLocation?.toStringService()!!,
                    returnResult = ConstantValues.ReturnResult.LENGTH,
                    apiKey = hereMapsApi
                )
            }?.getLength()

            RouteData(
                encodedPolyline = dataPolyline ?: "",
                length = dataLength ?: 0f
            )
        } else {
            throw HelperPlacesException()
        }
    }

}
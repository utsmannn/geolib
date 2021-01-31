/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.network

internal data class HereRouteResponse(
    val routes: List<Route>?,
    val errorDescription: String?
) {

    data class Section(
        val id: String,
        val polyline: String,
        val summary: Summary,
        val transport: Transport
    )

    data class Summary(
        val length: Long
    )

    data class Route(
        val sections: List<Section>
    )

    data class Transport(
        val mode: String
    )

    fun getPolyline(): String? {
        return routes?.firstOrNull()?.sections?.firstOrNull()?.polyline
    }

    fun getLength(): Float? {
        return routes?.firstOrNull()?.sections?.firstOrNull()?.summary?.length?.toFloat()
    }
}
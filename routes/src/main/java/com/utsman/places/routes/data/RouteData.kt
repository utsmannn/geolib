/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.data

data class RouteData(
    val encodedPolyline: String,
    val length: Float
) {
    val geometries = Mapper.getGoogleGeometries(encodedPolyline)
}
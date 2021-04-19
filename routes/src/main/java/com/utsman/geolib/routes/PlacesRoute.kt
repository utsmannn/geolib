/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.routes

import com.utsman.geolib.routes.data.RouteData
import com.utsman.geolib.routes.data.RouteRequest

interface PlacesRoute {
    suspend fun searchRoute(request: RouteRequest.() -> Unit): Result<RouteData>
    var enableLog: Boolean
}
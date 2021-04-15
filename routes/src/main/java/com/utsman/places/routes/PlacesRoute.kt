/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes

import com.utsman.places.routes.data.RouteData
import com.utsman.places.routes.data.RouteRequest
import com.utsman.places.utils.ResultState

interface PlacesRoute {
    suspend fun searchRoute(request: RouteRequest.() -> Unit): ResultState<RouteData>
    var enableLog: Boolean
}
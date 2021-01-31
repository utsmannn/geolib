/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes

import com.utsman.places.routes.data.RouteData
import com.utsman.places.routes.data.RouteRequest

interface PlacesRoute {
    suspend fun searchRoute(request: RouteRequest.() -> Unit): RouteData
    var enableLog: Boolean
}
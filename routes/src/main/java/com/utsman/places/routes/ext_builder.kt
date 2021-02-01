/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes

import android.util.Log
import java.util.*

internal fun logd(message: String) = Log.d("--PLACES--", message)

fun createPlacesRoute(hereMapsApiKey: String): PlacesRoute {
    return PlacesRouteImpl(hereMapsApiKey)
}
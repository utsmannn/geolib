/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.data

import android.location.Location

data class RouteRequest(
    var startLocation: Location? = null,
    var endLocation: Location? = null,
    var transportMode: TransportMode? = TransportMode.CAR,
) {
    fun isNullSafe() = startLocation != null
            && endLocation != null
            && transportMode != null
}

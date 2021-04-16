/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.location

import com.google.android.gms.location.FusedLocationProviderClient
import java.util.*

fun FusedLocationProviderClient.createPlacesLocation(hereMapsApiKey: String): PlacesLocation {
    return PlacesLocationImpl(this, hereMapsApiKey)
}

internal val Double.kM: String
    get() {
        val distanceInMeter = this / 1000.0

        val formatter = Formatter().format("%.1f", distanceInMeter)
        val doubleFormat = formatter.toString().replace(",", ".").toDouble()
        return "$doubleFormat Km"
    }
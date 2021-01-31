/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.location

import com.google.android.gms.location.FusedLocationProviderClient

fun FusedLocationProviderClient.createPlacesLocation(hereMapsApiKey: String): PlacesLocation {
    return PlacesLocationImpl(this, hereMapsApiKey)
}
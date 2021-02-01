/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.location.data

import android.location.Location

data class PlaceData(
    val hereId: String,
    val title: String,
    val address: String,
    val district: String,
    val city: String,
    val location: Location,
    val distance: Double,
    val distanceInKm: String?,
    val category: String? = null
)
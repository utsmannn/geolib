/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline

internal data class PolylineIdentifier(
    var polyline: List<Polyline>?,
    var firstLatLng: LatLng,
    var lastLatLng: LatLng,
    var zIndex: Float
)
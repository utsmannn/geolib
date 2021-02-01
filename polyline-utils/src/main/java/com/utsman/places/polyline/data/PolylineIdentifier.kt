/*
 * Created on 1/2/21 9:37 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline

internal data class PolylineIdentifier(
    var polyline: List<Polyline>?,
    var firstLatLng: LatLng,
    var lastLatLng: LatLng,
    var zIndex: Float
)
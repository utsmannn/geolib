/*
 * Created on 1/2/21 9:48 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline

import com.google.android.gms.maps.model.LatLng
import com.utsman.places.polyline.data.PolylineConfig

interface PlacesPolyline {
    fun startAnimate(
        geometries: List<LatLng>,
        actionConfig: (PolylineConfig.() -> Unit)? = null
    ): PlacesPointPolyline
}
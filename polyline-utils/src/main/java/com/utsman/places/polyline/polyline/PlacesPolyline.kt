/*
 * Created on 1/2/21 10:08 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline.polyline

import com.google.android.gms.maps.model.LatLng
import com.utsman.places.polyline.point.PlacesPointPolyline
import com.utsman.places.polyline.data.PolylineConfig

interface PlacesPolyline {
    fun startAnimate(
        geometries: List<LatLng>,
        actionConfig: (PolylineConfig.() -> Unit)? = null
    ): PlacesPointPolyline
}
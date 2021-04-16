/*
 * Created on 1/2/21 10:09 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.polyline.point

import com.google.android.gms.maps.model.LatLng
import com.utsman.geolib.polyline.data.PolylineConfig

interface PointPolyline {
    fun addPoints(
        newGeometries: List<LatLng>,
        polylineConfig: PolylineConfig
    ): PointPolyline

    fun remove(withGeometries: List<LatLng>? = null): Boolean

}
/*
 * Created on 1/2/21 10:08 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.polyline.polyline

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.utsman.geolib.polyline.data.PolylineConfigValue
import com.utsman.geolib.polyline.point.PointPolyline
import com.utsman.geolib.polyline.data.PolylineConfig

interface PolylineAnimator {
    fun startAnimate(
        geometries: List<LatLng>,
        actionConfig: (PolylineConfig.() -> Unit)? = null
    ): PointPolyline

    fun getBindGoogleMaps(): GoogleMap

    fun getCurrentConfig(): PolylineConfigValue
}
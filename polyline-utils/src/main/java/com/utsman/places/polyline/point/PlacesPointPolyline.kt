/*
 * Created on 1/2/21 10:09 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline.point

import com.google.android.gms.maps.model.LatLng
import com.utsman.places.polyline.data.PolylineConfig

interface PlacesPointPolyline {

    fun addPoints(
        newGeometries: List<LatLng>,
        actionConfig: (PolylineConfig.() -> Unit)? = null
    ): PlacesPointPolyline

    fun remove(withGeometries: List<LatLng>? = null): Boolean

}
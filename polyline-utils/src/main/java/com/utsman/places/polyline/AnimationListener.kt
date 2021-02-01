/*
 * Created on 1/2/21 9:56 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline

import com.google.android.gms.maps.model.LatLng

internal interface AnimationListener {
    fun onStartAnimation(latLng: LatLng)
    fun onEndAnimation(latLng: LatLng)
    fun onUpdate(latLng: LatLng, duration: Int)
}
/*
 * Created on 1/2/21 9:49 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.utsman.places.polyline.data.StackAnimationMode

data class PolylineConfig(
    var stackAnimationMode: StackAnimationMode? = null,
    var duration: Long = 2000,
    var cameraAutoUpdate: Boolean = false,
    internal var doOnStartAnim: ((LatLng) -> Unit)? = null,
    internal var doOnEndAnim: ((LatLng) -> Unit)? = null,
    internal var doOnUpdateAnim: ((LatLng, Int) -> Unit)? = null,
    internal var polylineOptions1: PolylineOptions? = null,
    internal var polylineOptions2: PolylineOptions? = null
)
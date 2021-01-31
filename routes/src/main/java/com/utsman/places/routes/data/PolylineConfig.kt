/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

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
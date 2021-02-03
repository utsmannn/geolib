/*
 * Created on 1/2/21 9:49 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline.data

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.utsman.places.polyline.data.StackAnimationMode
import com.utsman.places.polyline.utils.copyPolylineOptions
import com.utsman.places.polyline.utils.transparentColor
import com.utsman.places.polyline.utils.withAccentPolyline

data class PolylineConfig(
    var stackAnimationMode: StackAnimationMode? = StackAnimationMode.BlockStackAnimation,
    var duration: Long = 2000,
    var cameraAutoUpdate: Boolean = false,
    var polylineDrawMode: PolylineDrawMode = PolylineDrawMode.Normal,
    internal var doOnStartAnim: ((LatLng) -> Unit)? = null,
    internal var doOnEndAnim: ((LatLng) -> Unit)? = null,
    internal var doOnUpdateAnim: ((LatLng, Int) -> Unit)? = null,
    internal var polylineOptions1: PolylineOptions? = null,
    internal var polylineOptions2: PolylineOptions? = null,
    internal var polylineOptionsBorder: PolylineOptions? = null
) {

    internal fun enableBorder(isEnable: Boolean, borderColor: Int, width: Int) {
        val primaryWidth = polylineOptions1?.width ?: 10f
        polylineOptionsBorder = if (isEnable) {
            if (polylineOptions1 != null) {
                polylineOptions1!!.copyPolylineOptions()
            } else {
                PolylineOptions()
            }.apply {
                zIndex(1f)
                width(primaryWidth+width)
                color(borderColor)
            }
        } else {
            null
        }
    }
}
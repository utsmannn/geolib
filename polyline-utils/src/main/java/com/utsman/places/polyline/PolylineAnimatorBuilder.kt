/*
 * Created on 1/2/21 9:50 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ktx.model.polylineOptions
import com.utsman.places.polyline.data.StackAnimationMode
import com.utsman.places.polyline.polyline.PolylineAnimator
import com.utsman.places.polyline.polyline.PolylineAnimatorOptions

class PolylineAnimatorBuilder(
    private val googleMap: GoogleMap,
    primaryColor: Int,
    accentColor: Int
) {
    private val polylineAnimatorOptions = PolylineAnimatorOptions(googleMap, primaryColor, accentColor)

    fun createPolylineAnimator(): PolylineAnimator {
        return polylineAnimatorOptions
    }

    fun withPrimaryPolyline(optionsActions: PolylineOptions.() -> Unit): PolylineAnimatorBuilder {
        val options = polylineOptions(optionsActions)
        val polyline = googleMap.addPolyline(options).apply {
            points = emptyList()
        }
        polylineAnimatorOptions.apply {
            this.polylineOption1 = options
            this.primaryColor = polyline.color
        }
        return this
    }

    fun withAccentPolyline(optionsActions: PolylineOptions.() -> Unit): PolylineAnimatorBuilder {
        val options = polylineOptions(optionsActions)
        val polyline = googleMap.addPolyline(options).apply {
            points = emptyList()
        }
        polylineAnimatorOptions.apply {
            this.polylineOption2 = options
            this.accentColor = polyline.color
        }
        return this
    }

    fun withCameraAutoFocus(cameraAuto: Boolean): PolylineAnimatorBuilder {
        polylineAnimatorOptions.cameraAutoFocus = cameraAuto
        return this
    }

    fun withStackAnimationMode(stackAnimationMode: StackAnimationMode): PolylineAnimatorBuilder {
        polylineAnimatorOptions.stackAnimationMode = stackAnimationMode
        return this
    }

}
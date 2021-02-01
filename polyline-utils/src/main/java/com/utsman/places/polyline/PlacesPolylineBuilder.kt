/*
 * Created on 1/2/21 9:50 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ktx.model.polylineOptions
import com.utsman.places.polyline.data.StackAnimationMode

class PlacesPolylineBuilder(
    private val googleMap: GoogleMap,
    primaryColor: Int,
    accentColor: Int
) {
    private val placesPolylineOptions = PlacesPolylineOptions(googleMap, primaryColor, accentColor)

    fun createAnimatePolyline(): PlacesPolyline {
        return placesPolylineOptions
    }

    fun withPrimaryPolyline(optionsActions: PolylineOptions.() -> Unit): PlacesPolylineBuilder {
        val options = polylineOptions(optionsActions)
        val polyline = googleMap.addPolyline(options).apply {
            points = emptyList()
        }
        placesPolylineOptions.apply {
            this.polylineOption1 = options
            this.primaryColor = polyline.color
        }
        return this
    }

    fun withAccentPolyline(optionsActions: PolylineOptions.() -> Unit): PlacesPolylineBuilder {
        val options = polylineOptions(optionsActions)
        val polyline = googleMap.addPolyline(options).apply {
            points = emptyList()
        }
        placesPolylineOptions.apply {
            this.polylineOption2 = options
            this.accentColor = polyline.color
        }
        return this
    }

    fun withCameraAutoFocus(cameraAuto: Boolean): PlacesPolylineBuilder {
        placesPolylineOptions.cameraAutoFocus = cameraAuto
        return this
    }

    fun withStackAnimationMode(stackAnimationMode: StackAnimationMode): PlacesPolylineBuilder {
        placesPolylineOptions.stackAnimationMode = stackAnimationMode
        return this
    }

}
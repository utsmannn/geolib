/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.polyline

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ktx.model.polylineOptions
import com.utsman.places.routes.PlacesPolyline
import com.utsman.places.routes.data.StackAnimationMode

class PlacesPolylineBuilder(
    private val googleMap: GoogleMap,
    primaryColor: Int,
    accentColor: Int
) {
    private val placesPolyline = PlacesPolyline(googleMap, primaryColor, accentColor)

    fun createAnimatePolyline(): PlacesPolyline {
        return placesPolyline
    }

    fun withPrimaryPolyline(optionsActions: PolylineOptions.() -> Unit): PlacesPolylineBuilder {
        val options = polylineOptions(optionsActions)
        val polyline = googleMap.addPolyline(options).apply {
            points = emptyList()
        }
        placesPolyline.apply {
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
        placesPolyline.apply {
            this.polylineOption2 = options
            this.accentColor = polyline.color
        }
        return this
    }

    fun withCameraAutoFocus(cameraAuto: Boolean): PlacesPolylineBuilder {
        placesPolyline.cameraAutoFocus = cameraAuto
        return this
    }

    fun withStackAnimationMode(stackAnimationMode: StackAnimationMode): PlacesPolylineBuilder {
        placesPolyline.stackAnimationMode = stackAnimationMode
        return this
    }

}
/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes

import android.graphics.Color
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ktx.model.polylineOptions
import com.utsman.places.routes.data.PolylineConfig
import com.utsman.places.routes.polyline.PlacesPolylineBuilder

internal fun logd(message: String) = Log.d("--PLACES--", message)

fun createPlacesRoute(hereMapsApiKey: String): PlacesRoute {
    return PlacesRouteImpl(hereMapsApiKey)
}

fun GoogleMap.createPlacesPolylineBuilder(primaryColor: Int = Color.BLACK, accentColor: Int = Color.GRAY) =
    PlacesPolylineBuilder(this, primaryColor, accentColor)

fun PolylineOptions.toPolylineOptions(points: List<LatLng>): PolylineOptions {
    val polylineOptions = this.copyPolylineOptions()
    polylineOptions.addAll(points)
    return polylineOptions
}

fun PolylineOptions.copyPolylineOptions(): PolylineOptions {
    return PolylineOptions()
        .color(this.color)
        .width(this.width)
        .startCap(this.startCap)
        .endCap(this.endCap)
        .clickable(this.isClickable)
        .jointType(this.jointType)
        .visible(this.isVisible)
        .pattern(this.pattern)
}

fun PolylineConfig.withPrimaryPolyline(optionsActions: PolylineOptions.() -> Unit) {
    val options = polylineOptions(optionsActions)
    polylineOptions1 = options
}

fun PolylineConfig.withAccentPolyline(optionsActions: PolylineOptions.() -> Unit) {
    val options = polylineOptions(optionsActions)
    polylineOptions2 = options
}

fun PolylineConfig.doOnStartAnimation(action: (LatLng) -> Unit) {
    this.doOnStartAnim = action
}

fun PolylineConfig.doOnEndAnimation(action: (LatLng) -> Unit) {
    this.doOnEndAnim = action
}

fun PolylineConfig.doOnUpdateAnimation(action: (latLng: LatLng, mapCameraDuration: Int) -> Unit) {
    this.doOnUpdateAnim = action
}
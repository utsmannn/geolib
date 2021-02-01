/*
 * Created on 1/2/21 10:16 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline.utils

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ktx.model.polylineOptions
import com.utsman.places.polyline.PlacesPolylineBuilder
import com.utsman.places.polyline.data.PolylineConfig

fun GoogleMap.createPlacesPolylineBuilder(primaryColor: Int = Color.BLACK, accentColor: Int = Color.GRAY) =
    PlacesPolylineBuilder(this, primaryColor, accentColor)

fun PolylineOptions.toPolylineOptions(points: List<LatLng>): PolylineOptions {
    val polylineOptions = this.copyPolylineOptions()
    polylineOptions.addAll(points)
    return polylineOptions
}

internal fun PolylineOptions.copyPolylineOptions(): PolylineOptions {
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
/*
 * Created on 4/10/21 1:09 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.marker

import android.animation.ValueAnimator
import android.graphics.PointF
import android.view.View
import androidx.core.graphics.toPointF
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.utsman.geolib.marker.adapter.MarkerViewAdapter
import com.utsman.geolib.marker.config.AnchorPoint
import com.utsman.geolib.marker.data.MarkerView

fun Marker.moveMarker(newLatLng: LatLng, rotate: Boolean = false) {
    val animator: ValueAnimator = moveMarkerSmoothly(this, newLatLng)
    animator.start()
    val f = getAngle(
        LatLng(this.position.latitude, this.position.longitude),
        LatLng(newLatLng.latitude, newLatLng.longitude)
    ).toFloat()
    rotateMarker(this, f, rotate)
}

fun MarkerView.moveMarker(newLatLng: LatLng, googleMap: GoogleMap, rotate: Boolean = false) {
    val animator: ValueAnimator = moveMarkerSmoothly(this, googleMap, newLatLng)
    animator.start()

    val f = getAngle(
        LatLng(this.position.latitude, this.position.longitude),
        LatLng(newLatLng.latitude, newLatLng.longitude)
    ).toFloat()
    rotateMarker(this, f, rotate)
}

fun GoogleMap.clearAllLayers(vararg markerViewAdapters: MarkerViewAdapter) {
    clear()
    if (markerViewAdapters.isNotEmpty()) {
        markerViewAdapters.forEach { adapter ->
            adapter.removeAllMarkerView()
        }
    }
}

internal fun GoogleMap.getCurrentPointF(latLng: LatLng): PointF {
    return projection.toScreenLocation(latLng).toPointF()
}

internal fun View.moveJust(point: PointF, anchorPoint: AnchorPoint) {
    when (anchorPoint) {
        AnchorPoint.NORMAL -> {
            translationX = point.x-(measuredWidth/2)
            translationY = point.y-measuredHeight
        }
        AnchorPoint.CENTER -> {
            translationX = point.x-(measuredWidth/2)
            translationY = point.y-(measuredHeight/2)
        }
    }
}

internal fun View.move(point: PointF) {
    animate()
        .setDuration(1500)
        .translationX(point.x-(measuredWidth/2))
        .translationY(point.y-measuredHeight)
        .start()
}
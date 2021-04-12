/*
 * Created on 4/10/21 1:09 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker

import android.animation.ValueAnimator
import android.graphics.PointF
import android.view.View
import androidx.core.graphics.toPointF
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

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

internal fun GoogleMap.getCurrentPointF(latLng: LatLng): PointF {
    return projection.toScreenLocation(latLng).toPointF()
}

internal fun View.moveJust(point: PointF) {
    translationX = point.x-(measuredWidth/2)
    translationY = point.y-measuredHeight
}

internal fun View.move(point: PointF) {
    animate()
        .setDuration(1500)
        .translationX(point.x-(measuredWidth/2))
        .translationY(point.y-measuredHeight)
        .start()
}
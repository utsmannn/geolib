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

fun Marker.moveMarker(newLatLng: LatLng, rotate: Boolean = true) {
    val animator: ValueAnimator = moveMarkerSmoothly(this, newLatLng)
    animator.start()
    val f = getAngle(
        LatLng(this.position.latitude, this.position.longitude),
        LatLng(newLatLng.latitude, newLatLng.longitude)
    ).toFloat()
    rotateMarker(this, f, rotate)
}

fun MarkerView.moveMarker(prevLatLng: LatLng, newLatLng: LatLng, googleMap: GoogleMap, rotate: Boolean = true) {
    onMoved = true
    position = newLatLng
    val animator: ValueAnimator = moveMarkerSmoothly(this, googleMap, newLatLng)
    animator.start()
    //view.move(googleMap.getCurrentPointF(newLatLng))
    if (prevLatLng.toLocation().distanceTo(newLatLng.toLocation()) < 10f) {
        onMoved = false
    }
    /*val f = getAngle(
        LatLng(this.position.latitude, this.position.longitude),
        LatLng(newLatLng.latitude, newLatLng.longitude)
    ).toFloat()*/
    //rotateMarker(this, f, rotate)
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
        .translationX(point.x-(measuredWidth/2))
        .translationY(point.y-measuredHeight)
        .start()
}
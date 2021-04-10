/*
 * Created on 4/10/21 1:09 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker

import android.animation.ValueAnimator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

fun Marker.moveMarker(newLatLng: LatLng) {
    val animator: ValueAnimator = moveMarkerSmoothly(this, newLatLng)
    animator.start()
    val f = getAngle(
        LatLng(this.position.latitude, this.position.longitude),
        LatLng(newLatLng.latitude, newLatLng.longitude)
    ).toFloat()
    rotateMarker(this, f, true)
}
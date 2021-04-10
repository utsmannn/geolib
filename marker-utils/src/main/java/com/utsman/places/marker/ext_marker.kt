/*
 * Created on 4/10/21 1:09 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Point
import android.graphics.PointF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.graphics.toPointF
import androidx.core.graphics.unaryMinus
import androidx.core.view.contains
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

fun Marker.moveMarker(newLatLng: LatLng, rotate: Boolean = true) {
    val animator: ValueAnimator = moveMarkerSmoothly(this, newLatLng)
    animator.start()
    val f = getAngle(
        LatLng(this.position.latitude, this.position.longitude),
        LatLng(newLatLng.latitude, newLatLng.longitude)
    ).toFloat()
    rotateMarker(this, f, rotate)
}

fun Marker.moveMarkerLottie(newLatLng: LatLng, parent: ViewGroup, lottieView: LottieAnimationView, rotate: Boolean = true, updated: () -> Unit) {
    val lottieContainer = LayoutInflater.from(parent.context).inflate(R.layout.lottie_container, parent, false)
    val container = lottieContainer.findViewById<RelativeLayout>(R.id.lottie_container)

    /*if (parent.contains(lottieView)) {
        parent.removeView(lottieView)
        parent.addView(lottieView)
    } else {
        parent.addView(lottieView)
    }

    *//*if (container.contains(lottieView)) {
        container.removeView(lottieView)
        container.addView(lottieView)
    } else {
        container.addView(lottieView)
    }*//*

    lottieView.addAnimatorUpdateListener {
        updated.invoke()
    }
    lottieView.playAnimation()*/

    val animator: ValueAnimator = moveMarkerSmoothly(this, newLatLng)
    animator.start()
    val f = getAngle(
        LatLng(this.position.latitude, this.position.longitude),
        LatLng(newLatLng.latitude, newLatLng.longitude)
    ).toFloat()
    rotateMarker(this, f, rotate)
}

fun GoogleMap.addMarkerView(latLng: LatLng, view: View, parent: ViewGroup) {
    val param = RelativeLayout.LayoutParams(60.dp, 60.dp)
    parent.addView(view, param)

    //marker.alpha = 0.2f
    val currentPointMarker = projection.toScreenLocation(latLng)
    /*MainScope().launch {
        val xView = currentPointMarker.x
        val yView = currentPointMarker.y

        delay(70)
        view.move(currentPointMarker.toPointF())
        logd("v position -> ${view.measuredWidth} | ${view.measuredHeight}")
        logd("current position -> ${xView} | ${yView}")
    }*/

    val xView = currentPointMarker.x
    val yView = currentPointMarker.y

    view.moveJust(getCurrentPointF(latLng))
    logd("v position -> ${view.measuredWidth} | ${view.measuredHeight}")
    logd("current position -> ${xView} | ${yView}")

    setOnCameraMoveListener {
        val point = getCurrentPointF(latLng)
        logd("move... -> $point")
        view.moveJust(point)
    }

    /*setOnCameraMoveStartedListener {
        view.move(getCurrentPointF(latLng))
        logd("move start...")
    }

    setOnCameraIdleListener {
        view.move(getCurrentPointF(latLng))
        logd("idle...")
    }

    setOnCameraMoveCanceledListener {
        view.move(getCurrentPointF(latLng))
        logd("move cancel...")
    }*/

    //var currentPointMarker = projection.toScreenLocation(marker.position)

    //view.move(currentPointMarker.toPointF())
}

private fun GoogleMap.getCurrentPointF(latLng: LatLng): PointF {
    return projection.toScreenLocation(latLng).toPointF()
}

private fun View.moveJust(point: PointF) {
    translationX = point.x-(measuredWidth/2)
    translationY = point.y-measuredHeight
}

internal fun View.move(point: PointF) {
    animate()
        .translationX(point.x-(measuredWidth/2))
        .translationY(point.y-measuredHeight)
        .start()
}
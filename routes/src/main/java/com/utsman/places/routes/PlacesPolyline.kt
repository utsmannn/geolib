/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.utsman.places.routes.data.CalculationHelper
import com.utsman.places.routes.data.PolylineConfig
import com.utsman.places.routes.data.PolylineIdentifier
import com.utsman.places.routes.data.StackAnimationMode
import com.utsman.places.routes.polyline.PlacesPointPolyline

class PlacesPolyline(
    internal val googleMap: GoogleMap,
    internal var primaryColor: Int,
    internal var accentColor: Int
) {

    internal interface AnimationListener {
        fun onStartAnimation(latLng: LatLng)
        fun onEndAnimation(latLng: LatLng)
        fun onUpdate(latLng: LatLng, duration: Int)
    }

    internal var cameraAutoFocus = false
    internal var polylineOption1: PolylineOptions? = null
    internal var polylineOption2: PolylineOptions? = null
    internal var stackAnimationMode: StackAnimationMode? = null

    internal val initialPoints: MutableList<LatLng> = mutableListOf()
    internal val hasPolylines: MutableList<PolylineIdentifier?> = mutableListOf()

    fun startAnimate(
        geometries: List<LatLng>,
        actionConfig: (PolylineConfig.() -> Unit)? = null
    ): PlacesPointPolyline {
        val result = PlacesPointPolyline(this, stackAnimationMode)
        result.addPoints(geometries, actionConfig)
        return result
    }

    internal fun waitEndAnimate(
        polylineOptions1: PolylineOptions?,
        polylineOptions2: PolylineOptions?,
        geometries: List<LatLng>,
        duration: Long = 2000,
        listener: AnimationListener?,
        cameraPoint: Boolean = false
    ) {
        lateinit var newPolyline1: PolylineOptions
        lateinit var newPolyline2: PolylineOptions
        if (polylineOptions1 == null || polylineOptions2 == null) {
            newPolyline1 = PolylineOptions().apply {
                width(8f)
                color(primaryColor)
            }

            newPolyline2 = PolylineOptions().apply {
                width(8f)
                color(accentColor)
            }

        } else {
            newPolyline1 = polylineOptions1
            newPolyline2 = polylineOptions2
        }

        startAnimatePolyline(newPolyline2, geometries, duration, start = {
            // empty start
            listener?.onStartAnimation(geometries.first())
            if (cameraAutoFocus && !cameraPoint) {
                val bound = LatLngBounds.builder().apply {
                    initialPoints.distinct().forEach {
                        include(it)
                    }
                }.build()

                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 100))
            }
        }, end = {
            startAnimatePolyline(newPolyline1, geometries, duration / 2, start = {
                // empty start
            }, end = {
                // empty end
                listener?.onEndAnimation(geometries.last())
            }, onUpdate = { _, _ ->

            }, zIndex = 1f)
        }, onUpdate = { latLng, d ->
            listener?.onUpdate(latLng, d)
        }, zIndex = 10f)
    }

    internal fun blockStackAnimate(
        polylineOptions1: PolylineOptions?,
        polylineOptions2: PolylineOptions?,
        geometries: List<LatLng>,
        duration: Long = 2000,
        listener: AnimationListener?,
        cameraPoint: Boolean = false
    ) {
        lateinit var newPolyline1: PolylineOptions
        lateinit var newPolyline2: PolylineOptions
        if (polylineOptions1 == null || polylineOptions2 == null) {
            newPolyline1 = PolylineOptions().apply {
                width(8f)
                color(primaryColor)
            }

            newPolyline2 = PolylineOptions().apply {
                width(8f)
                color(accentColor)
            }

        } else {
            newPolyline1 = polylineOptions1
            newPolyline2 = polylineOptions2
        }

        startAnimatePolyline(newPolyline2, geometries, duration / 2, start = {
            listener?.onStartAnimation(geometries.first())
            if (cameraAutoFocus && !cameraPoint) {
                val bound = LatLngBounds.builder().apply {
                    initialPoints.distinct().forEach {
                        include(it)
                    }
                }.build()

                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 100))
            }
            startAnimatePolyline(newPolyline1, geometries, duration, start = {
                // empty start
            }, end = {
                // empty end
                listener?.onEndAnimation(geometries.last())
            }, onUpdate = { latLng, d ->
                //listener?.onUpdate(latLng, duration)
                listener?.onUpdate(latLng, d)
            }, zIndex = 10f)
        }, end = {
            // empty end
        }, onUpdate = { _, _ ->
            // on update
        }, zIndex = 1f)
    }

    internal fun offStackAnimate(
        polylineOptions: PolylineOptions?,
        geometries: List<LatLng>,
        duration: Long = 2000,
        listener: AnimationListener?,
        cameraPoint: Boolean = false
    ) {
        val newPolyline: PolylineOptions = polylineOptions
            ?: PolylineOptions().apply {
                width(8f)
                color(accentColor)
            }

        startAnimatePolyline(newPolyline, geometries, duration / 2, start = {
            listener?.onStartAnimation(geometries.first())
            if (cameraAutoFocus && !cameraPoint) {
                val bound = LatLngBounds.builder().apply {
                    initialPoints.distinct().forEach {
                        include(it)
                    }
                }.build()

                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 100))
            }
        }, end = {
            // empty end
            listener?.onEndAnimation(geometries.last())
        }, onUpdate = { latLng, d ->
            listener?.onUpdate(latLng, d)
            // on update
        }, zIndex = 1f)
    }

    private fun startAnimatePolyline(
        polylineOptions: PolylineOptions = PolylineOptions(),
        listLatLng: List<LatLng>,
        duration: Long,
        zIndex: Float,
        start: () -> Unit,
        end: () -> Unit,
        onUpdate: (LatLng, Int) -> Unit
    ) {
        var renderedPolyline: Polyline? = null
        val legs: List<Double> = CalculationHelper.calculateLegsLengths(listLatLng)
        val interpolator = DecelerateInterpolator()

        val totalPathDistance = legs.sum()
        val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.duration = duration
        interpolator.let {
            animator.interpolator = it
        }
        val polylines = mutableListOf<Polyline>()
        animator.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedValue as Float
            val pathSection = totalPathDistance * fraction / 100
            val copyPolylineOption = CalculationHelper.polylineUntilSection(
                listLatLng, legs, pathSection, polylineOptions.copyPolylineOptions()
            ) {
                onUpdate.invoke(it, (duration / 1000).toInt())
            }

            val newPolyline = googleMap.addPolyline(copyPolylineOption)
            renderedPolyline?.remove()
            renderedPolyline = newPolyline
            polylines.add(newPolyline)
            renderedPolyline?.zIndex = zIndex
        }

        val animatorListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                start.invoke()
            }

            override fun onAnimationEnd(animation: Animator?) {
                val hasPolylineAdded = hasPolylines.map { "${it?.firstLatLng}-${it?.lastLatLng}-${it?.zIndex}" }
                    .contains("${listLatLng.first()}-${listLatLng.last()}-$zIndex")

                if (!hasPolylineAdded) {
                    val identifier = PolylineIdentifier(
                        polyline = polylines,
                        firstLatLng = listLatLng.first(),
                        lastLatLng = listLatLng.last(),
                        zIndex = zIndex
                    )
                    hasPolylines.add(identifier)
                }

                end.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        }


        animator.addListener(animatorListener)
        animator.start()
    }
}
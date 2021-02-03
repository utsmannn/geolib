/*
 * Created on 1/2/21 10:08 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline.polyline

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.animation.DecelerateInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.utsman.places.polyline.AnimationListener
import com.utsman.places.polyline.data.*
import com.utsman.places.polyline.point.PointPolyline
import com.utsman.places.polyline.point.PointPolylineImpl
import com.utsman.places.polyline.utils.*

internal class PolylineAnimatorOptions(
    internal val googleMap: GoogleMap,
    internal var primaryColor: Int,
    internal var accentColor: Int = primaryColor.transparentColor()
) : PolylineAnimator {

    internal var cameraAutoFocus = false
    internal var polylineOption1: PolylineOptions? = null
    internal var polylineOption2: PolylineOptions? = null
    internal var stackAnimationMode: StackAnimationMode = StackAnimationMode.OffStackAnimation

    internal val initialPoints: MutableList<LatLng> = mutableListOf()
    internal val hasPolylines: MutableList<PolylineIdentifier?> = mutableListOf()

    private val shadowPolylineOptions by lazy {
        PolylineOptions().apply {
            color(Color.GRAY)
        }
    }

    override fun startAnimate(
        geometries: List<LatLng>,
        actionConfig: (PolylineConfig.() -> Unit)?
    ): PointPolyline {
        val result = PointPolylineImpl(this, stackAnimationMode)
        result.addPoints(geometries, actionConfig)
        return result
    }

    override fun getBindGoogleMaps(): GoogleMap {
        return googleMap
    }

    override fun getCurrentConfig(): PolylineConfigValue {
        return PolylineConfigValue(primaryColor, accentColor, stackAnimationMode, cameraAutoFocus)
    }

    internal fun waitEndAnimate(
        polylineOptions1: PolylineOptions?,
        polylineOptions2: PolylineOptions?,
        polylineOptionsBorder: PolylineOptions?,
        geometries: List<LatLng>,
        duration: Long = 2000,
        listener: AnimationListener?,
        cameraPoint: Boolean = false,
        drawMode: PolylineDrawMode
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

        checkIsCurved(drawMode, geometries, duration)
        startAnimatePolyline(newPolyline2, null, geometries, duration, start = {
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
            startAnimatePolyline(
                newPolyline1,
                polylineOptionsBorder,
                geometries,
                duration / 2,
                start = {
                    // empty start
                },
                end = {
                    // empty end
                    listener?.onEndAnimation(geometries.last())
                },
                onUpdate = { _, _ ->

                },
                zIndex = 10f
            )
        }, onUpdate = { latLng, d ->
            listener?.onUpdate(latLng, d)
        }, zIndex = 3f)
    }

    internal fun blockStackAnimate(
        polylineOptions1: PolylineOptions?,
        polylineOptions2: PolylineOptions?,
        polylineOptionsBorder: PolylineOptions?,
        geometries: List<LatLng>,
        duration: Long = 2000,
        listener: AnimationListener?,
        cameraPoint: Boolean = false,
        drawMode: PolylineDrawMode
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

        checkIsCurved(drawMode, geometries, duration)
        startAnimatePolyline(newPolyline2, null, geometries, duration / 2, start = {
            listener?.onStartAnimation(geometries.first())
            if (cameraAutoFocus && !cameraPoint) {
                val bound = LatLngBounds.builder().apply {
                    initialPoints.distinct().forEach {
                        include(it)
                    }
                }.build()

                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 100))
            }
            startAnimatePolyline(
                newPolyline1,
                polylineOptionsBorder,
                geometries,
                duration,
                start = {
                    // empty start
                },
                end = {
                    // empty end
                    listener?.onEndAnimation(geometries.last())
                },
                onUpdate = { latLng, d ->
                    //listener?.onUpdate(latLng, duration)
                    listener?.onUpdate(latLng, d)
                },
                zIndex = 10f
            )
        }, end = {
            // empty end
        }, onUpdate = { _, _ ->
            // on update
        }, zIndex = 3f)
    }

    internal fun offStackAnimate(
        polylineOptions: PolylineOptions?,
        polylineOptionsBorder: PolylineOptions?,
        geometries: List<LatLng>,
        duration: Long = 2000,
        listener: AnimationListener?,
        cameraPoint: Boolean = false,
        drawMode: PolylineDrawMode
    ) {
        val newPolyline: PolylineOptions = polylineOptions
            ?: PolylineOptions().apply {
                width(8f)
                color(accentColor)
            }

        checkIsCurved(drawMode, geometries, duration)
        startAnimatePolyline(newPolyline, polylineOptionsBorder, geometries, duration / 2, start = {
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
        }, zIndex = 3f)
    }

    private fun checkIsCurved(
        drawMode: PolylineDrawMode,
        geometries: List<LatLng>,
        duration: Long
    ) {
        if (drawMode is PolylineDrawMode.Curved) {
            val geometriesLank =
                CalculationHelper.geometriesLank(listOf(geometries.first(), geometries.last()))
            startAnimatePolyline(
                shadowPolylineOptions,
                null,
                geometriesLank,
                duration,
                start = {},
                end = {},
                zIndex = 1f,
                onUpdate = { _, _ -> })
        }
    }

    private fun startAnimatePolyline(
        polylineOptions: PolylineOptions,
        polylineOptionsBorder: PolylineOptions?,
        geometries: List<LatLng>,
        duration: Long,
        zIndex: Float,
        start: () -> Unit,
        end: () -> Unit,
        onUpdate: (LatLng, Int) -> Unit
    ) {
        var renderedPolyline: Polyline? = null
        var renderedPolylineBorder: Polyline? = null

        logd("geometri size is ---> ${geometries.size}")

        val legs: List<Double> = CalculationHelper.calculateLegsLengths(geometries)
        val interpolator = DecelerateInterpolator()

        val totalPathDistance = legs.sum()
        val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.duration = duration
        animator.interpolator = interpolator

        val polylines = mutableListOf<Polyline>()
        val polylinesBorder = mutableListOf<Polyline>()

        animator.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedValue as Float
            val pathSection = totalPathDistance * fraction / 100
            val copyPolylineOption = CalculationHelper.polylineUntilSection(
                geometries, legs, pathSection, polylineOptions.copyPolylineOptions()
            ) {
                onUpdate.invoke(it, (duration / 1000).toInt())
            }

            val newPolyline = googleMap.addPolyline(copyPolylineOption)
            if (polylineOptionsBorder != null) {
                val copyPolylineOptionBorder = CalculationHelper.polylineUntilSection(
                    geometries, legs, pathSection, polylineOptionsBorder.copyPolylineOptions()
                ) {}
                val borderPolyline = googleMap.addPolyline(copyPolylineOptionBorder)
                renderedPolylineBorder?.remove()
                renderedPolylineBorder = borderPolyline
                polylinesBorder.add(borderPolyline)
                renderedPolylineBorder?.zIndex = zIndex - 1
            }
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
                val currentGeoId = geometries.toGeoId()
                val currentGeoIdZIndex = geometries.toGeoIdZIndex(zIndex)

                val hasPolylineAdded = hasPolylines.map { it?.toGeoIdZIndex() }
                    .contains(currentGeoIdZIndex)

                if (!hasPolylineAdded) {
                    val identifier = PolylineIdentifier(
                        polyline = polylines,
                        geoId = currentGeoId,
                        zIndex = zIndex
                    )
                    hasPolylines.add(identifier)
                }

                if (polylineOptionsBorder != null) {
                    val currentBorderGeoId = geometries.toGeoId()
                    val currentBorderGeoIdZIndex = geometries.toGeoIdZIndex(zIndex - 1)

                    val hasPolylineBorderAdded = hasPolylines.map {
                        it?.toGeoIdZIndex()
                    }.contains(currentBorderGeoIdZIndex)

                    if (!hasPolylineBorderAdded) {
                        val identifier = PolylineIdentifier(
                            polyline = polylinesBorder,
                            geoId = currentBorderGeoId,
                            zIndex = zIndex - 1
                        )
                        hasPolylines.add(identifier)
                    }
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
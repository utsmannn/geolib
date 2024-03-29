/*
 * Created on 1/2/21 10:09 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.polyline.point

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.utsman.geolib.polyline.AnimationListener
import com.utsman.geolib.polyline.data.PolylineConfig
import com.utsman.geolib.polyline.data.PolylineDrawMode
import com.utsman.geolib.polyline.data.StackAnimationMode
import com.utsman.geolib.polyline.polyline.PolylineAnimatorOptions
import com.utsman.geolib.polyline.utils.CalculationHelper
import com.utsman.geolib.polyline.utils.logd
import com.utsman.geolib.polyline.utils.toGeoId
import com.utsman.geolib.polyline.utils.transparentColor

internal class PointPolylineImpl(
    private val polylineAnimatorOptions: PolylineAnimatorOptions,
    private val stackAnimationMode: StackAnimationMode?
) : PointPolyline {
    private lateinit var geometries: List<LatLng>
    private var config: PolylineConfig? = null

    override fun addPoints(
        newGeometries: List<LatLng>,
        polylineConfig: PolylineConfig
    ): PointPolyline {
        config = polylineConfig
        val geometriesWithMode = when (polylineConfig.polylineDrawMode) {
            is PolylineDrawMode.Normal -> newGeometries
            is PolylineDrawMode.Curved -> CalculationHelper.geometriesCurved(newGeometries)
            is PolylineDrawMode.Lank -> CalculationHelper.geometriesLank(
                listOf(
                    newGeometries.first(),
                    newGeometries.last()
                )
            )
        }

        polylineAnimatorOptions.initialPoints.addAll(geometriesWithMode)
        geometries = geometriesWithMode

        val polylineOptions1 =
            polylineConfig.polylineOptions1 ?: polylineAnimatorOptions.polylineOption1
            ?: PolylineOptions().apply {
                width(10f)
                color(polylineAnimatorOptions.primaryColor)
            }

        val polylineOptions2 =
            polylineConfig.polylineOptions2 ?: polylineAnimatorOptions.polylineOption2
            ?: PolylineOptions().apply {
                width(10f)
                color(polylineOptions1.color.transparentColor())
            }

        if (polylineConfig.cameraAutoUpdate) {
            val latLngBounds = LatLngBounds.builder().apply {
                newGeometries.forEach {
                    include(it)
                }
            }.build()

            val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(latLngBounds, 100)
            polylineAnimatorOptions.googleMap.animateCamera(cameraUpdateFactory)
        }

        val listener = object : AnimationListener {
            override fun onStartAnimation(latLng: LatLng) {
                polylineConfig.doOnStartAnim?.invoke(latLng)
            }

            override fun onEndAnimation(latLng: LatLng) {
                polylineConfig.doOnEndAnim?.invoke(latLng)
            }

            override fun onUpdate(latLng: LatLng, duration: Int) {
                polylineConfig.doOnUpdateAnim?.invoke(latLng, duration)
            }
        }

        val animationMode = polylineConfig.stackAnimationMode
            ?: stackAnimationMode
            ?: StackAnimationMode.BlockStackAnimation

        val drawMode = polylineConfig.polylineDrawMode

        logd("pattern impl is --> ${polylineAnimatorOptions.polylineOption1?.pattern}")
        logd("pattern impl 2 is --> ${polylineAnimatorOptions.polylineOption2?.pattern}")
        when (animationMode) {
            is StackAnimationMode.WaitStackEndAnimation -> {
                polylineAnimatorOptions.waitEndAnimate(
                    polylineOptions1,
                    polylineOptions2,
                    polylineConfig.polylineOptionsBorder,
                    geometriesWithMode,
                    polylineConfig.duration,
                    listener,
                    polylineConfig.cameraAutoUpdate,
                    drawMode
                )
            }
            is StackAnimationMode.BlockStackAnimation -> {
                polylineAnimatorOptions.blockStackAnimate(
                    polylineOptions1,
                    polylineOptions2,
                    polylineConfig.polylineOptionsBorder,
                    geometriesWithMode,
                    polylineConfig.duration,
                    listener,
                    polylineConfig.cameraAutoUpdate,
                    drawMode
                )
            }
            is StackAnimationMode.OffStackAnimation -> {
                polylineAnimatorOptions.offStackAnimate(
                    polylineOptions1,
                    polylineConfig.polylineOptionsBorder,
                    geometriesWithMode,
                    polylineConfig.duration,
                    listener,
                    polylineConfig.cameraAutoUpdate,
                    drawMode
                )
            }
        }
        return this
    }

    override fun addPoint(newLatLng: LatLng, extend: Boolean): PointPolyline {
        val currentLatLng = geometries.last()
        val newGeometries = if (extend) {
            CalculationHelper.geometriesLank(listOf(currentLatLng, newLatLng))
        } else {
            listOf(currentLatLng, newLatLng)
        }
        return addPoints(newGeometries, config ?: PolylineConfig())
    }

    override fun remove(withGeometries: List<LatLng>?): Boolean {
        val geo = withGeometries ?: geometries
        return if (this::geometries.isInitialized) {
            val currentGeoId = geo.toGeoId()
            val findPolyline = polylineAnimatorOptions.hasPolylines.filter {
                it?.geoId == currentGeoId
            }

            if (!findPolyline.isNullOrEmpty()) {
                findPolyline.forEach { p ->
                    p?.polyline?.forEach {
                        it.isVisible = false
                        it.remove()
                    }
                    polylineAnimatorOptions.hasPolylines.remove(p)
                }
            }
            findPolyline.isNullOrEmpty()
        } else {
            false
        }
    }
}
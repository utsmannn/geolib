/*
 * Created on 1/2/21 10:09 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline.point

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.utsman.places.polyline.AnimationListener
import com.utsman.places.polyline.data.PolylineConfig
import com.utsman.places.polyline.data.StackAnimationMode
import com.utsman.places.polyline.polyline.PlacesPolylineOptions
import com.utsman.places.polyline.utils.toGeoId

internal class PlacesPointPolylineImpl(
    private val placesPolylineOptions: PlacesPolylineOptions,
    private val stackAnimationMode: StackAnimationMode?
) : PlacesPointPolyline {

    private lateinit var geometries: List<LatLng>

    override fun addPoints(
        newGeometries: List<LatLng>,
        actionConfig: (PolylineConfig.() -> Unit)?
    ): PlacesPointPolyline {
        placesPolylineOptions.initialPoints.addAll(newGeometries)
        geometries = newGeometries

        val polylineConfig = if (actionConfig != null) {
            PolylineConfig().apply(actionConfig)
        } else {
            PolylineConfig()
        }

        val polylineOptions1 =
            polylineConfig.polylineOptions1 ?: placesPolylineOptions.polylineOption1
            ?: PolylineOptions().apply {
                width(8f)
                color(placesPolylineOptions.primaryColor)
            }

        val polylineOptions2 =
            polylineConfig.polylineOptions2 ?: placesPolylineOptions.polylineOption2
            ?: PolylineOptions().apply {
                width(8f)
                color(placesPolylineOptions.accentColor)
            }

        if (polylineConfig.cameraAutoUpdate) {
            val latLngBounds = LatLngBounds.builder().apply {
                newGeometries.forEach {
                    include(it)
                }
            }.build()

            val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(latLngBounds, 100)
            placesPolylineOptions.googleMap.animateCamera(cameraUpdateFactory)
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

        when (animationMode) {
            is StackAnimationMode.WaitStackEndAnimation -> {
                placesPolylineOptions.waitEndAnimate(
                    polylineOptions1,
                    polylineOptions2,
                    newGeometries,
                    polylineConfig.duration,
                    listener,
                    polylineConfig.cameraAutoUpdate
                )
            }
            is StackAnimationMode.BlockStackAnimation -> {
                placesPolylineOptions.blockStackAnimate(
                    polylineOptions1,
                    polylineOptions2,
                    newGeometries,
                    polylineConfig.duration,
                    listener,
                    polylineConfig.cameraAutoUpdate
                )
            }
            is StackAnimationMode.OffStackAnimation -> {
                placesPolylineOptions.offStackAnimate(
                    polylineOptions1,
                    newGeometries,
                    polylineConfig.duration,
                    listener,
                    polylineConfig.cameraAutoUpdate
                )
            }
        }
        return this
    }

    override fun remove(withGeometries: List<LatLng>?): Boolean {
        val geo = withGeometries ?: geometries
        return if (this::geometries.isInitialized) {
            val currentGeoId = geo.toGeoId()
            val findPolyline = placesPolylineOptions.hasPolylines.filter {
                it?.geoId == currentGeoId
            }

            if (!findPolyline.isNullOrEmpty()) {
                findPolyline.forEach { p ->
                    p?.polyline?.forEach {
                        it.isVisible = false
                        it.remove()
                    }
                    placesPolylineOptions.hasPolylines.remove(p)
                }
            }
            findPolyline.isNullOrEmpty()
        } else {
            false
        }
    }
}
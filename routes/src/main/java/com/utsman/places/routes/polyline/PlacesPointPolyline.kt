/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.polyline

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.utsman.places.routes.PlacesPolyline
import com.utsman.places.routes.data.PolylineConfig
import com.utsman.places.routes.data.StackAnimationMode

class PlacesPointPolyline(
    private val placesPolyline: PlacesPolyline,
    private val stackAnimationMode: StackAnimationMode?
) {

    private lateinit var geometries: List<LatLng>

    fun addPoints(
        newGeometries: List<LatLng>,
        actionConfig: (PolylineConfig.() -> Unit)? = null
    ): PlacesPointPolyline {
        placesPolyline.initialPoints.addAll(newGeometries)
        geometries = newGeometries

        val polylineConfig = if (actionConfig != null) {
            PolylineConfig().apply(actionConfig)
        } else {
            PolylineConfig()
        }

        val polylineOptions1 = polylineConfig.polylineOptions1 ?: placesPolyline.polylineOption1
        ?: PolylineOptions().apply {
            width(8f)
            color(placesPolyline.primaryColor)
        }

        val polylineOptions2 = polylineConfig.polylineOptions2 ?: placesPolyline.polylineOption2
        ?: PolylineOptions().apply {
            width(8f)
            color(placesPolyline.accentColor)
        }

        if (polylineConfig.cameraAutoUpdate) {
            val latLngBounds = LatLngBounds.builder().apply {
                newGeometries.forEach {
                    include(it)
                }
            }.build()

            val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(latLngBounds, 100)
            placesPolyline.googleMap.animateCamera(cameraUpdateFactory)
        }

        val listener = object : PlacesPolyline.AnimationListener {
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

        when (polylineConfig.stackAnimationMode ?: stackAnimationMode
        ?: StackAnimationMode.BlockStackAnimation) {
            is StackAnimationMode.WaitStackEndAnimation -> {
                placesPolyline.waitEndAnimate(
                    polylineOptions1,
                    polylineOptions2,
                    newGeometries,
                    polylineConfig.duration,
                    listener,
                    polylineConfig.cameraAutoUpdate
                )
            }
            is StackAnimationMode.BlockStackAnimation -> {
                placesPolyline.blockStackAnimate(
                    polylineOptions1,
                    polylineOptions2,
                    newGeometries,
                    polylineConfig.duration,
                    listener,
                    polylineConfig.cameraAutoUpdate
                )
            }
            is StackAnimationMode.OffStackAnimation -> {
                placesPolyline.offStackAnimate(
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

    fun remove(withGeometries: List<LatLng>? = null): Boolean {
        val geo = withGeometries ?: geometries
        return if (this::geometries.isInitialized) {
            val findPolyline = placesPolyline.hasPolylines.filter {
                it?.firstLatLng == geo.first() && it.lastLatLng == geo.last()
            }

            findPolyline.forEach { p ->
                p?.polyline?.forEach {
                    it.isVisible = false
                    it.remove()
                }
            }
            findPolyline.isNotEmpty()
        } else {
            false
        }
    }
}
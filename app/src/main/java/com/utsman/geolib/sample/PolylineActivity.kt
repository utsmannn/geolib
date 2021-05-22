/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.sample

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.utsman.geolib.location.createPlacesLocation
import com.utsman.geolib.marker.moveMarker
import com.utsman.geolib.polyline.point.PointPolyline
import com.utsman.geolib.polyline.data.StackAnimationMode
import com.utsman.geolib.polyline.utils.*
import com.utsman.geolib.routes.*
import com.utsman.geolib.routes.data.TransportMode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PolylineActivity : AppCompatActivity() {

    private val placesRoute by lazy {
        createPlacesRoute(getString(R.string.here_maps_api))
    }

    private val btnPoly1 by lazy { findViewById<Button>(R.id.btn_polyline_1) }
    private val btnPoly2 by lazy { findViewById<Button>(R.id.btn_polyline_2) }
    private val btnPoly3 by lazy { findViewById<Button>(R.id.btn_polyline_3) }

    private var poly1HasRender = false
    private var poly2HasRender = false
    private var poly3HasRender = false

    private lateinit var point1: PointPolyline
    private lateinit var point2: PointPolyline
    private lateinit var point3: PointPolyline

    private var markerPoly3: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_polyline)

        val mapsFragment =
            supportFragmentManager.findFragmentById(R.id.maps_view) as SupportMapFragment

        val fusedProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val placesLocation = fusedProviderClient.createPlacesLocation(HERE_API)

        lifecycleScope.launch {
            val googleMap = mapsFragment.awaitMap().apply {
                uiSettings.isZoomControlsEnabled = true
                setPadding(0, 0, 0, 200)
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center.toLatLng(), 14f))

            val polylineAnimator = googleMap.createPolylineAnimatorBuilder()
                .createPolylineAnimator()

            btnPoly1.setOnClickListener {
                lifecycleScope.launch {

                    val result = placesRoute.searchRoute {
                        startLocation = firstPoint1
                        endLocation = firstPoint2
                        transportMode = TransportMode.BIKE
                    }

                    result.onSuccess { first ->
                        if (!poly1HasRender) {
                            point1 = polylineAnimator.startAnimate(first.geometries) {
                                stackAnimationMode = StackAnimationMode.BlockStackAnimation
                            }
                            poly1HasRender = true
                        } else {
                            point1.remove()
                            poly1HasRender = false
                        }
                    }

                    result.onFailure {
                        it.printStackTrace()
                    }
                }
            }

            btnPoly2.setOnClickListener {
                lifecycleScope.launch {
                    val result = placesRoute.searchRoute {
                        startLocation = secondPoint1
                        endLocation = secondPoint2
                        transportMode = TransportMode.BIKE
                    }

                    result.onSuccess { second ->
                        if (!poly2HasRender) {
                            point2 = polylineAnimator.startAnimate(second.geometries) {
                                stackAnimationMode = StackAnimationMode.WaitStackEndAnimation
                                enableBorder(true, Color.RED)
                                withPrimaryPolyline {
                                    width(8f)
                                    color(Color.BLUE)
                                }
                                withAccentPolyline {
                                    width(8f)
                                    color(Color.GREEN)
                                }
                                doOnStartAnimation {
                                    toast("start...")
                                    googleMap.addMarker {
                                        this.position(it)
                                    }
                                }
                                doOnEndAnimation {
                                    googleMap.addMarker {
                                        this.position(it)
                                    }
                                    toast("end...")
                                }
                            }
                            poly2HasRender = true
                        } else {
                            point2.remove()
                            poly2HasRender = false
                        }
                    }

                    result.onFailure {
                        it.printStackTrace()
                    }
                }
            }

            btnPoly3.setOnClickListener {
                lifecycleScope.launch {
                    val initialLatLng = placesLocation.getLocationFlow().first()
                        .toLatLng()

                    placesLocation.getLocationFlow().collect { location ->
                        val updatedLatLng = location.toLatLng()
                        if (markerPoly3 == null) {
                            markerPoly3 = googleMap.addMarker {
                                position(updatedLatLng)
                            }
                        }

                        if (initialLatLng != updatedLatLng) {
                            if (!this@PolylineActivity::point3.isInitialized) {
                                val geometries = listOf(initialLatLng, updatedLatLng)
                                point3 = polylineAnimator.startAnimate(geometries) {
                                    stackAnimationMode = StackAnimationMode.OffStackAnimation
                                    withPrimaryPolyline {
                                        width(8f)
                                        color(Color.GREEN)
                                    }
                                    enableBorder(true, Color.RED, 5)
                                    doOnUpdateAnimation { latLng, _ ->
                                        markerPoly3?.position = latLng
                                    }
                                }
                            } else {
                                point3.addPoint(updatedLatLng)
                            }
                        }
                    }

                    /*val result = placesRoute.searchRoute {
                        startLocation = thirdPoint1
                        endLocation = thirdPoint2
                        transportMode = TransportMode.BIKE
                    }

                    result.onSuccess { third ->
                        if (!poly3HasRender) {
                            markerPoly3 = googleMap.addMarker {
                                position(thirdPoint1.toLatLng())
                            }

                            point3 = polylineAnimator.startAnimate(third.geometries) {
                                duration = 10000
                                stackAnimationMode = StackAnimationMode.OffStackAnimation
                                withPrimaryPolyline {
                                    width(8f)
                                    color(Color.GREEN)
                                }
                                enableBorder(true, Color.RED, 5)
                                doOnUpdateAnimation { latLng, _ ->
                                    markerPoly3?.position = latLng
                                }
                            }
                            poly3HasRender = true
                        } else {
                            point3.remove()
                            markerPoly3?.remove()
                            markerPoly3 = null
                            poly3HasRender = false
                        }
                    }

                    result.onFailure {
                        it.printStackTrace()
                    }*/
                }
            }
        }
    }
}
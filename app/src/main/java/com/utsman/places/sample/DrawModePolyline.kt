/*
 * Created on 3/2/21 3:16 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.ktx.awaitMap
import com.utsman.places.location.createPlacesLocation
import com.utsman.places.polyline.data.PolylineDrawMode
import com.utsman.places.polyline.data.StackAnimationMode
import com.utsman.places.polyline.utils.createPolylineAnimatorBuilder
import com.utsman.places.polyline.utils.withAnimate
import com.utsman.places.routes.createPlacesRoute
import com.utsman.places.routes.data.TransportMode
import com.utsman.places.utils.doOnFailure
import com.utsman.places.utils.doOnSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DrawModePolyline : AppCompatActivity() {

    private val placesRoute by lazy {
        createPlacesRoute(getString(R.string.here_maps_api)).apply {
            enableLog = true
        }
    }

    private val btnPoly1 by lazy { findViewById<Button>(R.id.btn_polyline_1) }
    private val btnPoly2 by lazy { findViewById<Button>(R.id.btn_polyline_2) }
    private val btnPoly3 by lazy { findViewById<Button>(R.id.btn_polyline_3) }

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

                    /*result.doOnSuccess { first ->
                        val options = PolylineOptions()
                            .addAll(first.geometries)
                            .color(Color.BLUE)
                            .startCap(RoundCap())
                            .endCap(RoundCap())

                        googleMap.addPolyline(options).withAnimate(polylineAnimator) {
                            polylineDrawMode = PolylineDrawMode.Normal
                        }
                    }*/

                    /*result.doOnFailure {
                        it.printStackTrace()
                    }*/

                    val options = PolylineOptions()
                        .addAll(result?.geometries)
                        .color(Color.BLUE)
                        .startCap(RoundCap())
                        .endCap(RoundCap())

                    googleMap.addPolyline(options).withAnimate(polylineAnimator) {
                        polylineDrawMode = PolylineDrawMode.Normal
                    }
                }
            }

            btnPoly2.setOnClickListener {
                lifecycleScope.launch {
                    val result = placesRoute.searchRoute {
                        startLocation = secondPoint1
                        endLocation = uki
                        transportMode = TransportMode.BIKE
                    }

                    val options = PolylineOptions()
                        .addAll(result?.geometries)
                        .color(Color.RED)
                        .startCap(RoundCap())
                        .endCap(RoundCap())

                    googleMap.addPolyline(options).withAnimate(polylineAnimator) {
                        polylineDrawMode = PolylineDrawMode.Curved
                    }

                    /*result.doOnSuccess { second ->
                        val options = PolylineOptions()
                            .addAll(second.geometries)
                            .color(Color.RED)
                            .startCap(RoundCap())
                            .endCap(RoundCap())

                        googleMap.addPolyline(options).withAnimate(polylineAnimator) {
                            polylineDrawMode = PolylineDrawMode.Curved
                        }
                    }

                    result.doOnFailure {
                        it.printStackTrace()
                    }*/
                }
            }

            btnPoly3.setOnClickListener {
                lifecycleScope.launch {
                    val result = placesRoute.searchRoute {
                        startLocation = thirdPoint1
                        endLocation = secondPoint2
                        transportMode = TransportMode.BIKE
                    }

                    lifecycleScope.launch {
                        delay(4000)
                        val options = PolylineOptions()
                            .addAll(result?.geometries)
                            .color(Color.GRAY)
                            .startCap(RoundCap())
                            .endCap(RoundCap())

                        googleMap.addPolyline(options).withAnimate(polylineAnimator) {
                            stackAnimationMode = StackAnimationMode.BlockStackAnimation
                            polylineDrawMode = PolylineDrawMode.Lank
                        }
                    }

                    /*result.doOnSuccess { third ->
                        logd("data is ----> $third")
                        toast("loaded..")

                        lifecycleScope.launch {
                            delay(4000)
                            val options = PolylineOptions()
                                .addAll(third.geometries)
                                .color(Color.GRAY)
                                .startCap(RoundCap())
                                .endCap(RoundCap())

                            googleMap.addPolyline(options).withAnimate(polylineAnimator) {
                                stackAnimationMode = StackAnimationMode.BlockStackAnimation
                                polylineDrawMode = PolylineDrawMode.Lank
                            }
                        }
                    }

                    result.doOnFailure {
                        it.printStackTrace()
                    }*/
                }
            }

        }
    }
}
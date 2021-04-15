/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.utsman.places.polyline.data.PolylineDrawMode
import com.utsman.places.polyline.point.PointPolyline
import com.utsman.places.polyline.data.StackAnimationMode
import com.utsman.places.polyline.utils.*
import com.utsman.places.routes.*
import com.utsman.places.routes.data.TransportMode
import com.utsman.places.utils.doOnFailure
import com.utsman.places.utils.doOnSuccess
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

                    if (result != null) {
                        if (!poly1HasRender) {
                            point1 = polylineAnimator.startAnimate(result.geometries) {
                                stackAnimationMode = StackAnimationMode.BlockStackAnimation
                            }
                            poly1HasRender = true
                        } else {
                            point1.remove()
                            poly1HasRender = false
                        }
                    }

                    /*result.doOnSuccess { first ->
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

                    result.doOnFailure {
                        it.printStackTrace()
                    }*/
                }
            }

            btnPoly2.setOnClickListener {
                lifecycleScope.launch {
                    val result = placesRoute.searchRoute {
                        startLocation = secondPoint1
                        endLocation = secondPoint2
                        transportMode = TransportMode.BIKE
                    }

                    if (result != null) {


                        if (!poly2HasRender) {
                            point2 = polylineAnimator.startAnimate(result.geometries) {
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

                    /*result.doOnSuccess { second ->
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

                    result.doOnFailure {
                        it.printStackTrace()
                    }*/
                }
            }

            btnPoly3.setOnClickListener {
                lifecycleScope.launch {
                    val result = placesRoute.searchRoute {
                        startLocation = thirdPoint1
                        endLocation = thirdPoint2
                        transportMode = TransportMode.BIKE
                    }

                    if (result != null) {
                        if (!poly3HasRender) {
                            markerPoly3 = googleMap.addMarker {
                                position(thirdPoint1.toLatLng())
                            }

                            point3 = polylineAnimator.startAnimate(result.geometries) {
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

                    /*result.doOnSuccess { third ->
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

                    result.doOnFailure {
                        it.printStackTrace()
                    }*/
                }
            }
        }
    }
}
/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.utsman.places.location.PlacesLocation
import com.utsman.places.location.createPlacesLocation
import com.utsman.places.routes.*
import com.utsman.places.routes.data.StackAnimationMode
import com.utsman.places.routes.data.TransportMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapsFragment =
            supportFragmentManager.findFragmentById(R.id.maps_view) as SupportMapFragment
        val placesRoute = createPlacesRoute(getString(R.string.here_maps_api))
        placesRoute.enableLog = false

        val fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        val placesLocation = fusedLocation.createPlacesLocation(getString(R.string.here_maps_api))

        lifecycleScope.launchWhenCreated {
            val googleMap = mapsFragment.awaitMap().apply {
                uiSettings.isZoomControlsEnabled = true
                setPadding(0, 0, 0, 100)
            }
            googleMap.animateCamera(center.toCameraUpdate(13f))

            val polylineBuilder = googleMap.createPlacesPolylineBuilder()
                .withStackAnimationMode(StackAnimationMode.OffStackAnimation)
                .withCameraAutoFocus(true)

            val polyline = polylineBuilder.createAnimatePolyline()

            findViewById<Button>(R.id.btn_start).setOnClickListener {
                googleMap.clear()
                startTestPoly(placesRoute, polyline, googleMap)
                //startObserverLocation(placesLocation, polyline)
            }
        }
    }

    private fun startObserverLocation(placesLocation: PlacesLocation, polyLine: PlacesPolyline) {
        lifecycleScope.launch {
            val latLngs = mutableListOf<LatLng>()
            var lastLatLng = listOf<LatLng>()
            val current = placesLocation.getLocationFlow().first()
            latLngs.add(current.toLatLng())
            val start = polyLine.startAnimate(latLngs) {
                stackAnimationMode = StackAnimationMode.OffStackAnimation
            }
            var width = 6f
            placesLocation.getLocationFlow()
                .collect {
                    val latLng = it.toLatLng()
                    latLngs.add(latLng)
                    val geometries = latLngs.distinct()
                    start.addPoints(geometries) {
                        cameraAutoUpdate = true
                        stackAnimationMode = StackAnimationMode.OffStackAnimation
                        duration = 1000
                        doOnEndAnimation {
                            if (lastLatLng.isNotEmpty()) {
                                start.remove(lastLatLng)
                            }
                            lastLatLng = geometries
                        }
                    }
                }
        }
    }

    private fun startTestPoly(
        placesRoute: PlacesRoute,
        polyline: PlacesPolyline,
        googleMap: GoogleMap
    ) {
        lifecycleScope.launch {
            val route = placesRoute.searchRoute {
                startLocation = buaran
                endLocation = rawamangun
                transportMode = TransportMode.BIKE
            }

            val start = polyline.startAnimate(route.geometries) {
                duration = 10000
                cameraAutoUpdate = true
                withAccentPolyline {
                    width(8f)
                    color(Color.CYAN)
                }
                withPrimaryPolyline {
                    width(8f)
                    color(Color.BLUE)
                }
                doOnStartAnimation {
                    toast("start...")
                    googleMap.addMarker {
                        this.position(it)
                    }
                }
            }

            delay(1000)
            val routeLast = placesRoute.searchRoute {
                startLocation = rawamangun
                endLocation = rawabadak
                transportMode = TransportMode.BIKE
            }

            val middle = start.addPoints(routeLast.geometries) {
                stackAnimationMode = StackAnimationMode.BlockStackAnimation
                duration = 4000
                doOnUpdateAnimation { latLng, mapCameraDuration ->
                    logd("updated ---> $latLng")
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
                    googleMap.animateCamera(cameraUpdate, mapCameraDuration, null)
                }
            }

            findViewById<Button>(R.id.btn_remove).setOnClickListener {
                middle.remove()
            }

            delay(2000)
            val routeToInitial = placesRoute.searchRoute {
                startLocation = rawabadak
                endLocation = center
                transportMode = TransportMode.BIKE
            }

            val end = middle.addPoints(routeToInitial.geometries) {
                //cameraAutoUpdate = true
                stackAnimationMode = StackAnimationMode.WaitStackEndAnimation
                duration = 2000
                doOnEndAnimation {
                    googleMap.addMarker {
                        this.position(it)
                    }
                    toast("end...")
                }
            }

            delay(3000)
            toast("start remove")
            end.remove()
        }
    }
}
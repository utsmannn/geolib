/*
 * Created on 4/13/21 12:56 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.ktx.awaitMap
import com.utsman.geolib.location.PlacesLocation
import com.utsman.geolib.location.createPlacesLocation
import com.utsman.geolib.marker.*
import com.utsman.geolib.marker.adapter.MarkerViewAdapter
import com.utsman.geolib.marker.config.AnchorPoint
import com.utsman.geolib.marker.config.SizeLayer
import com.utsman.geolib.marker.data.MarkerView
import kotlinx.coroutines.launch

class MarkerViewActivity : AppCompatActivity() {

    private val btnMarker1 by lazy { findViewById<Button>(R.id.btn_polyline_1) }
    private val btnMarker2 by lazy { findViewById<Button>(R.id.btn_polyline_2) }
    private val btnMarker3 by lazy { findViewById<Button>(R.id.btn_polyline_3) }

    private val markerViewAdapter by lazy { MarkerViewAdapter(this) }

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
            markerViewAdapter.bindGoogleMaps(googleMap)

            /**
             * for listen camera move, use this
             * don't use original function!
             * */
            markerViewAdapter.setOnCameraMoveListener {
                logd("move........")
            }

            btnMarker1.setOnClickListener {
                val marker = addLottieMarker(googleMap, placesLocation)

                /*lifecycleScope.launch {
                    delay(5000)
                    toast("removed..")
                    val hasMarker = markerViewAdapter.getMarkerView(marker.id)
                    logd("has marker -> ${hasMarker != null}")
                    val removing = markerViewAdapter.removeMarkerView(hasMarker)
                    toast("removing success -> $removing")
                }*/
            }

            btnMarker2.setOnClickListener {
                addLottieRadar(googleMap)
            }

            btnMarker3.setOnClickListener {
                googleMap.clearAllLayers(markerViewAdapter)
            }
        }
    }

    private fun addLottieMarker(googleMap: GoogleMap, placesLocation: PlacesLocation): MarkerView {
        val lottieView = LottieAnimationView(this)
        lottieView.setAnimation(R.raw.marker)
        // fix padding marker
        lottieView.setPadding(0, 0, 0, (-20).dp)
        lottieView.loop(true)
        lottieView.playAnimation()

        val markerWindowView = LayoutInflater.from(this).inflate(R.layout.marker_window_view, null)

        val marker = markerViewAdapter.addMarkerView {
            id = "marker_animation"
            view = lottieView
            windowView = {
                height = 30.dp
                width = 100.dp
                view = markerWindowView
            }
            latLng = firstPoint1.toLatLng()
        }

        /*MainScope().launch {
            placesLocation.getComparisonLocation().collect { comparator ->
                val newLatLng = comparator.currentLocation.toLatLng()
                marker.moveMarker(newLatLng, googleMap, true)
            }
        }*/

        marker.addMarkerViewClickListener { id, position ->
            toast("id -> $id, pos -> $position")
        }

        marker.addWindowMarkerViewClickListener { id, position ->
            toast("window id -> $id, pos -> $position")
        }

        return marker
    }

    private fun addLottieRadar(googleMap: GoogleMap) {
        val radarAnimationUrl = "https://assets8.lottiefiles.com/packages/lf20_FWNl1W.json"
        val lottieView = LottieAnimationView(this)
        lottieView.setAnimationFromUrl(radarAnimationUrl)
        lottieView.loop(true)
        lottieView.playAnimation()

        val marker = markerViewAdapter.addMarkerView {
            id = "marker_radar"
            view = lottieView
            latLng = center.toLatLng()
            anchorPoint = AnchorPoint.CENTER
            sizeLayer = SizeLayer.Custom(300.dp, 300.dp)
        }

        marker.addMarkerViewClickListener { id, position ->
            toast("id -> $id, pos -> $position")
        }
    }
}
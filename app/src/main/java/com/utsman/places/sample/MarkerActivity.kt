/*
 * Created on 4/10/21 1:32 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.utsman.places.location.PlacesLocation
import com.utsman.places.location.createPlacesLocation
import com.utsman.places.marker.moveMarker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MarkerActivity : AppCompatActivity() {

    private val btnMarker1 by lazy { findViewById<Button>(R.id.btn_polyline_1) }
    private val btnMarker2 by lazy { findViewById<Button>(R.id.btn_polyline_2) }
    private val btnMarker3 by lazy { findViewById<Button>(R.id.btn_polyline_3) }

    private var markerCat: Marker? = null
    private var markerCustom: Marker? = null

    private var marker1Job: Job? = null
    private var marker2Job: Job? = null

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

            btnMarker1.setOnClickListener {
                logd("setup..")
                markerCat?.remove()
                marker2Job?.cancel()

                if (marker1Job == null) {
                    marker1Job = lifecycleScope.launch {
                        setupMarkerCustom(googleMap, placesLocation)
                    }
                } else {
                    marker1Job?.cancel()
                }
            }

            btnMarker2.setOnClickListener {
                markerCat?.remove()
                marker1Job?.cancel()

                if (marker2Job == null) {
                    marker2Job = lifecycleScope.launch {
                        setupMarkerCat(googleMap, placesLocation)
                    }
                } else {
                    marker2Job?.cancel()
                }
            }
        }
    }

    private suspend fun setupMarkerCat(googleMap: GoogleMap, placesLocation: PlacesLocation) {
        val markerAdapter = CatMarkerAdapter(this)

        markerCat = googleMap.addMarker {
            position(center.toLatLng())
            icon(markerAdapter.getIconView())
        }

        placesLocation.getComparisonLocation().collect { comparisonLocation ->
            val updatedLatLng = comparisonLocation.currentLocation.toLatLng()
            markerCat?.moveMarker(newLatLng = updatedLatLng, rotate = false)
            logd("current -> ${comparisonLocation.previousLocation?.simpleString()} | ${comparisonLocation.currentLocation.simpleString()}")
        }
    }

    private suspend fun setupMarkerCustom(googleMap: GoogleMap, placesLocation: PlacesLocation) {
        logd("setup custom..")
        val markerAdapter = CustomMarkerAdapter(this)

        markerCustom = googleMap.addMarker {
            position(center.toLatLng())
            icon(markerAdapter.getIconView())
        }

        placesLocation.getComparisonLocation().collect { comparisonLocation ->
            val updatedLatLng = comparisonLocation.currentLocation.toLatLng()
            markerCustom?.moveMarker(newLatLng = updatedLatLng, rotate = false)
            logd("current -> ${comparisonLocation.previousLocation?.simpleString()} | ${comparisonLocation.currentLocation.simpleString()}")
        }
    }
}
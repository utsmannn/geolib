/*
 * Created on 1/2/21 2:01 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ktx.awaitMap
import com.utsman.places.routes.createPlacesRoute
import com.utsman.places.routes.data.TransportMode
import kotlinx.coroutines.launch

class RouteActivity : AppCompatActivity() {

    private val txtFrom: TextView by lazy {
        findViewById(R.id.txt_from)
    }

    private val txtTo: TextView by lazy {
        findViewById(R.id.txt_to)
    }

    private val btnGetRoute: Button by lazy {
        findViewById(R.id.btn_route)
    }

    private val placeRoute by lazy {
        createPlacesRoute(HERE_API)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        val mapsFragment =
            supportFragmentManager.findFragmentById(R.id.maps_view) as SupportMapFragment

        lifecycleScope.launch {
            val googleMap = mapsFragment.awaitMap()

            val latLngBounds = LatLngBounds.builder()
                .include(buaran.toLatLng())
                .include(rawabadak.toLatLng())
                .build()

            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

            txtFrom.text = "From: Buaran (${buaran.latitude},${buaran.longitude})"
            txtTo.text = "From: Rawabadak (${rawabadak.latitude},${rawabadak.latitude})"

            btnGetRoute.setOnClickListener {
                btnGetRoute.isEnabled = false
                lifecycleScope.launch {
                    val routeData = placeRoute.searchRoute {
                        startLocation = buaran
                        endLocation = rawabadak
                        transportMode = TransportMode.CAR
                    }

                    val geometriesRoute = routeData.geometries
                    val polylineOptions = PolylineOptions()
                        .addAll(geometriesRoute)

                    googleMap.addPolyline(polylineOptions)
                    btnGetRoute.isEnabled = true
                }
            }
        }
    }
}
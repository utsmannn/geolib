/*
 * Created on 1/2/21 2:01 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.ktx.awaitMap
import com.utsman.places.polyline.data.PolylineDrawMode
import com.utsman.places.polyline.data.StackAnimationMode
import com.utsman.places.polyline.utils.withAnimate
import com.utsman.places.polyline.utils.withPrimaryPolyline
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
            val googleMap = mapsFragment.awaitMap().apply {
                uiSettings.isZoomControlsEnabled = true
            }

            val latLngBounds = LatLngBounds.builder()
                .include(buaran.toLatLng())
                .include(rawabadak.toLatLng())
                .build()

            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

            txtFrom.text = "From: Buaran (${buaran.latitude},${buaran.longitude})"
            txtTo.text = "To: Rawabadak (${rawabadak.latitude},${rawabadak.latitude})"

            btnGetRoute.setOnClickListener {
                btnGetRoute.isEnabled = false
                lifecycleScope.launch {
                    val routeData = placeRoute.searchRoute {
                        startLocation = cakung
                        endLocation = buaran
                        transportMode = TransportMode.CAR
                    }

                    val geometriesRoute = routeData.geometries
                    val polylineOptions = PolylineOptions()
                        /*.add(cakung.toLatLng())
                        .add(buaran.toLatLng())*/
                        .addAll(geometriesRoute)

                    googleMap.addPolyline(polylineOptions).withAnimate(googleMap) {
                        stackAnimationMode = StackAnimationMode.BlockStackAnimation
                        polylineDrawMode = PolylineDrawMode.Curved
                        withPrimaryPolyline {
                            color(Color.RED)
                            startCap(RoundCap())
                            endCap(RoundCap())
                        }

                        //enableBorder(true, color = Color.GREEN, width = 10)
                    }
                    btnGetRoute.isEnabled = true
                }
            }
        }
    }
}
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
import com.google.android.gms.maps.model.*
import com.google.maps.android.ktx.awaitMap
import com.utsman.places.polyline.data.PolylineDrawMode
import com.utsman.places.polyline.data.StackAnimationMode
import com.utsman.places.polyline.utils.addPolyline
import com.utsman.places.polyline.utils.buildAnimationConfig
import com.utsman.places.polyline.utils.withAnimate
import com.utsman.places.polyline.utils.withPrimaryPolyline
import com.utsman.places.routes.createPlacesRoute
import com.utsman.places.routes.data.TransportMode
import com.utsman.places.utils.doOnFailure
import com.utsman.places.utils.doOnSuccess
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
                    val result = placeRoute.searchRoute {
                        startLocation = cakung
                        endLocation = buaran
                        transportMode = TransportMode.CAR
                    }

                    result.doOnSuccess {
                        logd(it.toString())
                        /*val geometriesRoute = it.geometries

                        val pattern = listOf(Dot(), Gap(10f))
                        val polylineOptions = PolylineOptions()
                            .addAll(geometriesRoute)
                            .color(Color.BLUE)

                        googleMap.addPolyline(polylineOptions).withAnimate(googleMap, polylineOptions) {
                            stackAnimationMode = StackAnimationMode.BlockStackAnimation
                            polylineDrawMode = PolylineDrawMode.Curved
                            *//*withPrimaryPolyline {
                                startCap(RoundCap())
                                endCap(RoundCap())
                                color(Color.GREEN)
                            }*//*
                        }
                        btnGetRoute.isEnabled = true*/
                    }

                    result.doOnFailure {
                        toast(it.message ?: "Error")
                    }
                }
            }
        }
    }
}
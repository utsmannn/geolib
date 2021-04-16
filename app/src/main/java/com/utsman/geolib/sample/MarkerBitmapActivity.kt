/*
 * Created on 4/10/21 1:32 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.sample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch

class MarkerBitmapActivity : AppCompatActivity() {

    private val btnMarker1 by lazy { findViewById<Button>(R.id.btn_polyline_1) }
    private val btnMarker2 by lazy { findViewById<Button>(R.id.btn_polyline_2) }
    private val btnMarker3 by lazy { findViewById<Button>(R.id.btn_polyline_3) }

    private var markerCat: Marker? = null
    private var markerCustom: Marker? = null

    private val catMarkerAdapter by lazy { CatMarkerAdapter(this) }
    private val customMarkerView by lazy { CustomMarkerAdapter(this) }

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

            btnMarker1.setOnClickListener {
                lifecycleScope.launch {
                    val iconBitmap = catMarkerAdapter.getIconView()
                    markerCat = googleMap.addMarker {
                        position(center.toLatLng())
                        icon(iconBitmap)
                    }
                }
            }

            btnMarker2.setOnClickListener {
                lifecycleScope.launch {
                    val iconCustom = customMarkerView.getIconView()
                    markerCustom = googleMap.addMarker {
                        position(firstPoint1.toLatLng())
                        icon(iconCustom)
                    }
                }
            }

            btnMarker3.setOnClickListener {

            }
        }
    }
}
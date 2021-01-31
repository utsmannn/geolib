/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.utsman.places.location.createPlacesLocation
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

typealias strings = R.string

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtLog = findViewById<TextView>(R.id.txt_log)

        val fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        val hereApiKey = getString(strings.here_maps_api)

        val placesLocation = fusedLocation.createPlacesLocation(hereApiKey)
        placesLocation.enableLog = false

        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    MainScope().launch {
                        placesLocation.getLocationFlow()
                            .collect {
                                val places = placesLocation.getPlacesLocation(it)
                                txtLog.text = places.toString()
                            }
                    }
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                }

            })
            .check()

        findViewById<Button>(R.id.btn_to_search).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        findViewById<Button>(R.id.btn_to_route).setOnClickListener {
            startActivity(Intent(this, RouteActivity::class.java))
        }

        findViewById<Button>(R.id.btn_to_maps).setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }
}
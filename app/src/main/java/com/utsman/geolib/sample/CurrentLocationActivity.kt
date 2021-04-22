/*
 * Created on 31/1/21 10:20 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.utsman.geolib.location.createPlacesLocation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CurrentLocationActivity : AppCompatActivity() {

    private val btnGetCurrentLocation by lazy { findViewById<Button>(R.id.btn_current_location) }
    private val txtResult by lazy { findViewById<TextView>(R.id.txt_result) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)

        val fusedProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val placesLocation = fusedProviderClient.createPlacesLocation(HERE_API)

        btnGetCurrentLocation.setOnClickListener {
            lifecycleScope.launch {
                val currentLocation = placesLocation.getLocationFlow().first()
                txtResult.text = "${currentLocation.longitude},${currentLocation.longitude}"
                val result = placesLocation.getPlacesLocation(currentLocation)
                result.onSuccess {
                    val currentPlace = it.first()
                    txtResult.append("\n${currentPlace.title} \n\n${currentPlace.address}")
                }
                result.onFailure {
                    toast("Failure -> ${it.message}")
                    it.printStackTrace()
                }
            }
        }
    }
}
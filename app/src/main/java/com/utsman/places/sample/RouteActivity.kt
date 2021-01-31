/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.utsman.places.routes.createPlacesRoute
import com.utsman.places.routes.data.TransportMode
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RouteActivity : AppCompatActivity() {

    private val placesRoute by lazy {
        createPlacesRoute(getString(R.string.here_maps_api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        MainScope().launch {
            val route = placesRoute.searchRoute {
                startLocation = buaran
                endLocation = rawamangun
                transportMode = TransportMode.BIKE
            }

            val polyline = route.encodedPolyline
            val length = route.length

            findViewById<TextView>(R.id.txt_result).text = length.toString()
            logd(polyline)
            logd("data is --> \n$route")
            val geometry = route.geometries
            logd("geometris is --> \n$geometry")
        }
    }
}

fun logd(message: String) = Log.d("SAMPLE", message)
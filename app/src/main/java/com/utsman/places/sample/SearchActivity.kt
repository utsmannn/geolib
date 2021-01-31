/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import com.utsman.places.location.createPlacesLocation
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private val placesLocation by lazy {
        LocationServices.getFusedLocationProviderClient(this)
            .createPlacesLocation(getString(strings.here_maps_api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val editQuery = findViewById<EditText>(R.id.edit_query)
        editQuery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 2) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        searchPlace(s.toString())
                    }, 1000)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun searchPlace(query: String) {
        MainScope().launch {
            val currentLocation = placesLocation.getLocationFlow().first()
            val data = placesLocation.searchPlaces(currentLocation, query)
            val txtResult = findViewById<TextView>(R.id.txt_result)
            txtResult.text = data.toString()
        }
    }
}
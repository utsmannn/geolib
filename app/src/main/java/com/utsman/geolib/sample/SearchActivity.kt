/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.sample

import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.utsman.geolib.location.createPlacesLocation
import com.utsman.geolib.location.data.PlaceData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    class ViewHolderAdapter(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(placeData: PlaceData) = itemView.run {
            val txtPlaceName = findViewById<TextView>(R.id.txt_place_name)
            val txtPlaceAddress = findViewById<TextView>(R.id.txt_address)
            val txtDistanceKm = findViewById<TextView>(R.id.txt_distance)

            txtPlaceName.text = placeData.title
            txtPlaceAddress.text = placeData.address
            txtDistanceKm.text = placeData.distanceInKm
        }
    }

    class Adapter : RecyclerView.Adapter<ViewHolderAdapter>() {
        var items = listOf<PlaceData>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAdapter {
            return ViewHolderAdapter(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_search_view, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolderAdapter, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    private val placesLocation by lazy {
        LocationServices.getFusedLocationProviderClient(this)
            .createPlacesLocation(getString(R.string.here_maps_api))
    }

    private val searchAdapter by lazy { Adapter() }
    private val editQuery by lazy { findViewById<EditText>(R.id.edit_query) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.rv_places) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }

        lifecycleScope.launch {
            val currentLocation = placesLocation.getLocationFlow().first()

            editQuery.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (count > 2) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            searchPlace(s.toString(), currentLocation)
                        }, 1000)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

        }
    }

    private fun searchPlace(query: String, currentLocation: Location) {
        MainScope().launch {
            val result = placesLocation.searchPlaces(currentLocation, query)
            result.onSuccess {
                val data = it
                searchAdapter.items = data
                searchAdapter.notifyDataSetChanged()
            }
            result.onFailure {
                toast("failure -> ${it.message}")
            }
        }
    }
}
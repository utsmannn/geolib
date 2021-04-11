/*
 * Created on 4/10/21 1:32 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.utsman.places.location.PlacesLocation
import com.utsman.places.location.createPlacesLocation
import com.utsman.places.marker.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MarkerActivity : AppCompatActivity() {

    private val btnMarker1 by lazy { findViewById<Button>(R.id.btn_polyline_1) }
    private val btnMarker2 by lazy { findViewById<Button>(R.id.btn_polyline_2) }
    private val btnMarker3 by lazy { findViewById<Button>(R.id.btn_polyline_3) }

    private var markerCat: Marker? = null
    private var markerCustom: Marker? = null
    private var markerLottie: Marker? = null

    private var marker1Job: Job? = null
    private var marker2Job: Job? = null
    private var marker3Job: Job? = null

    private val markerViewAdapter by lazy { MarkerViewAdapter(this) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_polyline)

        val mapsFragment =
            supportFragmentManager.findFragmentById(R.id.maps_view) as SupportMapFragment

        val fusedProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val placesLocation = fusedProviderClient.createPlacesLocation(HERE_API)

        mapsFragment.view?.isFocusableInTouchMode = true
        mapsFragment.view?.setOnTouchListener { v, event ->
            val x = event.rawX
            val y = event.rawY
            logd("touch on -> $x | $y")
            true
        }

        lifecycleScope.launch {
            val googleMap = mapsFragment.awaitMap().apply {
                uiSettings.isZoomControlsEnabled = true
                setPadding(0, 0, 0, 200)
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center.toLatLng(), 14f))
            markerViewAdapter.bindGoogleMaps(googleMap)

            markerViewAdapter.setOnCameraMoveListener {
                logd("move........")
            }

            btnMarker1.setOnClickListener {
                /*logd("setup..")
                markerCat?.remove()
                marker2Job?.cancel()

                if (marker1Job == null) {
                    marker1Job = lifecycleScope.launch {
                        setupMarkerCustom(googleMap, placesLocation)
                    }
                } else {
                    marker1Job?.cancel()
                }*/
                /*lifecycleScope.launch {

                }*/
                setupMarkerCustom(googleMap, placesLocation)
            }

            btnMarker2.setOnClickListener {
                /*markerCat?.remove()
                marker1Job?.cancel()

                if (marker2Job == null) {
                    marker2Job = lifecycleScope.launch {
                        setupMarkerCat(googleMap, mapsFragment.view, placesLocation)
                    }
                } else {
                    marker2Job?.cancel()
                }*/

                /*findViewById<Button>(R.id.btn_test).animate()
                    .translationY(100f)
                    .translationX(0f)
                    .start()*/
                //findViewById<Button>(R.id.btn_test).isVisible = false

                /*ObjectAnimator.ofFloat(findViewById<Button>(R.id.btn_test), "translationX", 100f).apply {
                    duration = 2000
                    start()
                }*/

                ///ObjectAnimator.ofFloa


                setupMarkerCat(googleMap, mapsFragment.view, placesLocation)
            }

            // 540 | 725

            btnMarker3.setOnClickListener {
                if (marker3Job == null) {
                    marker3Job = lifecycleScope.launch {
                        setupMarkerLottie(googleMap, placesLocation)
                    }
                } else {
                    marker3Job?.cancel()
                }
            }
        }
    }

    private fun setupMarkerCat(googleMap: GoogleMap, mapView: View?, placesLocation: PlacesLocation) {
       /* val lottieAnimLayout = LayoutInflater.from(this).inflate(R.layout.marker_lottie, null)
        val lottieAnimView = lottieAnimLayout.findViewById<LottieAnimationView>(R.id.lottie_view)*/
        val lottieView = LottieAnimationView(this)
        lottieView.setAnimation(R.raw.marker)
        lottieView.setPadding(0, 0, 0, (-20).dp)
        lottieView.loop(true)
        lottieView.playAnimation()

        markerViewAdapter.addMarker {
            view = lottieView
            latLng = center.toLatLng()
        }

        /*val param = RelativeLayout.LayoutParams(100.dp, 100.dp)
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP)*/

        //containerLottie.addView(lottieView, param)

        //val markerAdapter = CatMarkerAdapter(this)

        /*markerCat = googleMap.addMarker {
            position(center.toLatLng())
            //icon(markerAdapter.getIconView())
        }*/

        //googleMap.addMarkerView(center.toLatLng(), lottieView, parentLayout, "lottie_2")

        /*placesLocation.getComparisonLocation().collect { comparisonLocation ->
            val point = googleMap.projection.toScreenLocation(comparisonLocation.currentLocation.toLatLng())
            logd("x -> ${point.x} | y -> ${point.y}")
            val updatedLatLng = comparisonLocation.currentLocation.toLatLng()
            markerCat?.moveMarker(newLatLng = updatedLatLng, rotate = false)
            logd("current -> ${comparisonLocation.previousLocation?.simpleString()} | ${comparisonLocation.currentLocation.simpleString()}")
        }*/

    }

    private val cameraMoveListener = GoogleMap.OnCameraMoveListener { }

    private fun setupMarkerCustom(googleMap: GoogleMap, placesLocation: PlacesLocation) {
        /*logd("setup custom..")
        val markerAdapter = CustomMarkerAdapter(this)

        markerCustom = googleMap.addMarker {
            position(center.toLatLng())
            icon(markerAdapter.getIconView())
        }

        placesLocation.getComparisonLocation().collect { comparisonLocation ->
            val updatedLatLng = comparisonLocation.currentLocation.toLatLng()
            markerCustom?.moveMarker(newLatLng = updatedLatLng, rotate = false)
            logd("current -> ${comparisonLocation.previousLocation?.simpleString()} | ${comparisonLocation.currentLocation.simpleString()}")
        }*/

        val lottieView = LottieAnimationView(this)
        lottieView.setAnimation(R.raw.marker)
        lottieView.setPadding(0, 0, 0, (-20).dp)
        lottieView.loop(true)
        lottieView.playAnimation()

        val marker = markerViewAdapter.addMarker {
            view = lottieView
            latLng = firstPoint1.toLatLng()
        }

        MainScope().launch {
            placesLocation.getComparisonLocation().collect { comparator ->
                logd("comparator -> ${(comparator.previousLocation ?: Location("")).simpleString()} | ${comparator.currentLocation.simpleString()} == ${comparator.previousLocation?.distanceTo(comparator.currentLocation)}")
                //marker.moveMarker(comparator.previousLocation?.toLatLng(), comparator.currentLocation, googleMap)
                val prevLatLng = comparator.previousLocation?.toLatLng()
                val newLatLng = comparator.currentLocation.toLatLng()
                if (prevLatLng != null) {
                    marker.moveMarker(prevLatLng, newLatLng, googleMap)
                }
            }
        }

        //googleMap.addMarkerView(firstPoint1.toLatLng(), lottieView, parentLayout, "lottie_1")
    }

    private suspend fun setupMarkerLottie(googleMap: GoogleMap, placesLocation: PlacesLocation) {
        val lottieView = LottieAnimationView(this)
        lottieView.run {
            setAnimation(R.raw.marker)
        }
        logd("setup custom..")
        val markerAdapter = LottieMarkerAdapter(this)

        markerLottie = googleMap.addMarker {
            position(center.toLatLng())
            icon(markerAdapter.getIconView())
        }

        placesLocation.getComparisonLocation().collect { comparisonLocation ->
            val updatedLatLng = comparisonLocation.currentLocation.toLatLng()
            //markerLottie?.moveMarker(newLatLng = updatedLatLng, rotate = false)
            val lottieAnimLayout = LayoutInflater.from(this).inflate(R.layout.marker_lottie, null)
            val lottieAnimView = lottieAnimLayout.findViewById<LottieAnimationView>(R.id.lottie_view)


            logd("current -> ${comparisonLocation.previousLocation?.simpleString()} | ${comparisonLocation.currentLocation.simpleString()}")
        }
    }
}
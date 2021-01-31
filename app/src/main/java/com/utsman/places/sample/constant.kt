/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.content.Context
import android.location.Location
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

fun Context.toast(msg:String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

val buaran = Location("").apply {
    latitude = -6.222471
    longitude = 106.9223605
}

val rawamangun = Location("").apply {
    latitude = -6.192736
    longitude = 106.8759893
}

val rawabadak = Location("").apply {
    latitude = -6.129162
    longitude = 106.8999633
}

val center = Location("").apply {
    latitude = -6.218141
    longitude = 106.9055753
}

val gunungBatu = Location("").apply {
    latitude = -6.60602
    longitude = 107.0543233
}

val gunungBatu2 = Location("").apply {
    latitude = -6.591606
    longitude = 107.0565503
}

fun Location.toLatLng() = LatLng(latitude, longitude)

fun Location.toCameraUpdate(zoom: Float = 16f): CameraUpdate =
    CameraUpdateFactory.newLatLngZoom(toLatLng(), zoom)

fun LatLng.toCameraUpdate(zoom: Float = 16f): CameraUpdate =
    CameraUpdateFactory.newLatLngZoom(this, zoom)
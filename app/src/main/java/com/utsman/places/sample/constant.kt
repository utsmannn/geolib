/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.content.Context
import android.content.Intent
import android.location.Location
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

fun Context.toast(msg:String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

val Context.HERE_API get() = this.getString(R.string.here_maps_api)

infix fun Context.intent(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

val center = Location("").apply {
    latitude = -6.23111
    longitude = 106.8786825
}

val firstPoint1 = Location("").apply {
    latitude = -6.239557
    longitude = 106.869241
}

val firstPoint2 = Location("").apply {
    latitude = -6.221938
    longitude = 106.868082
}

val secondPoint1 = Location("").apply {
    latitude = -6.239770
    longitude = 106.878511
}

val secondPoint2 = Location("").apply {
    latitude = -6.219250
    longitude = 106.877481
}

val thirdPoint1 = Location("").apply {
    latitude = -6.239344
    longitude = 106.888424
}

val thirdPoint2 = Location("").apply {
    latitude = -6.225436
    longitude = 106.887137
}

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

val center1 = Location("").apply {
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
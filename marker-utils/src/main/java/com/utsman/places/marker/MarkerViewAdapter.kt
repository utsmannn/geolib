/*
 * Created on 4/10/21 1:55 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

abstract class MarkerViewAdapter(private val context: Context, layoutRes: Int) {

    lateinit var view: View
    var markerRatio: MarkerRatio = MarkerRatio(100, 100)

    init {
        bindLayout(layoutRes)
    }

    private fun bindLayout(layoutRes: Int) {
        val inflater = LayoutInflater.from(context).inflate(layoutRes, null)
        view = inflater
        onViewCreated(inflater)
    }

    fun setupMarkerRatio(markerRatio: MarkerRatio): MarkerViewAdapter {
        this.markerRatio = markerRatio
        return this
    }

    fun getIconView(): BitmapDescriptor {
        val bitmap = createBitmapMarkerFromLayout(this)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    abstract fun onViewCreated(view: View)
}
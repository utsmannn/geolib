/*
 * Created on 4/13/21 1:46 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker.adapter

import android.view.View
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.utsman.places.marker.createBitmapMarkerFromLayout

abstract class MarkerBitmapAdapter {

    suspend fun getIconView(): BitmapDescriptor {
        val bitmap = createBitmapMarkerFromLayout(this)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    abstract suspend fun createView(): View
    abstract fun maxWidth(): Int
    abstract fun maxHeight(): Int
}
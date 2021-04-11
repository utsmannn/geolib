/*
 * Created on 4/11/21 11:16 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker

import android.view.View
import com.google.android.gms.maps.model.LatLng
import java.util.*

data class MarkerView(
    val view: View,
    val latLng: LatLng,
    val tag: String? = "marker_view_${UUID.randomUUID()}"
) {
    data class MarkerViewConfig(
        var view: View? = null,
        val latLng: LatLng? = null,
        val tag: String? = "marker_view_${UUID.randomUUID()}"
    )
}

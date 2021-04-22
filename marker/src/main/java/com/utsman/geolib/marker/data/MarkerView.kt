/*
 * Created on 4/13/21 1:46 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.marker.data

import android.view.View
import androidx.core.view.isVisible
import com.google.android.gms.maps.model.LatLng
import com.utsman.geolib.marker.config.AnchorPoint
import com.utsman.geolib.marker.config.SizeLayer
import java.util.*

data class MarkerView(
    var id: String = UUID.randomUUID().toString(),
    var position: LatLng,
    internal val view: View,
    internal val windowViewConfig: WindowViewConfig? = null,
    internal var anchorPoint: AnchorPoint = AnchorPoint.NORMAL
) {
    data class MarkerViewConfig(
        var id: String = UUID.randomUUID().toString(),
        var view: View? = null,
        var windowView: (WindowViewConfig.() -> Unit)? = null,
        var latLng: LatLng? = null,
        var tag: String = UUID.randomUUID().toString(),
        var anchorPoint: AnchorPoint = AnchorPoint.NORMAL,
        var sizeLayer: SizeLayer = SizeLayer.Marker
    )

    data class WindowViewConfig(
        var width: Int? = null,
        var height: Int? = null,
        var view: View? = null
    )

    var isVisible: Boolean
        get() {
            return view.isVisible
        }
        set(value) {
            view.isVisible = value
        }

    fun addMarkerViewClickListener(onMarkerClick: (id: String, position: LatLng) -> Unit) {
        view.setOnClickListener { onMarkerClick.invoke(id, position) }
    }

    fun addWindowMarkerViewClickListener(onMarkerClick: (id: String, position: LatLng) -> Unit) {
        windowViewConfig?.view?.setOnClickListener { onMarkerClick.invoke(id, position) }
    }
}

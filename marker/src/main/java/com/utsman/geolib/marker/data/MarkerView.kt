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
    @JvmField
    var id: String = UUID.randomUUID().toString(),
    @JvmField
    var position: LatLng,
    @JvmField
    internal val view: View,
    @JvmField
    internal val windowViewConfig: WindowViewConfig? = null,
    @JvmField
    internal var anchorPoint: AnchorPoint = AnchorPoint.NORMAL
) {
    data class MarkerViewConfig(
        @JvmField
        var id: String = UUID.randomUUID().toString(),
        @JvmField
        var view: View? = null,
        @JvmField
        var windowView: (WindowViewConfig.() -> Unit)? = null,
        @JvmField
        var latLng: LatLng? = null,
        @JvmField
        var tag: String = UUID.randomUUID().toString(),
        @JvmField
        var anchorPoint: AnchorPoint = AnchorPoint.NORMAL,
        @JvmField
        var sizeLayer: SizeLayer = SizeLayer.Marker
    )

    data class WindowViewConfig(
        @JvmField
        var width: Int? = null,
        @JvmField
        var height: Int? = null,
        @JvmField
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

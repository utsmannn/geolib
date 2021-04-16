/*
 * Created on 4/13/21 1:46 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.utsman.places.marker.config.SizeLayer
import com.utsman.places.marker.data.MarkerView
import com.utsman.places.marker.dp
import com.utsman.places.marker.getCurrentPointF
import com.utsman.places.marker.moveJust
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MarkerViewAdapter {
    private lateinit var googleMap: GoogleMap
    private val moveFlow: MutableStateFlow<(() -> Unit)?> = MutableStateFlow(null)
    private val markerViews: MutableList<MarkerView> = mutableListOf()
    private var parent: ViewGroup

    constructor(activity: AppCompatActivity) {
        this.parent = activity.findViewById(android.R.id.content)
    }

    constructor(parentView: ViewGroup) {
        this.parent = parentView
    }

    fun bindGoogleMaps(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (this::googleMap.isInitialized) {
            googleMap.setOnCameraMoveListener {
                markerViews.forEach { mark ->
                    mark.view.moveJust(googleMap.getCurrentPointF(mark.position), mark.anchorPoint)
                }

                MainScope().launch {
                    moveFlow.collect { unit ->
                        unit?.invoke()
                    }
                }
            }
        }
    }

    fun setOnCameraMoveListener(onCameraMove: () -> Unit) {
        moveFlow.value = onCameraMove
    }

    fun getMarkerView(id: String): MarkerView? {
        return markerViews.find { mark -> mark.id == id }
    }

    fun removeMarkerView(markerView: MarkerView?): Boolean {
        markerViews.mapIndexed { index, markView ->
            if (markerView?.id == markView.id) {
                parent.removeView(markView.view)
                markerViews.removeAt(index)
                return true
            }
        }
        return false
    }

    fun removeAllMarkerView(): Boolean {
        if (markerViews.isNotEmpty()) {
            markerViews.forEach { marker ->
                parent.removeView(marker.view)
            }
            markerViews.clear()
            return true
        }
        return false
    }

    fun addMarkerView(config: MarkerView.MarkerViewConfig.() -> Unit): MarkerView {
        val markerViewConfig = MarkerView.MarkerViewConfig().apply(config)
        return if (markerViewConfig.view == null && markerViewConfig.latLng == null) {
            throw IllegalAccessError("")
        } else {
            MarkerView(
                id = markerViewConfig.id,
                view = markerViewConfig.view!!,
                position = markerViewConfig.latLng!!,
                anchorPoint = markerViewConfig.anchorPoint
            ).apply {
                if (!markerViews.map { marks -> marks.id }.contains(markerViewConfig.id)) {
                    view.tag = "marker_view_${markerViewConfig.tag}"
                    markerViews.add(this)
                    val param = when (markerViewConfig.sizeLayer) {
                        is SizeLayer.Marker -> ViewGroup.LayoutParams(60.dp, 60.dp)
                        is SizeLayer.Normal -> ViewGroup.LayoutParams(120.dp, 120.dp)
                        is SizeLayer.Custom -> {
                            val width = (markerViewConfig.sizeLayer as SizeLayer.Custom).width
                            val height = (markerViewConfig.sizeLayer as SizeLayer.Custom).height
                            ViewGroup.LayoutParams(width, height)
                        }
                    }
                    view.isInvisible = true
                    parent.addView(view, param)

                    MainScope().launch {
                        delay(70)
                        val point = googleMap.getCurrentPointF(position)
                        view.moveJust(point, markerViewConfig.anchorPoint)
                        view.isInvisible = false
                    }
                } else {
                    Log.e("MarkerView Error", "id: ${markerViewConfig.id} has been added")
                }
            }
        }
    }
}
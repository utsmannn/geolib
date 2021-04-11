/*
 * Created on 4/11/21 10:23 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker

import android.util.Log
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.google.android.gms.maps.GoogleMap
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
                    mark.view.moveJust(googleMap.getCurrentPointF(mark.position))
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

    fun addMarker(config: MarkerView.MarkerViewConfig.() -> Unit): MarkerView {
        val markerViewConfig = MarkerView.MarkerViewConfig().apply(config)
        return if (markerViewConfig.view == null && markerViewConfig.latLng == null) {
            throw IllegalAccessError("")
        } else {
            MarkerView(
                view = markerViewConfig.view!!,
                position = markerViewConfig.latLng!!
            ).apply {
                view.tag = "marker_view_${markerViewConfig.tag}"
                markerViews.add(this)
                val param = RelativeLayout.LayoutParams(60.dp, 60.dp)
                view.isInvisible = true
                parent.addView(view, param)

                MainScope().launch {
                    delay(70)
                    val point = googleMap.getCurrentPointF(position)
                    view.moveJust(point)
                    view.isInvisible = false
                }
            }
        }
    }
}
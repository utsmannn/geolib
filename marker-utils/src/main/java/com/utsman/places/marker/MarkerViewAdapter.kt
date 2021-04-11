/*
 * Created on 4/11/21 10:23 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker

import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class MarkerViewAdapter {
    private lateinit var googleMap: GoogleMap
    private val moveFlow: MutableStateFlow<(() -> Unit)?> = MutableStateFlow(null)
    private val markerViews: MutableList<MarkerView> = mutableListOf()
    private lateinit var parent: ViewGroup

    fun bindGoogleMaps(googleMap: GoogleMap, activity: AppCompatActivity) {
        this.googleMap = googleMap
        parent = activity.findViewById(android.R.id.content)
        googleMap.setOnCameraMoveListener {
            MainScope().launch {
                moveFlow.collect { unit ->
                    unit?.invoke()
                }

                markerViews.forEach {
                    val point = googleMap.getCurrentPointF(it.latLng)
                    it.view.moveJust(point)
                }
            }
        }
    }

    fun setOnCameraMoveListener(onCameraMove: () -> Unit) {
        moveFlow.value = onCameraMove
    }

    fun addMarker(config: (MarkerView.MarkerViewConfig) -> Unit): MarkerView {
        val markerViewConfig = MarkerView.MarkerViewConfig().apply(config)
        return if (markerViewConfig.view == null && markerViewConfig.latLng == null) {
            throw IllegalAccessError("")
        } else {
            MarkerView(
                view = markerViewConfig.view!!,
                latLng = markerViewConfig.latLng!!,
                tag = markerViewConfig.tag
            ).apply {
                if (!markerViews.map { it.latLng }.contains(latLng)) {
                    markerViews.add(this)
                }

                val param = RelativeLayout.LayoutParams(60.dp, 60.dp)
                parent.addView(view, param)
            }
        }
    }
}
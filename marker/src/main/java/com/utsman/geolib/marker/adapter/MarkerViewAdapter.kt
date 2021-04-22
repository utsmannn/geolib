/*
 * Created on 4/13/21 1:46 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.marker.adapter

import android.graphics.PointF
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.google.android.gms.maps.GoogleMap
import com.utsman.geolib.core.GeolibException
import com.utsman.geolib.marker.*
import com.utsman.geolib.marker.config.SizeLayer
import com.utsman.geolib.marker.data.MarkerView
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

    private val paddingWindowMarker = 20.dp
    private val paddingSide = 10.dp

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
                    val point = googleMap.getCurrentPointF(mark.position)
                    mark.view.moveJust(point, mark.anchorPoint)

                    val parentXSize = this.parent.width.toFloat()
                    logd("point is -> ${point.x} | ${point.y} ||| parent size -> $parentXSize")

                    if (mark.windowViewConfig?.view != null) {
                        val markerView = mark.windowViewConfig.view
                        if (markerView != null) {
                            val halfViewWidth = markerView.measuredWidth / 2

                            when {
                                point.x < 0f -> {
                                    val newPointX = point.x + halfViewWidth.toFloat() + paddingSide
                                    val newPointY = point.y
                                    val newPoint = PointF(newPointX, newPointY)

                                    markerView.moveJustOffset(
                                        point = newPoint,
                                        paddingY = paddingWindowMarker
                                    )
                                }
                                point.x < halfViewWidth -> {
                                    // start point

                                    val newPointX = halfViewWidth.toFloat() + paddingSide
                                    val newPointY = point.y
                                    val newPoint = PointF(newPointX, newPointY)

                                    markerView.moveJustOffset(
                                        point = newPoint,
                                        paddingY = paddingWindowMarker
                                    )
                                }
                                point.x > parentXSize -> {
                                    val newPointX = point.x - halfViewWidth.toFloat() - paddingSide
                                    val newPointY = point.y
                                    val newPoint = PointF(newPointX, newPointY)

                                    markerView.moveJustOffset(
                                        point = newPoint,
                                        paddingY = paddingWindowMarker
                                    )
                                }
                                point.x > (parentXSize - halfViewWidth) -> {
                                    // end point

                                    val newPointX = parentXSize - halfViewWidth.toFloat() - paddingSide
                                    val newPointY = point.y
                                    val newPoint = PointF(newPointX, newPointY)

                                    markerView.moveJustOffset(
                                        point = newPoint,
                                        paddingY = paddingWindowMarker
                                    )
                                }
                                else -> {
                                    markerView.moveJustOffset(
                                        point = point,
                                        paddingY = paddingWindowMarker
                                    )
                                }
                            }
                        }
                    }
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
                if (markView.windowViewConfig != null) {
                    parent.removeView(markView.windowViewConfig.view)
                }
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
                if (marker.windowViewConfig != null) {
                    parent.removeView(marker.windowViewConfig.view)
                }
                parent.removeView(marker.view)
            }
            markerViews.clear()
            return true
        }
        return false
    }

    fun addMarkerView(config: MarkerView.MarkerViewConfig.() -> Unit): MarkerView {
        if (this::googleMap.isInitialized) {
            val markerViewConfig = MarkerView.MarkerViewConfig().apply(config)
            return if (markerViewConfig.view == null && markerViewConfig.latLng == null) {
                when {
                    markerViewConfig.view == null -> {
                        throw GeolibException("view is null, don't forget to set your `view`")
                    }
                    markerViewConfig.latLng == null -> {
                        throw GeolibException("LatLng is null, don't forget to set your `latLng`")
                    }
                    else -> {
                        throw GeolibException("Something error! See documentation on https://utsmannn.github.io/geolib/docs/artifacts/marker-lib#any-view-marker")
                    }
                }
            } else {
                val windowViewConfig = MarkerView.WindowViewConfig()
                markerViewConfig.windowView?.invoke(windowViewConfig)

                MarkerView(
                    id = markerViewConfig.id,
                    view = markerViewConfig.view!!,
                    windowViewConfig = windowViewConfig,
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

                        val windowParam = ViewGroup.LayoutParams(
                            windowViewConfig.width ?: 150.dp,
                            windowViewConfig.height ?: 10.dp
                        )

                        view.isInvisible = true
                        parent.addView(view, param)

                        val windowView = windowViewConfig.view
                        if (windowView != null) {
                            windowView.isInvisible = true
                            parent.addView(windowView, windowParam)
                        }

                        MainScope().launch {
                            delay(70)
                            val point = googleMap.getCurrentPointF(position)
                            view.moveJust(point, markerViewConfig.anchorPoint)
                            windowView?.moveJustOffset(point, paddingWindowMarker)
                            view.isInvisible = false
                            windowView?.isInvisible = false
                        }
                    } else {
                        Log.e("MarkerView Error", "id: ${markerViewConfig.id} has been added")
                    }
                }
            }
        } else {
            throw GeolibException("Google maps is not initialized, don't forget to `bindGoogleMaps` into the adapter, see https://utsmannn.github.io/geolib/docs/artifacts/marker-lib#any-view-marker")
        }
    }
}
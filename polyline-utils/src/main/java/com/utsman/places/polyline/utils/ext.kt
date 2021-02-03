/*
 * Created on 1/2/21 10:16 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.polyline.utils

import android.graphics.Color
import android.util.Log
import androidx.core.graphics.ColorUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ktx.model.polylineOptions
import com.utsman.places.polyline.PolylineAnimatorBuilder
import com.utsman.places.polyline.data.PolylineConfig
import com.utsman.places.polyline.data.PolylineIdentifier
import com.utsman.places.polyline.point.PointPolyline
import com.utsman.places.polyline.point.PointPolylineImpl
import com.utsman.places.polyline.polyline.PolylineAnimator
import com.utsman.places.polyline.polyline.PolylineAnimatorOptions
import com.utsman.places.utils.GeolibException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun GoogleMap.createPolylineAnimatorBuilder(
    primaryColor: Int = Color.BLACK,
    accentColor: Int = primaryColor.transparentColor()
) = PolylineAnimatorBuilder(this, primaryColor, accentColor)

fun PolylineOptions.toPolylineOptions(points: List<LatLng>): PolylineOptions {
    val polylineOptions = this.copyPolylineOptions()
    polylineOptions.addAll(points)
    return polylineOptions
}

internal fun logd(msg: String) = Log.d("PLACES", msg)

internal const val trans35 = 0x59
internal const val trans20 = 0x33

internal fun Int.transparentColor(alpha: Int = trans35): Int {
    return ColorUtils.setAlphaComponent(this, alpha)
}

internal fun PolylineOptions.copyPolylineOptions(): PolylineOptions {
    return PolylineOptions().apply {
        color(this@copyPolylineOptions.color)
        width(this@copyPolylineOptions.width)
        startCap(this@copyPolylineOptions.startCap)
        endCap(this@copyPolylineOptions.endCap)
        clickable(this@copyPolylineOptions.isClickable)
        jointType(this@copyPolylineOptions.jointType)
        visible(this@copyPolylineOptions.isVisible)
        pattern(this@copyPolylineOptions.pattern)
    }
}

internal fun List<LatLng>.toGeoId(): String {
    return "${first()}-${last()}"
}

internal fun List<LatLng>.toGeoIdZIndex(zIndex: Float): String {
    return "${first()}-${last()}-${zIndex}"
}

internal fun PolylineIdentifier.toGeoIdZIndex() = "${this.geoId}-${this.zIndex}"

fun Polyline.withAnimate(
    polylineAnimator: PolylineAnimator,
    actionConfig: (PolylineConfig.() -> Unit)? = null
): Polyline {
    val googleMaps = polylineAnimator.getBindGoogleMaps()
    val config = polylineAnimator.getCurrentConfig()

    val primaryColor = if (config.primaryColor == Color.BLACK) color else config.primaryColor
    val accentColor =
        if (config.primaryColor == primaryColor.transparentColor()) config.accentColor else config.accentColor

    val options = PolylineAnimatorOptions(googleMaps, primaryColor, accentColor)
    options.startAnimate(points, actionConfig)
    return this.apply {
        points = emptyList()
    }
}

fun PolylineOptions.buildAnimationConfig(actionConfig: PolylineConfig.() -> Unit): PolylineConfig {
    return PolylineConfig(polylineOptions1 = this).apply(actionConfig)
}

fun GoogleMap.addPolyline(polylineConfig: PolylineConfig): PointPolyline {
    val primaryColor = polylineConfig.polylineOptions1?.color ?: Color.BLACK
    val accentColor = polylineConfig.polylineOptions2?.color ?: primaryColor.transparentColor()
    val polylineAnimator = createPolylineAnimatorBuilder(primaryColor, accentColor)
        .createPolylineAnimator()
    val points = polylineConfig.polylineOptions1?.points
    return if (!points.isNullOrEmpty()) {
        polylineAnimator.startAnimate(points)
    } else {
        throw GeolibException("Point must be added")
    }

}

fun Polyline.withAnimate(
    googleMap: GoogleMap,
    polylineOptions: PolylineOptions? = null,
    actionConfig: (PolylineConfig.() -> Unit)? = null
): Polyline {
    val config = if (actionConfig != null) {
        PolylineConfig().apply(actionConfig).apply {
            if (polylineOptions != null) {
                polylineOptions1 = polylineOptions.copyPolylineOptions()
            }
        }
    } else {
        PolylineConfig()
    }

    actionConfig?.invoke(config)
    val primaryColor = config.polylineOptions1?.color ?: Color.BLACK
    val accentColor = config.polylineOptions2?.color ?: primaryColor.transparentColor()
    val builder = PolylineAnimatorBuilder(googleMap, primaryColor, accentColor)
    val polylineAnimator = builder.createPolylineAnimator() as PolylineAnimatorOptions

    val pointPolyline: PointPolyline = PointPolylineImpl(polylineAnimator, config.stackAnimationMode)
    pointPolyline.addPoints(points, config)
    return this.apply {
        points = emptyList()
    }
}

fun PolylineConfig.withPrimaryPolyline(optionsActions: PolylineOptions.() -> Unit) {
    val options = polylineOptions(optionsActions)
    if (polylineOptions1 == null) polylineOptions1 = options
}

fun PolylineConfig.withAccentPolyline(optionsActions: PolylineOptions.() -> Unit) {
    val options = polylineOptions(optionsActions)
    if (polylineOptions2 == null) polylineOptions2 = options
}

fun PolylineConfig.doOnStartAnimation(action: (LatLng) -> Unit) {
    this.doOnStartAnim = action
}

fun PolylineConfig.doOnEndAnimation(action: (LatLng) -> Unit) {
    this.doOnEndAnim = action
}

fun PolylineConfig.doOnUpdateAnimation(action: (latLng: LatLng, mapCameraDuration: Int) -> Unit) {
    this.doOnUpdateAnim = action
}

fun PolylineConfig.enableBorder(
    isEnable: Boolean,
    color: Int = Color.BLACK.transparentColor(),
    width: Int = 2
) {
    this.enableBorder(isEnable, color, width)
}
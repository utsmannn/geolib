/*
 * Created on 4/10/21 1:11 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.marker

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.Window
import android.view.animation.LinearInterpolator
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


fun Location.toLatLng() = LatLng(latitude, longitude)

internal fun logd(message: String) = Log.d("SAMPLE", message)

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

internal fun rotateMarker(marker: Marker, toRotation: Float, rotate: Boolean? = true) {

    if (rotate != null && rotate) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val startRotation = marker.rotation
        val duration: Long = 300

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = LinearInterpolator().getInterpolation(elapsed.toFloat() / duration)

                val rot = t * toRotation + (1 - t) * startRotation

                marker.rotation = if (-rot > 180) rot / 2 else rot
                if (t < 1.0) {
                    handler.postDelayed(this, 16)
                }
            }
        })
    }
}

fun createBitmapFromView(view: View, width: Int, height: Int): Bitmap {
    if (width > 0 && height > 0) {
        view.measure(
            makeMeasureSpec(
                width, EXACTLY
            ),
            makeMeasureSpec(
                height, EXACTLY
            )
        )
    }
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)

    val bitmap = Bitmap.createBitmap(
        view.measuredWidth,
        view.measuredHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    val background = view.background

    background?.draw(canvas)
    view.draw(canvas)

    return bitmap
}

internal suspend fun createBitmapMarkerFromLayout(viewAdapter: MarkerViewAdapter): Bitmap {
    val bitmap = createBitmapFromView(viewAdapter.createView(), 200.dp, 200.dp)
    val maxWidth = viewAdapter.maxWidth()
    val maxHeight = viewAdapter.maxHeight()
    return scale(bitmap, maxWidth, maxHeight)
}

private fun scale(bm: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    var bmp = bm
    var width = bmp.width
    var height = bmp.height
    when {
        width > height -> {
            val ratio = width.toFloat() / maxWidth
            width = maxWidth
            height = (height / ratio).toInt()
        }
        height > width -> {
            val ratio = height.toFloat() / maxHeight
            height = maxHeight
            width = (width / ratio).toInt()
        }
        else -> {
            height = maxHeight
            width = maxWidth
        }
    }
    bmp = Bitmap.createScaledBitmap(bm, width, height, true)
    return bmp
}

internal fun bitmapFromVector(context: Context, @DrawableRes icon: Int): BitmapDescriptor {
    val background = ContextCompat.getDrawable(context, icon)
    background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)

    val bitmap = Bitmap.createBitmap(
        background.intrinsicWidth,
        background.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)

    background.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

internal fun moveMarkerSmoothly(marker: Marker, newLatLng: LatLng) : ValueAnimator {
    val animator = ValueAnimator.ofFloat(0f, 100f)

    val deltaLatitude = doubleArrayOf(newLatLng.latitude - marker.position.latitude)
    val deltaLongitude = newLatLng.longitude - marker.position.longitude
    val prevStep = floatArrayOf(0f)
    animator.duration = 1500

    animator.addUpdateListener { animation ->
        val deltaStep = (animation.animatedValue as Float - prevStep[0]).toDouble()
        prevStep[0] = animation.animatedValue as Float
        val latLng = LatLng(
            marker.position.latitude + deltaLatitude[0] * deltaStep * 1.0 / 100,
            marker.position.longitude + deltaStep * deltaLongitude * 1.0 / 100
        )
        marker.position = latLng
    }

    return animator
}

private fun computeHeading(from: LatLng, to: LatLng): Double {
    val fromLat = Math.toRadians(from.latitude)
    val fromLng = Math.toRadians(from.longitude)
    val toLat = Math.toRadians(to.latitude)
    val toLng = Math.toRadians(to.longitude)
    val dLng = toLng - fromLng
    val heading = atan2(
        sin(dLng) * cos(toLat),
        cos(fromLat) * sin(toLat) - sin(fromLat) * cos(toLat) * cos(
            dLng
        )
    )
    return wrap(Math.toDegrees(heading))
}

private fun wrap(n: Double): Double {
    val min = -180.0
    val max = 180.0
    return if (n >= min && n < max) n else mod(n - -180.0, max - min) + min
}

private fun mod(x: Double, m: Double): Double {
    return (x % m + m) % m
}

internal fun getAngle(fromLatLon: LatLng, toLatLon: LatLng): Double {
    var heading = 0.0
    if (fromLatLon !== toLatLon) {
        heading = computeHeading(fromLatLon, toLatLon)
    }
    return heading
}
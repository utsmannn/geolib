/*
 * Created on 2/2/21 7:44 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.polyline.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil

internal object CalculationHelper {

    fun polylineUntilSection(
        path: List<LatLng>,
        legs: List<Double>,
        pathSection: Double,
        polylineOptions: PolylineOptions,
        currentLatLng: (LatLng) -> Unit
    ): PolylineOptions {
        var legSum = 0.0
        var pastSections = 0.0

        for ((index, value) in path.withIndex()) {
            polylineOptions.add(value)

            if (path.size > 2 && index < path.size - 1 && legs.size > 1) {
                val to = path[index + 1]
                val currentLeg = legs[index]
                legSum += currentLeg
                if (pathSection < legSum) {
                    val fraction = (pathSection - pastSections) / currentLeg
                    val pointToBeAdded = SphericalUtil.interpolate(value, to, fraction)
                    currentLatLng.invoke(pointToBeAdded)
                    polylineOptions.add(pointToBeAdded)
                    return polylineOptions
                } else {
                    pastSections += currentLeg
                }
            }
        }
        polylineOptions.add(path.last())
        return polylineOptions
    }

    fun calculateLegsLengths(path: List<LatLng>): List<Double> {
        val legs = mutableListOf<Double>()
        for ((index, value) in path.withIndex()) {
            if (index < path.size - 1) {
                val to = path[index + 1]
                val legLength = SphericalUtil.computeDistanceBetween(value, to)
                legs.add(legLength)
            }
        }
        return legs
    }

    private fun step(closed: ClosedRange<Double>, step: Double): Iterable<Double> {
        val sequence = generateSequence(closed.start) { previous ->
            if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
            val next = previous + step
            if (next > closed.endInclusive) null else next
        }
        return sequence.asIterable()
    }

    fun geometriesCurved(geometries: List<LatLng>): List<LatLng> {
        return if (geometries.size > 2) {
            val first = geometries.first()
            val last = geometries.last()
            val points = geometries.size

            val distances = SphericalUtil.computeDistanceBetween(first, last)
            val heading = SphericalUtil.computeHeading(first, last)

            val radius = if (heading < 0) {
                3.0
            } else {
                0.3
            }

            val midPoint = SphericalUtil.computeOffset(first, distances * 0.5, heading)

            //Apply mathematics to calculate position of the circle center
            val x = (1 - radius * radius) * distances * 0.5 / (2 * radius)
            val r = (1 + radius * radius) * distances * 0.5 / (2 * radius)
            val c = SphericalUtil.computeOffset(midPoint, x, heading + 90.0)

            val h1 = SphericalUtil.computeHeading(c, first)
            val h2 = SphericalUtil.computeHeading(c, last)

            val step = (h2 - h1) / points
            (0 until points).toList().mapIndexed { index, _ ->
                SphericalUtil.computeOffset(c, r, h1 + index * step)
            }
        } else {
            val completeStep = geometriesLank(geometries)
            geometriesCurved(completeStep)
        }
    }

    fun geometriesLank(geometries: List<LatLng>): List<LatLng> {
        return if (geometries.size > 2) {
            val first = geometries.first()
            val last = geometries.last()
            val points = geometries.size

            val distances = SphericalUtil.computeDistanceBetween(first, last)
            val heading = SphericalUtil.computeHeading(first, last)

            val radius = 0.01

            val midPoint = SphericalUtil.computeOffset(first, distances * 0.5, heading)
            val x = (1 - radius * radius) * distances * 0.5 / (2 * radius)
            val r = (1 + radius * radius) * distances * 0.5 / (2 * radius)
            val c = SphericalUtil.computeOffset(midPoint, x, heading + 90.0)

            val h1 = SphericalUtil.computeHeading(c, first)
            val h2 = SphericalUtil.computeHeading(c, last)

            val step = (h2 - h1) / points
            (0 until points/100).toList().mapIndexed { index, _ ->
                SphericalUtil.computeOffset(c, r, h1 + index * step)
            }
        } else {
            val heading = SphericalUtil.computeHeading(geometries.first(), geometries.last())
            val distance = SphericalUtil.computeDistanceBetween(geometries.first(), geometries.last())
            val range = 0.0.rangeTo(distance)
            val completableStep = (geometries.size * 0.10) * 0.1
            val iterator = step(range, (distance/geometries.size) * completableStep)
            iterator.map {
                SphericalUtil.computeOffset(geometries.first(), it, heading)
            }
        }
    }
}
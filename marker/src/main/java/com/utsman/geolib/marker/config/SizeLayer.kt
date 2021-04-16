/*
 * Created on 4/13/21 1:48 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.marker.config

sealed class SizeLayer {
    object Marker : SizeLayer()
    object Normal : SizeLayer()
    data class Custom(val width: Int, val height: Int): SizeLayer()
}
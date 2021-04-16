/*
 * Created on 3/2/21 12:58 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.polyline.data

sealed class PolylineDrawMode {
    object Normal : PolylineDrawMode()
    object Curved : PolylineDrawMode()
    object Lank : PolylineDrawMode()
}
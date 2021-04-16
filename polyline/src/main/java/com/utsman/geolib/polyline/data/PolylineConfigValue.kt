/*
 * Created on 2/2/21 7:44 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.polyline.data

data class PolylineConfigValue(
    val primaryColor: Int,
    val accentColor: Int,
    val stackAnimationMode: StackAnimationMode,
    val cameraAutoUpdate: Boolean
)
/*
 * Created on 4/10/21 1:16 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.location.data

import android.location.Location

data class ComparisonLocation(
    val previousLocation: Location?,
    val currentLocation: Location
)
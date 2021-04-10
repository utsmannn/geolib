/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.location

import android.location.Location
import com.utsman.places.location.data.ComparisonLocation
import com.utsman.places.location.data.PlaceData
import kotlinx.coroutines.flow.Flow

interface PlacesLocation {
    suspend fun getLocationFlow(): Flow<Location>
    suspend fun getPlacesLocation(location: Location): List<PlaceData>
    suspend fun getComparisonLocation(): Flow<ComparisonLocation>
    suspend fun searchPlaces(location: Location, query: String): List<PlaceData>
    var enableLog: Boolean
}
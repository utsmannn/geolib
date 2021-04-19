/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.utsman.geolib.location.data.ComparisonLocation
import com.utsman.geolib.location.data.ConstantValues
import com.utsman.geolib.location.data.Mapper
import com.utsman.geolib.location.data.PlaceData
import com.utsman.geolib.location.network.HereService
import com.utsman.geolib.core.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

internal class PlacesLocationImpl(
    private val fusedProviderClient: FusedLocationProviderClient,
    private val hereMapsApi: String,
    override var enableLog: Boolean = true
) : PlacesLocation {

    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 1000
    }

    private val distanceValidator = 30f

    private fun provideService(): HereService {
        return Network.retrofit(ConstantValues.BASE_URL, enableLog)
            .create(HereService::class.java)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLocationFlow(): Flow<Location> {
        val callbackFlow: Flow<Location> = callbackFlow {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult?) {
                    if (result != null) {
                        for (location in result.locations) {
                            offer(location)
                        }
                    }
                }
            }

            fusedProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            ).addOnCanceledListener {
                cancel("Cancel request", Throwable("Cancel request by user"))
            }.addOnFailureListener {
                cancel("Failure request location", it)
            }

            awaitClose { fusedProviderClient.removeLocationUpdates(locationCallback) }
        }

        return callbackFlow.distinctUntilChanged { old, new ->
            old.distanceTo(new) < distanceValidator
        }
    }

    override suspend fun getPlacesLocation(location: Location): Result<List<PlaceData>> {
        val locationString = location.toStringService()
        val errorMessage = "Get place failure!"

        return fetch(errorMessage) {
            val response = provideService().getPlaceLocation(locationString, hereMapsApi)
            if (response.errorDescription != null) {
                throw GeolibException(response.errorDescription)
            }
            response
        }.mapCatching {
            it.items?.map { d -> Mapper.mapToPlaceData(d) } ?: emptyList()
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getComparisonLocation(): Flow<ComparisonLocation> {
        var oldLocation: Location? = null
        val callbackFlow: Flow<ComparisonLocation> = callbackFlow {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult?) {
                    if (result != null) {
                        for (location in result.locations) {
                            offer(ComparisonLocation(oldLocation, location))
                        }
                    }
                }
            }

            fusedProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            ).addOnCanceledListener {
                cancel("Cancel request", Throwable("Cancel request by user"))
            }.addOnFailureListener {
                cancel("Failure request location", it)
            }

            awaitClose { fusedProviderClient.removeLocationUpdates(locationCallback) }
        }

        return callbackFlow.distinctUntilChanged { old, new ->
            oldLocation = old.currentLocation
            old.currentLocation.distanceTo(new.currentLocation) < 5f
        }
    }

    override suspend fun searchPlaces(location: Location, query: String): Result<List<PlaceData>> {
        val locationString = location.toStringService()
        val errorMessage = "Search place failure!"
        return fetch(errorMessage) {
            provideService().searchPlace(locationString, query, hereMapsApi)
        }.mapCatching {
            it.items?.map { d -> Mapper.mapToPlaceData(d) } ?: emptyList()
        }
    }
}
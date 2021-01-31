/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.utsman.places.location.data.ConstantValues
import com.utsman.places.location.data.Mapper
import com.utsman.places.location.data.PlaceData
import com.utsman.places.location.network.HereService
import com.utsman.places.utils.Network
import com.utsman.places.utils.fetch
import com.utsman.places.utils.toStringService
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

    override suspend fun getPlacesLocation(location: Location): List<PlaceData> {
        val locationString = location.toStringService()
        val data = fetch {
            provideService().getPlaceLocation(locationString, hereMapsApi)
        }
        return data?.items?.map {
            Mapper.mapToPlaceData(it)
        } ?: emptyList()
    }

    override suspend fun searchPlaces(location: Location, query: String): List<PlaceData> {
        val locationString = location.toStringService()
        val data = fetch {
            provideService().searchPlace(locationString, query, hereMapsApi)
        }
        return data?.items?.map {
            Mapper.mapToPlaceData(it)
        } ?: emptyList()
    }
}
/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.data

sealed class TransportMode {
    object CAR : TransportMode()
    object BIKE : TransportMode()

    fun getString() = when (this) {
        is CAR -> "car"
        is BIKE -> "scooter"
    }
}
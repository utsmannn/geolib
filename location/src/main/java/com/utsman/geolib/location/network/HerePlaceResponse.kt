/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.location.network

import android.location.Location

internal data class HerePlaceResponse(
    val items: List<Item>?,
    val errorDescription: String?
) {

    data class Item(
        val id: String? = null,
        val title: String? = null,
        val address: Address? = null,
        val position: Position? = null,
        val distance: Double? = null,
        val categories: List<Category>? = emptyList()
    )

    data class Address(
        val label: String? = null,
        val street: String? = null,
        val district: String? = null,
        val subdistrict: String? = null,
        val city: String? = null,
        val country: String? = null
    )

    data class Position(
        val lat: Double = 0.0,
        val lng: Double = 0.0
    ) {
        fun toLocation(): Location {
            return Location("").apply {
                latitude = lat
                longitude = lng
            }
        }
    }

    data class Category(
        val name: String
    )
}

/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.location.data

import android.location.Location
import com.utsman.places.location.network.HerePlaceResponse

internal object Mapper {
    fun mapToPlaceData(item: HerePlaceResponse.Item): PlaceData {
        return item.run {
            val title = title
            val addressLabel = address?.label?.removePrefix("$title, ")
            PlaceData(
                hereId = id ?: "",
                title = title ?: "Unknown place",
                address = addressLabel ?: "Unknown address",
                district = address?.district ?: "Unknown district",
                city = address?.city ?: "Unknown city",
                location = position?.toLocation() ?: Location(""),
                distance = distance ?: 0.0,
                category = categories?.firstOrNull()?.name
            )
        }
    }
}
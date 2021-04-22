/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.location.data

import android.location.Location
import com.utsman.geolib.location.kM
import com.utsman.geolib.location.network.HerePlaceResponse
import java.util.*

internal object Mapper {
    fun mapToPlaceData(item: HerePlaceResponse.Item): PlaceData {
        return item.run {
            val titleIfAddress = title?.lower()?.contains(address?.street?.lower().toString()) == true

            val fixTitleIfAddress = if (address?.houseNumber != null) {
                "${address.street}, No. ${address.houseNumber}, ${address.subdistrict}"
            } else {
                "${address?.street}, ${address?.subdistrict}"
            }

            val fixAddress = if (address?.houseNumber != null) {
                "${address.street}, No. ${address.houseNumber}, ${address.subdistrict}, ${address.district}, ${address.city}"
            } else {
                "${address?.street}, ${address?.subdistrict}, ${address?.district}, ${address?.city}"
            }

            val title = if (titleIfAddress) {
                fixTitleIfAddress
            } else {
                title
            }

            val distanceInKm = distance?.kM
            PlaceData(
                hereId = id ?: "",
                title = title ?: "Unknown place",
                address = fixAddress,
                district = address?.district ?: "Unknown district",
                city = address?.city ?: "Unknown city",
                location = position?.toLocation() ?: Location(""),
                distance = distance ?: 0.0,
                distanceInKm = distanceInKm,
                houseNumber = address?.houseNumber,
                category = categories?.firstOrNull()?.name
            )
        }
    }

    private fun String.lower(): String {
        return this.toLowerCase(Locale.getDefault())
    }
}
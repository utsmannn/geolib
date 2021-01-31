/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.utsman.places.routes.decoder.PolylineEncoderDecoder

internal object Mapper {

    fun mapPolylineAlgorithm(encoded: String?): String? {
        return if (encoded != null) {
            val geometry = PolylineEncoderDecoder.decode(encoded)
                .map {
                    LatLng(it.lat, it.lng)
                }

            PolyUtil.encode(geometry)
        } else {
            null
        }
    }

    fun getGoogleGeometries(encoded: String): List<LatLng> {
        return PolyUtil.decode(encoded)
    }

}
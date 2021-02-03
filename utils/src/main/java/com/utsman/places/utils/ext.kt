/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.utils

import android.location.Location

fun Location.toStringService() = "${latitude},${longitude}"

suspend fun <T>fetch(call: suspend () -> T): T? {
    return try {
        call.invoke()
    } catch (e: Throwable) {
        GeolibException(e.message)
            .printStackTrace()
        null
    }
}
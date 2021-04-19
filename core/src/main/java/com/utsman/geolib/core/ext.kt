/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.core

import android.location.Location

fun Location.toStringService() = "${latitude},${longitude}"

suspend fun <T>fetch(errorMessage: String, call: suspend () -> T): Result<T> {
    return try {
        Result.success(call.invoke())
    } catch (e: Throwable) {
        val errMsg = "$errorMessage, details: ${e.message}"
        val exception = GeolibException(errMsg)
        Result.failure(exception)
    }
}
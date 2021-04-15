/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.utils

import android.location.Location

fun Location.toStringService() = "${latitude},${longitude}"

suspend fun <T>fetch(errorMessage: String, call: suspend () -> T): ResultState<T> {
    return try {
        ResultState.Success(call.invoke())
    } catch (e: Throwable) {
        val errMsg = "$errorMessage, details: ${e.message}"
        val exception = GeolibException(errMsg)
        ResultState.Failure(exception)
    }
}

fun <T, U>ResultState<T>.mapData(map: (T) -> U): ResultState<U> {
    return when (this) {
        is ResultState.Success -> {
            val data = map.invoke(data)
            ResultState.Success(data)
        }
        is ResultState.Failure -> ResultState.Failure(throwable)
    }
}

fun <T>ResultState<T>.doOnSuccess(success: (T) -> Unit): ResultState<T> {
    return when (this) {
        is ResultState.Success -> {
            val data = this.data
            success.invoke(data)
            this
        }
        is ResultState.Failure -> this
    }
}

fun <T>ResultState<T>.doOnFailure(failure: (GeolibException) -> Unit): ResultState<T> {
    return when (this) {
        is ResultState.Success -> this
        is ResultState.Failure -> {
            failure.invoke(GeolibException(throwable.localizedMessage))
            this
        }
    }
}

fun <T>ResultState<T>.mapToSuccess(): T? {
    return when (this) {
        is ResultState.Success -> data
        is ResultState.Failure -> null
    }
}

fun <T>ResultState<T>.mapToException(): GeolibException? {
    return when (this) {
        is ResultState.Success -> null
        is ResultState.Failure -> GeolibException(throwable.message)
    }
}
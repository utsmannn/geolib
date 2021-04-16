/*
 * Created on 4/15/21 1:09 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.core

sealed class ResultState<T> {
    data class Success<T>(val data: T): ResultState<T>()
    data class Failure<T>(val throwable: Throwable): ResultState<T>()
}

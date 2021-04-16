/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.core

class GeolibException(override val message: String? = "Fused provider client failed, check documentation!") :
    Throwable(message)
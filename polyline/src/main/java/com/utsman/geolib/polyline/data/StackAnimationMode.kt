/*
 * Created on 1/2/21 9:39 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.geolib.polyline.data

sealed class StackAnimationMode {
    object WaitStackEndAnimation : StackAnimationMode()
    object BlockStackAnimation : StackAnimationMode()
    object OffStackAnimation : StackAnimationMode()
}
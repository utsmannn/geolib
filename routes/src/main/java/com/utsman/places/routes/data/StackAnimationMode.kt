/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.routes.data

sealed class StackAnimationMode {
    object WaitStackEndAnimation : StackAnimationMode()
    object BlockStackAnimation : StackAnimationMode()
    object OffStackAnimation : StackAnimationMode()
}
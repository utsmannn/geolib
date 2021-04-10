/*
 * Created on 4/10/21 2:06 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.utsman.places.marker.MarkerViewAdapter
import com.utsman.places.marker.dp
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import kotlin.coroutines.resume

class CatMarkerAdapter(private val context: Context) : MarkerViewAdapter() {
    override suspend fun createView(): View {
        return LayoutInflater.from(context).inflate(R.layout.marker_cat, null)
    }

    override fun maxWidth(): Int {
        return 50.dp
    }

    override fun maxHeight(): Int {
        return 70.dp
    }
}

class CustomMarkerAdapter(private val context: Context) : MarkerViewAdapter() {
    private val catUrl = "https://asset.kompas.com/crops/AOqycoSV_pH5eU51rYStWW_zVFY=/1x0:1000x666/750x500/data/photo/2019/11/04/5dbfff829ebe6.jpg"
    override suspend fun createView(): View = suspendCancellableCoroutine { task ->
        val view = LayoutInflater.from(context).inflate(R.layout.marker_custom, null)
        val ivCat = view.findViewById<ImageView>(R.id.iv_cat)
        Picasso.get()
            .load(catUrl)
            .transform(CropCircleTransformation())
            .into(ivCat, object : Callback {
                override fun onSuccess() {
                    MainScope().launch {
                        delay(100)
                        task.resume(view)
                    }
                }

                override fun onError(e: Exception?) {
                    task.resume(view)
                }

            })

        //delay(2000)
    }

    override fun maxWidth(): Int {
        return 70.dp
    }

    override fun maxHeight(): Int {
        return 70.dp
    }
}

class LottieMarkerAdapter(private val context: Context) : MarkerViewAdapter() {
    override suspend fun createView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.marker_lottie, null)
        val lottieView = view.findViewById<LottieAnimationView>(R.id.lottie_view)
        lottieView.addAnimatorUpdateListener { animator ->
            logd("animator --> ${animator.animatedValue}")
        }
        lottieView.playAnimation()
        delay(1000)
        logd("view laoaded...")
        return view
    }

    override fun maxWidth(): Int {
        return 50.dp
    }

    override fun maxHeight(): Int {
        return 70.dp
    }
}
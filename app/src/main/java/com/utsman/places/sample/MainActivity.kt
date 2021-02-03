/*
 * Created on 31/1/21 5:51 PM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    findViewById<Button>(R.id.btn_current_location).setOnClickListener {
                        this@MainActivity intent CurrentLocationActivity::class.java
                    }

                    findViewById<Button>(R.id.btn_search_location).setOnClickListener {
                        this@MainActivity intent SearchActivity::class.java
                    }

                    findViewById<Button>(R.id.btn_route).setOnClickListener {
                        this@MainActivity intent RouteActivity::class.java
                    }

                    findViewById<Button>(R.id.btn_polyline).setOnClickListener {
                        this@MainActivity intent PolylineActivity::class.java
                    }

                    findViewById<Button>(R.id.btn_draw_polyline).setOnClickListener {
                        this@MainActivity intent DrawModePolyline::class.java
                    }
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                }

            })
            .check()
    }
}
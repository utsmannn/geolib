/*
 * Created on 4/10/21 2:06 AM
 * Copyright (c) Muhammad Utsman 2021 All rights reserved.
 */

package com.utsman.places.sample

import android.content.Context
import android.view.View
import android.widget.TextView
import com.utsman.places.marker.MarkerViewAdapter

class DrawMarkerAdapter(context: Context) : MarkerViewAdapter(context, R.layout.marker_cat) {

    override fun onViewCreated(view: View) {
        view.run {
            val tvTitle = findViewById<TextView>(R.id.txt_title)
            tvTitle.text = "okeeee.."
        }
    }
}
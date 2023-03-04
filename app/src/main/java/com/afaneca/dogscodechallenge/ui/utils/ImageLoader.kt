package com.afaneca.dogscodechallenge.ui.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object ImageLoader {


    /**
     * Loads image from [url] using external library and then inflates it into the a [view]
     */
    fun loadImageIntoView(context: Context, url: String, view: ImageView) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }
}
package com.example.githubapitask.utils

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.githubapitask.R

object DataBindingAdapter {

    @JvmStatic
    @BindingAdapter("image_url")
    fun bindImageUrl(
        view: ImageView, url: String?
    ) {
        //Log.d("image_url", "bind->$url")
        val options = RequestOptions
            .centerCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(R.drawable.ic_launcher_background)

        Glide.with(view.context)
            .asBitmap()
            .load(url)
            .apply(options)
            .into(view)
    }
}
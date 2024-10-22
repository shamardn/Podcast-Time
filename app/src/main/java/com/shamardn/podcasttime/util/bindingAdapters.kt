package com.shamardn.podcasttime.util

import android.net.Uri
import android.widget.ImageView
import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.shamardn.podcasttime.R

@BindingAdapter("app:setMaxProgressForSeekBar")
fun setMaxProgressForSeekBar(seekBar: SeekBar, maxProgress: Long) {
    seekBar.max = maxProgress.toInt()
}

@BindingAdapter("app:setCurrentProgressForSeekBar")
fun setCurrentProgressForSeekBar(seekBar: SeekBar, progress: Long) {
    seekBar.progress = progress.toInt()
}

@BindingAdapter("app:loadImageResource")
fun loadImageResource(imageView: ShapeableImageView, source: String?) {
    if (source != null) {
        Glide.with(imageView.context)
            .load(Uri.parse(source))
            .into(imageView)
    } else {
        imageView.setImageResource(R.drawable.podcast_icon)
    }
}

@BindingAdapter("app:setImagePlayOrStop")
fun setImagePlayOrStop(imageView: ImageView, isPlay: Boolean) {
    if (isPlay) {
        imageView.setImageResource(R.drawable.ic_pause)
    } else {
        imageView.setImageResource(R.drawable.ic_play)
    }
}

@BindingAdapter("app:setImageIsLoading")
fun setImageIsLoading(imageView: ImageView, isByffering :Boolean) {
    if (isByffering){
        imageView.setImageResource(R.drawable.ic_downloads)
    }
}
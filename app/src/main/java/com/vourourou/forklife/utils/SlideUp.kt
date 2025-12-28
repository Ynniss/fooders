package com.vourourou.forklife.utils

import android.view.View
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.vourourou.forklife.R

fun View.slideUp(animationDuration: Long, startOffset: Long) {
    val slideAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up).apply {
        duration = animationDuration
        interpolator = FastOutSlowInInterpolator()
        this.startOffset = startOffset
    }

    startAnimation(slideAnimation)
}
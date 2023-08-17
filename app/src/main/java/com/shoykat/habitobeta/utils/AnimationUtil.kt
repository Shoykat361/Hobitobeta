package com.shoykat.habitobeta.utils

import kotlin.math.pow

class AnimationUtil {
    companion object {
        fun easeOutCubic(value : Float) : Float {
            return 1f - (1f - value).pow(3f)
        }
    }
}
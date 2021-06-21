package com.fphoenixcorneae.text.style

import android.graphics.Color
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance
import androidx.annotation.ColorInt
import androidx.annotation.IntRange

/**
 * @desc 可变前景颜色 Span
 * @date 2020-09-26:17:55
 */
class MutableForegroundColorSpan(
    @ColorInt var mColor: Int = Color.RED,
    @IntRange(from = 0, to = 255) var mAlpha: Int = 0
) : CharacterStyle(), UpdateAppearance {

    override fun updateDrawState(tp: TextPaint) {
        tp.color = mColor
        tp.alpha = mAlpha
    }
}
package com.fphoenixcorneae.text.style

import android.graphics.*
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance

/**
 * @desc 彩虹样的Span，主要是用到了Paint的Shader技术
 * @date 2020-09-26 17:39
 */
class RainbowSpan(vararg colors: Int) : CharacterStyle(), UpdateAppearance {
    private var mColors = intArrayOf(
        Color.parseColor("#8bc34a"),
        Color.parseColor("#ffc107"),
        Color.parseColor("#e84e40"),
        Color.parseColor("#5677fc")
    )

    override fun updateDrawState(paint: TextPaint) {
        paint.style = Paint.Style.FILL
        val shader: Shader = LinearGradient(
            0f, 0f, 0f, paint.textSize * mColors.size, mColors, null,
            Shader.TileMode.MIRROR
        )
        val matrix = Matrix()
        matrix.setRotate(90f)
        shader.setLocalMatrix(matrix)
        paint.shader = shader
    }

    init {
        if (colors.isNotEmpty() && colors.size >= 2) {
            mColors = colors
        }
    }
}
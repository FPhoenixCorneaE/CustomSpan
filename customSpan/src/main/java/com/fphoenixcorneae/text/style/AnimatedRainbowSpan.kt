package com.fphoenixcorneae.text.style

import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.format.DateUtils
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance
import android.util.Property
import android.view.animation.LinearInterpolator

/**
 * @desc AnimatedRainbowSpan实现一个动画的彩虹样式,实现思路：通过ObjectAnimator动画调整RainbowSpan中矩阵的平移，从而实现动画彩虹的效果。
 * @date 2020-09-27 12:40
 */
class AnimatedRainbowSpan(
    text: String,
    colors: IntArray,
    start: Int,
    end: Int,
    private val onAnimationUpdate: ((SpannableString) -> Unit)?
) : CharacterStyle(), UpdateAppearance {

    private var mColors = intArrayOf(
        Color.parseColor("#8bc34a"),
        Color.parseColor("#ffc107"),
        Color.parseColor("#e84e40"),
        Color.parseColor("#5677fc")
    )
    private var shader: Shader? = null
    private val matrix = Matrix()
    var translateXPercentage = 0f

    override fun updateDrawState(paint: TextPaint) {
        paint.style = Paint.Style.FILL
        val width = paint.textSize * mColors.size
        if (shader == null) {
            shader = LinearGradient(
                0f, 0f, 0f, width, mColors, null,
                Shader.TileMode.MIRROR
            )
        }
        matrix.reset()
        matrix.setRotate(90f)
        matrix.postTranslate(width * translateXPercentage, 0f)
        shader!!.setLocalMatrix(matrix)
        paint.shader = shader
    }

    private val animatedRainbowSpanFloatProperty: Property<AnimatedRainbowSpan, Float> =
        object : Property<AnimatedRainbowSpan, Float>(
            Float::class.java, "animatedRainbowSpanFloatProperty"
        ) {
            override operator fun set(span: AnimatedRainbowSpan, value: Float) {
                span.translateXPercentage = value
            }

            override operator fun get(span: AnimatedRainbowSpan): Float {
                return span.translateXPercentage
            }
        }

    init {
        if (colors.size >= 2) {
            mColors = colors
        }

        val spannableString = SpannableString(text).apply {
            setSpan(this@AnimatedRainbowSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }

        ObjectAnimator.ofFloat(
            this, animatedRainbowSpanFloatProperty, 0f, 100f
        ).apply {
            setEvaluator(FloatEvaluator())
            addUpdateListener { onAnimationUpdate?.invoke(spannableString) }
            interpolator = LinearInterpolator()
            duration = DateUtils.MINUTE_IN_MILLIS * 3
            repeatCount = ValueAnimator.INFINITE
            start()
        }
    }
}
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
 * @desc AnimatedRainbowSpan 实现一个动画的彩虹样式,实现思路：通过 ObjectAnimator 动画调整 RainbowSpan 中矩阵的平移，从而实现动画彩虹的效果。
 * @date 2020-09-27 12:40
 */
class AnimatedRainbowSpan(
    text: String,
    colors: IntArray,
    start: Int,
    end: Int,
    private val mDuration: Long = DateUtils.MINUTE_IN_MILLIS * 3,
    private val onAnimationUpdate: ((SpannableString) -> Unit)?,
) : CharacterStyle(), UpdateAppearance {

    private var mColors = intArrayOf(
        Color.parseColor("#8bc34a"),
        Color.parseColor("#ffc107"),
        Color.parseColor("#e84e40"),
        Color.parseColor("#5677fc")
    )
    private var mShader: Shader? = null
    private val mMatrix = Matrix()
    private var mTranslateXPercentage = 0f
    private var mAnimator: ObjectAnimator

    private val mAnimatedRainbowSpanFloatProperty: Property<AnimatedRainbowSpan, Float> =
        object : Property<AnimatedRainbowSpan, Float>(
            Float::class.java, "mAnimatedRainbowSpanFloatProperty"
        ) {
            override operator fun set(span: AnimatedRainbowSpan, value: Float) {
                span.mTranslateXPercentage = value
            }

            override operator fun get(span: AnimatedRainbowSpan): Float {
                return span.mTranslateXPercentage
            }
        }

    override fun updateDrawState(paint: TextPaint) {
        paint.style = Paint.Style.FILL
        val width = paint.textSize * mColors.size
        if (mShader == null) {
            mShader = LinearGradient(
                0f, 0f, 0f, width, mColors, null,
                Shader.TileMode.MIRROR
            )
        }
        mMatrix.reset()
        mMatrix.setRotate(90f)
        mMatrix.postTranslate(width * mTranslateXPercentage, 0f)
        mShader?.setLocalMatrix(mMatrix)
        paint.shader = mShader
    }

    fun cancelAnimation() {
        mAnimator.cancel()
    }

    fun pauseAnimation() {
        if (mAnimator.isRunning && mAnimator.isPaused.not()) {
            mAnimator.pause()
        }
    }

    fun resumeAnimation() {
        if (mAnimator.isStarted && mAnimator.isPaused) {
            mAnimator.resume()
        }
    }

    fun toggleAnimation() {
        if (mAnimator.isRunning && mAnimator.isPaused.not()) {
            mAnimator.pause()
        } else if (mAnimator.isStarted && mAnimator.isPaused) {
            mAnimator.resume()
        } else if (mAnimator.isStarted.not() && mAnimator.isRunning.not()) {
            mAnimator.start()
        }
    }

    init {
        if (colors.size >= 2) {
            mColors = colors
        }

        val spannableString = SpannableString(text).apply {
            setSpan(this@AnimatedRainbowSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }

        mAnimator = ObjectAnimator.ofFloat(
            this, mAnimatedRainbowSpanFloatProperty, 0f, 100f
        ).apply {
            setEvaluator(FloatEvaluator())
            addUpdateListener { onAnimationUpdate?.invoke(spannableString) }
            interpolator = LinearInterpolator()
            duration = mDuration
            repeatCount = ValueAnimator.INFINITE
            start()
        }
    }
}
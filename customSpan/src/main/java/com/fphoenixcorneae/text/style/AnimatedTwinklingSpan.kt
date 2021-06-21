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
 * @desc AnimatedTwinklingSpan 实现一个文字闪烁的样式, 实现思路：通过 ObjectAnimator 动画调整 Span 中矩阵的平移，从而实现闪烁的效果。
 * @date 2020-09-27 12:40
 */
class AnimatedTwinklingSpan(
    private val text: String,
    colors: IntArray,
    private val start: Int,
    private val end: Int,
    private val mDuration: Long = DateUtils.SECOND_IN_MILLIS * 3,
    private val onAnimationUpdate: ((SpannableString) -> Unit)?,
) : CharacterStyle(), UpdateAppearance {

    private var mColors = intArrayOf(
        Color.BLACK,
        Color.WHITE,
        Color.BLACK
    )
    private var mShader: Shader? = null
    private val mMatrix = Matrix()
    private var mTranslateXPercentage = 0f
    private var mAnimator: ObjectAnimator

    private val mAnimatedTwinklingSpanFloatProperty: Property<AnimatedTwinklingSpan, Float> =
        object : Property<AnimatedTwinklingSpan, Float>(
            Float::class.java, "mAnimatedTwinklingSpanFloatProperty"
        ) {
            override operator fun set(span: AnimatedTwinklingSpan, value: Float) {
                span.mTranslateXPercentage = value
            }

            override operator fun get(span: AnimatedTwinklingSpan): Float {
                return span.mTranslateXPercentage
            }
        }

    override fun updateDrawState(paint: TextPaint) {
        paint.style = Paint.Style.FILL
        val width = paint.measureText(text, start, end)
        if (mShader == null) {
            mShader = LinearGradient(
                0f, 0f, width, 0f,
                mColors,
                null,
                Shader.TileMode.CLAMP
            )
        }
        mMatrix.reset()
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
            setSpan(this@AnimatedTwinklingSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }

        mAnimator = ObjectAnimator.ofFloat(
            this, mAnimatedTwinklingSpanFloatProperty, -1f, 2f
        ).apply {
            setEvaluator(FloatEvaluator())
            addUpdateListener { onAnimationUpdate?.invoke(spannableString) }
            interpolator = LinearInterpolator()
            duration = mDuration
            repeatMode = ObjectAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            start()
        }
    }
}
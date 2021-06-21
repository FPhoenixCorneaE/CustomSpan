package com.fphoenixcorneae.text.style

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.format.DateUtils
import android.util.Property
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import java.util.*

/**
 * @desc AnimatedForegroundColorSpan 实现改变文字颜色效果。
 * @date 2020-09-27 16:14
 */
class AnimatedForegroundColorSpan(
    text: String,
    @ColorInt private val mStartColor: Int = Color.BLACK,
    @ColorInt private val mEndColor: Int = Color.RED,
    val start: Int,
    val end: Int,
    private val mDuration: Long = DateUtils.SECOND_IN_MILLIS,
    private val onAnimationUpdate: ((SpannableString) -> Unit)?,
) {

    private val mSpans: ArrayList<MutableForegroundColorSpan> = ArrayList()
    private var mAnimator: ObjectAnimator

    private fun addSpan(span: MutableForegroundColorSpan) {
        span.mColor = mStartColor
        span.mAlpha = 255
        mSpans.add(span)
    }

    private var mColor: Int
        get() = mStartColor
        set(color) {
            val size = mSpans.size
            for (index in 0 until size) {
                val mutableForegroundColorSpan = mSpans[index]
                mutableForegroundColorSpan.mColor = color
            }
        }


    private val mAnimatedForegroundColorSpanArgbProperty: Property<AnimatedForegroundColorSpan, Int> =
        object : Property<AnimatedForegroundColorSpan, Int>(
            Int::class.java, "mAnimatedForegroundColorSpanArgbProperty"
        ) {
            override operator fun set(span: AnimatedForegroundColorSpan, value: Int) {
                span.mColor = value
            }

            override operator fun get(span: AnimatedForegroundColorSpan): Int {
                return span.mColor
            }
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
        // 把文字切断成多个 character 的 span
        val spannableString = SpannableString(text).apply {
            repeat(text.length) { index ->
                if (index in start..end) {
                    with(MutableForegroundColorSpan()) {
                        addSpan(this)
                        setSpan(this, index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            }
        }
        // 改变 span 颜色后再改变其它的 span 颜色
        mAnimator = ObjectAnimator.ofInt(
            this,
            mAnimatedForegroundColorSpanArgbProperty,
            mStartColor,
            mEndColor
        ).apply {
            setEvaluator(ArgbEvaluator())
            addUpdateListener { onAnimationUpdate?.invoke(spannableString) }
            interpolator = LinearInterpolator()
            duration = mDuration
            start()
        }
    }
}
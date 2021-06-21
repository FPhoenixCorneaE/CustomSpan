package com.fphoenixcorneae.text.style

import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.util.Property
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import java.util.*

/**
 * @desc AnimatedTypeWriterSpan实现打字机效果。
 * @date 2020-09-27 16:50
 */
class AnimatedTypeWriterSpan constructor(
    text: String,
    @ColorInt private val mColor: Int = Color.BLACK,
    private val mDuration: Long = 2000,
    @FloatRange(from = 0.0, to = 1.0) private val mAlpha: Float = 0f,
    private val onAnimationUpdate: ((SpannableString) -> Unit)?
) {

    private val mSpans: ArrayList<MutableForegroundColorSpan> = ArrayList()

    private fun addSpan(span: MutableForegroundColorSpan) {
        span.mColor = mColor
        span.mAlpha = (mAlpha * 255).toInt()
        mSpans.add(span)
    }

    var alpha: Float
        get() = mAlpha
        set(alpha) {
            val size = mSpans.size
            var total = 1.0f * size * alpha
            for (index in 0 until size) {
                val mutableForegroundColorSpan = mSpans[index]
                when {
                    total >= 1.0f -> {
                        mutableForegroundColorSpan.mAlpha = 255
                        total -= 1.0f
                    }
                    else -> {
                        mutableForegroundColorSpan.mAlpha = (total * 255).toInt()
                        total = 0.0f
                    }
                }
            }
        }

    private val animatedTypeWriterSpanFloatProperty: Property<AnimatedTypeWriterSpan, Float> =
        object : Property<AnimatedTypeWriterSpan, Float>(
            Float::class.java, "animatedTypeWriterSpanFloatProperty"
        ) {
            override operator fun set(spanAnimated: AnimatedTypeWriterSpan, value: Float) {
                spanAnimated.alpha = value
            }

            override operator fun get(spanAnimated: AnimatedTypeWriterSpan): Float {
                return spanAnimated.alpha
            }
        }

    init {
        // 把文字切断成多个character的span
        val spannableString = SpannableString(text).apply {
            repeat(text.length) { index ->
                with(MutableForegroundColorSpan()) {
                    addSpan(this)
                    setSpan(this, index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }

        // 淡入span后再淡入其它的span
        ObjectAnimator.ofFloat(
            this, animatedTypeWriterSpanFloatProperty, 0f, 1f
        ).apply {
            setEvaluator(FloatEvaluator())
            addUpdateListener { onAnimationUpdate?.invoke(spannableString) }
            interpolator = LinearInterpolator()
            duration = mDuration
            start()
        }
    }
}
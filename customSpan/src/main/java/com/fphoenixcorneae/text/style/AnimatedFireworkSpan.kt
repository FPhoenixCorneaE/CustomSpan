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
 * @desc AnimatedFireworkSpan实现“烟火”动画,让文字随机淡入。
 * @date 2020-09-27 13:30
 */
class AnimatedFireworkSpan constructor(
    text: String,
    @ColorInt private val mColor: Int = Color.BLACK,
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

    private val animatedFireworkSpanFloatProperty: Property<AnimatedFireworkSpan, Float> =
        object : Property<AnimatedFireworkSpan, Float>(
            Float::class.java, "animatedFireworkSpanFloatProperty"
        ) {
            override operator fun set(spanAnimated: AnimatedFireworkSpan, value: Float) {
                spanAnimated.alpha = value
            }

            override operator fun get(spanAnimated: AnimatedFireworkSpan): Float {
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
        // 打乱顺序
        mSpans.shuffle()

        // 淡入span后再淡入其它的span
        ObjectAnimator.ofFloat(
            this, animatedFireworkSpanFloatProperty, 0f, 1f
        ).apply {
            setEvaluator(FloatEvaluator())
            addUpdateListener { onAnimationUpdate?.invoke(spannableString) }
            interpolator = LinearInterpolator()
            duration = 1000
            start()
        }
    }
}
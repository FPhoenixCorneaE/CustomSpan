package com.fphoenixcorneae.text.style

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.util.Property
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import java.util.*

/**
 * @desc AnimatedForegroundColorSpan实现改变文字颜色效果。
 * @date 2020-09-27 16:14
 */
class AnimatedForegroundColorSpan(
    text: String,
    @ColorInt private val mStartColor: Int = Color.BLACK,
    @ColorInt private val mEndColor: Int = Color.RED,
    val start: Int,
    val end: Int,
    private val onAnimationUpdate: ((SpannableString) -> Unit)?
) {

    private val mSpans: ArrayList<MutableForegroundColorSpan> = ArrayList()

    private fun addSpan(span: MutableForegroundColorSpan) {
        span.mColor = mStartColor
        span.mAlpha = 255
        mSpans.add(span)
    }

    private val spannableString = SpannableString(text)


    private val animatedForegroundColorSpanArgbProperty: Property<MutableForegroundColorSpan, Int> =
        object : Property<MutableForegroundColorSpan, Int>(
            Int::class.java, "animatedForegroundColorSpanArgbProperty"
        ) {
            override operator fun set(span: MutableForegroundColorSpan, value: Int) {
                span.mColor = value
            }

            override operator fun get(span: MutableForegroundColorSpan): Int {
                return span.mColor
            }
        }

    init {
        // 把文字切断成多个character的span
        spannableString.apply {
            repeat(text.length) { index ->
                if (index in start..end)
                    with(MutableForegroundColorSpan()) {
                        addSpan(this)
                        setSpan(this, index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                        ObjectAnimator.ofInt(
                            this,
                            animatedForegroundColorSpanArgbProperty,
                            mStartColor,
                            mEndColor
                        ).apply {
                            setEvaluator(ArgbEvaluator())
                            addUpdateListener { onAnimationUpdate?.invoke(spannableString) }
                            interpolator = LinearInterpolator()
                            duration = 1000
                            start()
                        }
                    }
            }
        }
    }
}
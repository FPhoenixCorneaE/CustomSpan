package com.fphoenixcorneae.text.style

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

/**
 * @desc Google提供的ImageSpan和DynamicDrawableSpan只能实现图片和文字底部对齐或者是baseline对齐，现在JustifyCenterImageSpan可以实现图片和文字居中对齐。
 * @date 2020-09-26 17:10
 */
class JustifyCenterImageSpan(
    private val mDrawable: Drawable
) : ImageSpan(mDrawable) {
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fontMetricsInt: FontMetricsInt?,
    ): Int {
        var drawable = drawable
        if (drawable == null) {
            drawable = mDrawable
        }
        val rect = drawable.bounds
        if (fontMetricsInt != null) {
            val fmPaint = paint.fontMetricsInt
            val fontHeight = fmPaint.bottom - fmPaint.top
            val drHeight = rect.bottom - rect.top
            val top = drHeight / 2 - fontHeight / 4
            val bottom = drHeight / 2 + fontHeight / 4
            fontMetricsInt.ascent = -bottom
            fontMetricsInt.top = -bottom
            fontMetricsInt.bottom = top
            fontMetricsInt.descent = top
        }
        return rect.right
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint,
    ) {
        val drawable = drawable
        canvas.save()
        val transY = (bottom - top - drawable.bounds.bottom) / 2 + top
        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}
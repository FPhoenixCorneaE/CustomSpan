package com.fphoenixcorneae.text.style

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.RectF
import android.text.style.ReplacementSpan
import androidx.annotation.ColorInt

/**
 * @desc FrameSpan 实现给相应的字符序列添加边框的效果
 * @date 2020-09-26 17:01
 */
class FrameSpan(
    @ColorInt frameColor: Int,
    private val cornerRadius: Float = 0f,
) : ReplacementSpan() {
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mWidth = 0

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: FontMetricsInt?,
    ): Int {
        // return text with relative to the Paint
        mWidth = paint.measureText(text, start, end).toInt()
        return mWidth
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
        // draw the frame with custom Paint
        // 设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。
        // paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        val oval = RectF(x, y + paint.ascent(), x + mWidth, y + paint.descent())
        canvas.drawRoundRect(
            oval,
            cornerRadius,
            cornerRadius,
            mPaint
        )
        canvas.drawText(text, start, end, x, y.toFloat(), paint)
    }

    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = frameColor
    }
}
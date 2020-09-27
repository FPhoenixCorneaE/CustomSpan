package com.fphoenixcorneae.text.style

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.RectF
import android.text.style.ReplacementSpan

/**
 * @desc RoundedBackgroundSpan实现给相应的字符序列添加圆角背景
 * @date 2020-09-27 11:59
 */
class RoundedBackgroundSpan(
    private val mColor: Int,
    private val mRadius: Float
) : ReplacementSpan() {
    private var mSize = 0
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        mSize = (paint.measureText(text, start, end) + 2 * mRadius).toInt()
        // mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        // 我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        // 可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return mSize
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
        paint: Paint
    ) {
        // 保存文字颜色
        val color = paint.color
        // 设置背景颜色
        paint.color = mColor
        // 设置画笔的锯齿效果
        paint.isAntiAlias = true
        // 设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。
        // paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        val oval = RectF(x, y + paint.ascent(), x + mSize, y + paint.descent())
        // 绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
        canvas.drawRoundRect(oval, mRadius.toFloat(), mRadius.toFloat(), paint)
        // 恢复画笔的文字颜色
        paint.color = color
        //绘制文字
        canvas.drawText(text, start, end, x + mRadius, y.toFloat(), paint)
    }

}
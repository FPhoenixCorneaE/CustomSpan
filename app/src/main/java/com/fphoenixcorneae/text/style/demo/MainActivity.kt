package com.fphoenixcorneae.text.style.demo

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import androidx.appcompat.app.AppCompatActivity
import com.fphoenixcorneae.text.style.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // FrameSpan实现给相应的字符序列添加边框的效果
        mTvFrameSpan.text = SpannableString(getString(R.string.span_string1)).apply {
            setSpan(FrameSpan(Color.RED, 10f), 28, 38, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
        // RoundedBackgroundSpan可以实现自定义圆角背景Span
        mTvRoundedBackgroundSpan.text = SpannableString(getString(R.string.span_string1)).apply {
            setSpan(
                RoundedBackgroundSpan(Color.GREEN, 8f),
                28,
                38,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        // Google提供的ImageSpan和DynamicDrawableSpan只能实现图片和文字底部对齐或者是baseline对齐，现在JustifyCenterImageSpan可以实现图片和文字居中对齐
        mTvJustifyCenterImageSpan.text = SpannableString(getString(R.string.span_string2)).apply {
            val drawable = getDrawable(R.mipmap.ic_launcher)!!
            drawable.setBounds(0, 0, 50, 50)
            setSpan(
                JustifyCenterImageSpan(drawable),
                5,
                6,
                ImageSpan.ALIGN_CENTER
            )
        }
        // 彩虹样的Span，主要是用到了Paint的Shader技术
        mTvRainbowSpan.text = SpannableString(getString(R.string.span_string3)).apply {
            setSpan(
                RainbowSpan(Color.RED, Color.GREEN, Color.BLUE),
                15,
                38,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        // AnimatedRainbowSpan实现一个动画的彩虹样式,实现思路：通过ObjectAnimator动画调整RainbowSpan中矩阵的平移，从而实现动画彩虹的效果
        AnimatedRainbowSpan(
            getString(R.string.span_string3),
            intArrayOf(Color.RED, Color.GREEN, Color.BLUE),
            15,
            38
        ) {
            mTvAnimatedRainbowSpan.text = it
        }
        // AnimatedFireworkSpan实现“烟火”动画,让文字随机淡入
        AnimatedFireworkSpan(getString(R.string.span_string4), mAlpha = 0f) {
            mTvAnimatedFireworkSpan.text = it
        }
        mTvAnimatedFireworkSpan.setOnClickListener {
            AnimatedFireworkSpan(getString(R.string.span_string4), mAlpha = 0f) {
                mTvAnimatedFireworkSpan.text = it
            }
        }
        // AnimatedForegroundColorSpan实现改变文字颜色效果
        AnimatedForegroundColorSpan(getString(R.string.span_string4), start = 10, end = 18) {
            mTvAnimatedForegroundColorSpan.text = it
        }
        mTvAnimatedForegroundColorSpan.setOnClickListener {
            AnimatedForegroundColorSpan(getString(R.string.span_string4), start = 10, end = 18) {
                mTvAnimatedForegroundColorSpan.text = it
            }
        }
        // AnimatedTypeWriterSpan实现打字机效果
        AnimatedTypeWriterSpan(getString(R.string.span_string5), mAlpha = 0f) {
            mTvAnimatedTypeWriterSpan.text = it
        }
        mTvAnimatedTypeWriterSpan.setOnClickListener {
            AnimatedTypeWriterSpan(getString(R.string.span_string5), mAlpha = 0f) {
                mTvAnimatedTypeWriterSpan.text = it
            }
        }
    }
}
package com.fphoenixcorneae.text.style.demo

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import androidx.appcompat.app.AppCompatActivity
import com.fphoenixcorneae.text.style.FrameSpan
import com.fphoenixcorneae.text.style.JustifyCenterImageSpan
import com.fphoenixcorneae.text.style.RainbowSpan
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTvFrameSpan.text = SpannableString(getString(R.string.span_string1)).apply {
            setSpan(FrameSpan(Color.RED, 10f), 28, 38, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
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
        mTvRainbowSpan.text = SpannableString(getString(R.string.span_string3)).apply {
            setSpan(
                RainbowSpan(Color.RED, Color.GREEN, Color.BLUE),
                15,
                38,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
    }
}
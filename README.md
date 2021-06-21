# CustomSpan
自定义 Span
--------------------------------------------------------------------

![图片预览](https://github.com/FPhoenixCorneaE/CustomSpan/blob/master/images/custom_span.png)


How to include it in your project:
--------------
**Step 1.** Add the JitPack repository to your build file

**groovy**
```groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

**kotlin**
```kotlin
allprojects {
	repositories {
		maven { setUrl("https://jitpack.io") }
	}
}
```

**Step 2.** Add the dependency
```groovy
dependencies {
	implementation("com.github.FPhoenixCorneaE:CustomSpan:1.0.1")
}
```


### 实现效果
- **FrameSpan** 

  给相应的字符序列添加边框。

- **JustifyCenterImageSpan** 

  图片和文字居中对齐。

- **AnimatedForegroundColorSpan** 

  改变文字颜色效果。

- **RainbowSpan** 

  彩虹样的Span。

- **AnimatedRainbowSpan** 

  一个动画的彩虹样式。

- **AnimatedFireworkSpan** 

  “烟火”动画。

- **AnimatedTypeWriterSpan** 

  打字机效果。

- **RoundedBackgroundSpan** 

  给相应的字符序列添加圆角背景。
  

#### 1、FrameSpan实现给相应的字符序列添加边框的效果

```kotlin
mTvFrameSpan.text = SpannableString(getString(R.string.span_string1)).apply {
            setSpan(FrameSpan(Color.RED, 10f), 28, 38, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
```

#### 2、RoundedBackgroundSpan可以实现自定义圆角背景Span

```kotlin
mTvRoundedBackgroundSpan.text = SpannableString(getString(R.string.span_string1)).apply {
            setSpan(
                RoundedBackgroundSpan(Color.GREEN, 8f),
                28,
                38,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
```

#### 3、Google提供的ImageSpan和DynamicDrawableSpan只能实现图片和文字底部对齐或者是baseline对齐，现在JustifyCenterImageSpan可以实现图片和文字居中对齐

```kotlin
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
```

#### 4、彩虹样的Span，主要是用到了Paint的Shader技术

```kotlin
mTvRainbowSpan.text = SpannableString(getString(R.string.span_string3)).apply {
            setSpan(
                RainbowSpan(Color.RED, Color.GREEN, Color.BLUE),
                15,
                38,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
```

#### 5、AnimatedRainbowSpan实现一个动画的彩虹样式,实现思路：通过ObjectAnimator动画调整RainbowSpan中矩阵的平移，从而实现动画彩虹的效果

```kotlin
AnimatedRainbowSpan(
            getString(R.string.span_string3),
            intArrayOf(Color.RED, Color.GREEN, Color.BLUE),
            15,
            38
        ) {
            mTvAnimatedRainbowSpan.text = it
        }
```

#### 6、AnimatedFireworkSpan实现“烟火”动画,让文字随机淡入

```kotlin
AnimatedFireworkSpan(getString(R.string.span_string4), mAlpha = 0f) {
            mTvAnimatedFireworkSpan.text = it
        }
```

#### 7、AnimatedForegroundColorSpan实现改变文字颜色效果

```kotlin
AnimatedForegroundColorSpan(getString(R.string.span_string4), start = 10, end = 18) {
            mTvAnimatedForegroundColorSpan.text = it
        }
```

#### 8、AnimatedTypeWriterSpan实现打字机效果

```kotlin
AnimatedTypeWriterSpan(getString(R.string.span_string5), mAlpha = 0f) {
            mTvAnimatedTypeWriterSpan.text = it
        }
```

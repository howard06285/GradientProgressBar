package com.shigaga.gradientprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import com.shigaga.gradientprogressbar.CustomGradientProgressBar.Companion.mAnimationDuration
import com.shigaga.gradientprogressbar.databinding.CustomGradientProgressbarBinding
import setVisibleOrInvisible


/**
 * Created by Howard on 2021/09/29
 *
 * 實現右側圓弧 + 背景漸層色 的客製化 ProgressBar
 *
 */

class CustomGradientProgressBar : RelativeLayout {

    private val TAG = javaClass.simpleName
    private var binding: CustomGradientProgressbarBinding? = null
    private var progressBarText = ""
    private var progressBarStrokeColor = R.color.color_pink_fcb3ac
    private var progressBarMaskLayerColor = R.color.color_pink_fcb3ac

    companion object {
        var mAnimationDuration = 600L

    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    fun init(context: Context, attributes: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CustomGradientProgressbarBinding.inflate(inflater, this, true)

        if (attributes != null) {
            context.theme.obtainStyledAttributes(attributes, R.styleable.CustomGradientProgressBar, 0, 0).let { attrs ->
                setProgressBarCornerRadius(attrs.getInt(R.styleable.CustomGradientProgressBar_progressBarCornerRadius, 0))
                setProgressDrawable(attrs.getResourceId(R.styleable.CustomGradientProgressBar_progressBarDrawable, R.drawable.gradient_progressbar_layerlist))
                setProgressBarAnimationDuration(attrs.getInteger(R.styleable.CustomGradientProgressBar_progressBarDuration, 600).toLong())
                setProgressBarTextViewIcon(attrs.getResourceId(R.styleable.CustomGradientProgressBar_progressBarTextViewIcon, R.drawable.ic_lightning_white))
                setProgressBarText(attrs.getString(R.styleable.CustomGradientProgressBar_progressBarText))
                setProgressBarTextSize(
                    attrs.getDimension(
                        R.styleable.CustomGradientProgressBar_progressBarTextSize,
                        resources.getDimension(R.dimen.custom_gradient_progressbar_default_text_size)
                    )
                )
                setProgressBarTextColor(attrs.getResourceId(R.styleable.CustomGradientProgressBar_progressBarTextColor, R.color.white))

                attrs.getResourceId(R.styleable.CustomGradientProgressBar_progressBarMaskLayerColor, R.color.color_pink_fcb3ac).let {
                    progressBarMaskLayerColor = it
                    setProgressMaskLayerColor(it)
                }

                attrs.getResourceId(R.styleable.CustomGradientProgressBar_progressBarStrokeColor, R.color.color_pink_fcb3ac).let {
                    progressBarStrokeColor = it
                    setProgressBarStrokeColor(it)
                }

                attrs.recycle()
            }
        }
    }

    /**
     *  @param progress ProgressBar 的進度百分比值
     *  @param animate 是否要有動畫
     */
    fun setProgressValue(progress: Int, animate: Boolean) {
        binding?.progressBarCover?.setMaskLayerProgress(progress = progress, animate = animate)
    }


    /**
     *  @param progressDrawable ProgressBar 的背景圖片，預設是漸層色
     */
    fun setProgressDrawable(@DrawableRes progressDrawable: Int) {
        binding?.progressBarBgImageView?.setBackgroundResource(progressDrawable)
    }

    /**
     *  @param iconDrawable ProgressBar 文字旁的 icon，預設 @drawable/ic_lightning_white
     */
    fun setProgressBarTextViewIcon(@DrawableRes iconDrawable: Int?) {
        binding?.progressBarText?.setCompoundDrawablesWithIntrinsicBounds(
            iconDrawable
                ?: 0,
            0,
            0,
            0
        )
    }

    /**
     *  @param textSize ProgressBar 上的文字大小，預設 12sp
     */
    private fun setProgressBarTextSize(@Dimension textSize: Float) {
        binding?.progressBarText?.textSize = Utils.pixelsToSp(context, textSize)
    }

    /**
     *  @param colorRes ProgressBar 上的文字顏色，預設 white
     */
    private fun setProgressBarTextColor(@ColorRes colorRes: Int?) {
        colorRes?.let {
            try {
                ContextCompat.getColor(context, it)

            } catch (e: Exception) {
                ContextCompat.getColor(context, R.color.color_pink_fcb3ac)

            }.let { color ->
                binding?.progressBarText?.setTextColor(color)
            }
        }
    }

    /**
     *  @param text ProgressBar 壓上的文字
     */
    fun setProgressBarText(text: String?) {
        binding?.progressBarText?.text = text ?: progressBarText
    }

    /**
     *  @param colorRes ProgressBar 遮罩顏色，預設 R.color.color_pink_fcb3ac
     */
    fun setProgressMaskLayerColor(@ColorRes colorRes: Int?) {
        colorRes?.let {
            try {
                ContextCompat.getColor(context, it)

            } catch (e: Exception) {
                ContextCompat.getColor(context, R.color.color_pink_fcb3ac)

            }.let { color ->
                binding?.progressBarCover?.mColorInt = color
            }
        }
    }

    /**
     *  @param dp ProgressBar 邊緣 stroke 寬度, 預設 1dp
     */
    fun setProgressBarStrokeWidth(dp: Int?) {
        dp?.let {
            (binding?.progressBarLayout?.layoutParams as? MarginLayoutParams)?.setMargins(
                Utils.dpToPx(it.toFloat()).toInt(),
                Utils.dpToPx(it.toFloat()).toInt(),
                Utils.dpToPx(it.toFloat()).toInt(),
                Utils.dpToPx(it.toFloat()).toInt()
            )
        }
    }

    /**
     *  @param colorRes ProgressBar 邊緣 stroke 顏色，預設 R.color.color_pink_fcb3ac
     */
    fun setProgressBarStrokeColor(@ColorRes colorRes: Int?) {
        colorRes?.let {
            try {
                ContextCompat.getColor(context, it)

            } catch (e: Exception) {
                ContextCompat.getColor(context, R.color.color_pink_fcb3ac)

            }.let { color ->
                binding?.progressBarOuterFrame?.setCardBackgroundColor(color)
            }
        }
    }

    /**
     *  @param duration ProgressBar 動畫的時間長度，in milli-second
     */
    private fun setProgressBarAnimationDuration(duration: Long) {
        mAnimationDuration = duration
    }

    /**
     *  @param cornerRadius ProgressBar 的圓角，直接透過 CustomGradientProgressBar 的 attribute 設定
     */
    fun setProgressBarCornerRadius(cornerRadius: Int?) {
        cornerRadius?.let { radius ->
            binding?.progressBarOuterFrame?.radius = Utils.dpToPx(radius.toFloat())
            binding?.progressBarLayout?.radius = Utils.dpToPx(radius.toFloat())
        }
    }
}


/**
 * Created by Howard on 2021/09/21
 *
 *  CustomGradientProgressBar 的圓弧遮罩層
 */
class GradientProgressBarMaskLayer : View, ValueAnimator.AnimatorUpdateListener {

    private val TAG = javaClass.simpleName
    var mColorInt: Int = "#FCB3AC".toColorInt()

    private val border by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.TRANSPARENT
            strokeWidth = 0f
        }
    }

    private val fill by lazy {
        Paint().apply {
            isAntiAlias = true
            color = mColorInt
        }
    }

    private var path: Path? = null
    private var maskLayerRect: RectF? = null
    private var mAnimator: ValueAnimator? = null
    private val duration get() = mAnimationDuration
    private var percentage = 0
    private var newW = width
    private var newH = height

    constructor(context: Context?) : super(context!!) {
        setWillNotDraw(false)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        setWillNotDraw(false)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        path?.let {
            canvas.drawPath(it, fill)
            canvas.drawPath(it, border)
        }
    }

    /**
     * 進度條預設狀態 100% 不顯示動畫
     */
    fun setMaskLayerProgress(progress: Int, animate: Boolean) {

        this.setVisibleOrInvisible(isVisible = progress < 100)

        percentage = progress

        if (progress < 100) {
            if (animate) {
                startAnim(progress = progress)
            } else {
                drawProgressBarMaskLayer(percentage = progress, newW, newH)
            }
        }
    }

    private fun startAnim(progress: Int) {
        mAnimator = ValueAnimator.ofInt(0, progress)
        mAnimator?.duration = duration
        mAnimator?.addUpdateListener(this)
        mAnimator?.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val animatedValue = animation.animatedValue as? Int

        animatedValue?.let { value ->
            path?.reset()
            drawProgressBarMaskLayer(percentage = value, viewWidth = newW, viewHeight = newH)
            invalidate()
        }
    }

    private fun drawProgressBarMaskLayer(percentage: Int, viewWidth: Int, viewHeight: Int) {

        val center = Point(viewWidth / 2, viewHeight / 2)
        val radius = (viewHeight / 2).toFloat()

        val arcSweep = 180
        val arcOffset = -90
        val arcCenterX = (percentage.toFloat() / 100f) * viewWidth.toFloat() - radius
        path = Path()
        maskLayerRect = RectF(arcCenterX - radius, center.y - radius, arcCenterX + radius, center.y + radius)

        path?.arcTo(maskLayerRect!!, (arcOffset + arcSweep).toFloat(), (-arcSweep).toFloat())
        path?.addRect(arcCenterX, center.y - radius, viewWidth.toFloat(), center.y + radius + 1, Path.Direction.CW)
        path?.close()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        newW = w
        newH = h
        drawProgressBarMaskLayer(percentage = percentage, viewWidth = newW, viewHeight = newH)
    }
}


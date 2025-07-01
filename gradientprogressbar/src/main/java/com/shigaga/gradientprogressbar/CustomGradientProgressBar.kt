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
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import com.shigaga.gradientprogressbar.databinding.CustomGradientProgressbarBinding

/**
 * CustomGradientProgressBar - A customizable progress bar with gradient background and animated arc mask
 * 
 * Features:
 * - Gradient background support
 * - Animated progress with arc mask effect
 * - Customizable text with icon
 * - Configurable colors, dimensions, and animations
 * - Multiple mask styles (arc, linear)
 * - Full XML attribute support
 * 
 * @author Howard
 * @since 1.0.0
 */
class CustomGradientProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var binding: CustomGradientProgressbarBinding? = null
    
    // Configuration properties
    private var progressBarText: String = ""
    private var progressBarTextSize: Float = 0f
    private var progressBarTextColor: Int = Color.WHITE
    private var progressBarTextIcon: Int = 0
    private var progressBarTextVisible: Boolean = true
    
    private var progressBarCornerRadius: Float = 0f
    private var progressBarBackground: Int = 0
    private var progressBarMaskColor: Int = "#FCB3AC".toColorInt()
    private var progressBarStrokeColor: Int = "#FCB3AC".toColorInt()
    private var progressBarStrokeWidth: Float = Utils.dpToPx(1f)
    private var progressBarHeight: Float = 0f
    
    private var progressBarAnimationDuration: Long = DEFAULT_ANIMATION_DURATION
    private var progressBarAnimationEnabled: Boolean = true
    
    private var progressBarInitialProgress: Int = 0
    private var progressBarMaxProgress: Int = 100
    private var currentProgress: Int = 0
    
    private var progressBarStyle: ProgressBarStyle = ProgressBarStyle.ROUNDED
    private var progressBarMaskStyle: MaskStyle = MaskStyle.ARC

    companion object {
        const val DEFAULT_ANIMATION_DURATION = 600L
        const val DEFAULT_TEXT_SIZE_SP = 12f
        const val DEFAULT_CORNER_RADIUS_DP = 10f
        const val DEFAULT_STROKE_WIDTH_DP = 1f
    }

    enum class ProgressBarStyle {
        ROUNDED, RECTANGULAR
    }

    enum class MaskStyle {
        ARC, LINEAR
    }

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val inflater = LayoutInflater.from(context)
        binding = CustomGradientProgressbarBinding.inflate(inflater, this, true)

        // Load default values
        progressBarTextSize = Utils.dpToPx(DEFAULT_TEXT_SIZE_SP)
        progressBarCornerRadius = Utils.dpToPx(DEFAULT_CORNER_RADIUS_DP)
        progressBarBackground = R.drawable.gradient_progressbar_layerlist
        progressBarTextIcon = R.drawable.ic_lightning_white

        attrs?.let { parseAttributes(context, it) }
        applyConfiguration()
    }

    private fun parseAttributes(context: Context, attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomGradientProgressBar,
            0, 0
        ).apply {
            try {
                // Text configuration
                progressBarText = getString(R.styleable.CustomGradientProgressBar_progressBarText) ?: ""
                progressBarTextSize = getDimension(
                    R.styleable.CustomGradientProgressBar_progressBarTextSize,
                    Utils.dpToPx(DEFAULT_TEXT_SIZE_SP)
                )
                progressBarTextColor = getColor(
                    R.styleable.CustomGradientProgressBar_progressBarTextColor,
                    Color.WHITE
                )
                progressBarTextIcon = getResourceId(
                    R.styleable.CustomGradientProgressBar_progressBarTextIcon,
                    R.drawable.ic_lightning_white
                )
                progressBarTextVisible = getBoolean(
                    R.styleable.CustomGradientProgressBar_progressBarTextVisible,
                    true
                )

                // Appearance configuration
                progressBarCornerRadius = getDimension(
                    R.styleable.CustomGradientProgressBar_progressBarCornerRadius,
                    Utils.dpToPx(DEFAULT_CORNER_RADIUS_DP)
                )
                progressBarBackground = getResourceId(
                    R.styleable.CustomGradientProgressBar_progressBarBackground,
                    R.drawable.gradient_progressbar_layerlist
                )
                progressBarMaskColor = getColor(
                    R.styleable.CustomGradientProgressBar_progressBarMaskColor,
                    "#FCB3AC".toColorInt()
                )
                progressBarStrokeColor = getColor(
                    R.styleable.CustomGradientProgressBar_progressBarStrokeColor,
                    "#FCB3AC".toColorInt()
                )
                progressBarStrokeWidth = getDimension(
                    R.styleable.CustomGradientProgressBar_progressBarStrokeWidth,
                    Utils.dpToPx(DEFAULT_STROKE_WIDTH_DP)
                )
                progressBarHeight = getDimension(
                    R.styleable.CustomGradientProgressBar_progressBarHeight,
                    0f
                )

                // Animation configuration
                progressBarAnimationDuration = getInteger(
                    R.styleable.CustomGradientProgressBar_progressBarAnimationDuration,
                    DEFAULT_ANIMATION_DURATION.toInt()
                ).toLong()
                progressBarAnimationEnabled = getBoolean(
                    R.styleable.CustomGradientProgressBar_progressBarAnimationEnabled,
                    true
                )

                // Progress configuration
                progressBarInitialProgress = getInteger(
                    R.styleable.CustomGradientProgressBar_progressBarInitialProgress,
                    0
                )
                progressBarMaxProgress = getInteger(
                    R.styleable.CustomGradientProgressBar_progressBarMaxProgress,
                    100
                )

                // Style configuration
                val styleValue = getInteger(R.styleable.CustomGradientProgressBar_gradientProgressBarStyle, 0)
                progressBarStyle = if (styleValue == 1) ProgressBarStyle.RECTANGULAR else ProgressBarStyle.ROUNDED

                val maskStyleValue = getInteger(R.styleable.CustomGradientProgressBar_progressBarMaskStyle, 0)
                progressBarMaskStyle = if (maskStyleValue == 1) MaskStyle.LINEAR else MaskStyle.ARC

            } finally {
                recycle()
            }
        }
    }

    private fun applyConfiguration() {
        binding?.let { binding ->
            // Apply text configuration
            binding.progressBarText.text = progressBarText
            binding.progressBarText.textSize = Utils.pxToSp(context, progressBarTextSize)
            binding.progressBarText.setTextColor(progressBarTextColor)
            binding.progressBarText.setCompoundDrawablesWithIntrinsicBounds(progressBarTextIcon, 0, 0, 0)
            binding.progressBarText.visibility = if (progressBarTextVisible) View.VISIBLE else View.GONE

            // Apply appearance configuration
            binding.progressBarOuterFrame.radius = progressBarCornerRadius
            binding.progressBarLayout.radius = progressBarCornerRadius
            binding.progressBarBgImageView.setBackgroundResource(progressBarBackground)
            binding.progressBarOuterFrame.setCardBackgroundColor(progressBarStrokeColor)
            binding.progressBarCover.setMaskColor(progressBarMaskColor)
            binding.progressBarCover.setMaskStyle(progressBarMaskStyle)
            binding.progressBarCover.setAnimationDuration(progressBarAnimationDuration)

            // Apply stroke width
            val strokeWidthPx = progressBarStrokeWidth.toInt()
            val layoutParams = binding.progressBarLayout.layoutParams as MarginLayoutParams
            layoutParams.setMargins(strokeWidthPx, strokeWidthPx, strokeWidthPx, strokeWidthPx)
            binding.progressBarLayout.layoutParams = layoutParams

            // Apply height if specified
            if (progressBarHeight > 0) {
                layoutParams.height = progressBarHeight.toInt()
            }

            // Set initial progress
            currentProgress = progressBarInitialProgress
            binding.progressBarCover.setProgress(currentProgress, false)
        }
    }

    // Public API methods

    /**
     * Sets the progress value with optional animation
     * @param progress The progress value (0-maxProgress)
     * @param animate Whether to animate the progress change
     */
    fun setProgress(@IntRange(from = 0) progress: Int, animate: Boolean = progressBarAnimationEnabled) {
        val clampedProgress = progress.coerceIn(0, progressBarMaxProgress)
        currentProgress = clampedProgress
        val percentage = (clampedProgress * 100) / progressBarMaxProgress
        binding?.progressBarCover?.setProgress(percentage, animate)
    }

    /**
     * Gets the current progress value
     * @return The current progress (0-maxProgress)
     */
    fun getProgress(): Int = currentProgress

    /**
     * Sets the maximum progress value
     * @param maxProgress The maximum progress value
     */
    fun setMaxProgress(@IntRange(from = 1) maxProgress: Int) {
        progressBarMaxProgress = maxProgress.coerceAtLeast(1)
        // Recalculate current progress percentage
        val percentage = (currentProgress * 100) / progressBarMaxProgress
        binding?.progressBarCover?.setProgress(percentage, false)
    }

    /**
     * Gets the maximum progress value
     * @return The maximum progress value
     */
    fun getMaxProgress(): Int = progressBarMaxProgress

    /**
     * Sets the progress bar text
     * @param text The text to display
     */
    fun setText(text: String) {
        progressBarText = text
        binding?.progressBarText?.text = text
    }

    /**
     * Gets the progress bar text
     * @return The current text
     */
    fun getText(): String = progressBarText

    /**
     * Sets the text size in SP
     * @param textSizeSp The text size in SP
     */
    fun setTextSize(@Dimension textSizeSp: Float) {
        progressBarTextSize = Utils.dpToPx(textSizeSp)
        binding?.progressBarText?.textSize = textSizeSp
    }

    /**
     * Sets the text color
     * @param color The color to set
     */
    fun setTextColor(@ColorInt color: Int) {
        progressBarTextColor = color
        binding?.progressBarText?.setTextColor(color)
    }

    /**
     * Sets the text color from resource
     * @param colorRes The color resource ID
     */
    fun setTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        setTextColor(color)
    }

    /**
     * Sets the text icon
     * @param iconRes The drawable resource ID for the icon
     */
    fun setTextIcon(@DrawableRes iconRes: Int) {
        progressBarTextIcon = iconRes
        binding?.progressBarText?.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0)
    }

    /**
     * Sets text visibility
     * @param visible Whether the text should be visible
     */
    fun setTextVisible(visible: Boolean) {
        progressBarTextVisible = visible
        binding?.progressBarText?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * Sets the mask color
     * @param color The mask color
     */
    fun setMaskColor(@ColorInt color: Int) {
        progressBarMaskColor = color
        binding?.progressBarCover?.setMaskColor(color)
    }

    /**
     * Sets the mask color from resource
     * @param colorRes The color resource ID
     */
    fun setMaskColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        setMaskColor(color)
    }

    /**
     * Sets the stroke color
     * @param color The stroke color
     */
    fun setStrokeColor(@ColorInt color: Int) {
        progressBarStrokeColor = color
        binding?.progressBarOuterFrame?.setCardBackgroundColor(color)
    }

    /**
     * Sets the stroke color from resource
     * @param colorRes The color resource ID
     */
    fun setStrokeColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        setStrokeColor(color)
    }

    /**
     * Sets the stroke width in DP
     * @param strokeWidthDp The stroke width in DP
     */
    fun setStrokeWidth(@Dimension strokeWidthDp: Float) {
        progressBarStrokeWidth = Utils.dpToPx(strokeWidthDp)
        binding?.progressBarLayout?.let { layout ->
            val strokeWidthPx = progressBarStrokeWidth.toInt()
            val layoutParams = layout.layoutParams as MarginLayoutParams
            layoutParams.setMargins(strokeWidthPx, strokeWidthPx, strokeWidthPx, strokeWidthPx)
            layout.layoutParams = layoutParams
        }
    }

    /**
     * Sets the corner radius in DP
     * @param cornerRadiusDp The corner radius in DP
     */
    fun setCornerRadius(@Dimension cornerRadiusDp: Float) {
        progressBarCornerRadius = Utils.dpToPx(cornerRadiusDp)
        binding?.progressBarOuterFrame?.radius = progressBarCornerRadius
        binding?.progressBarLayout?.radius = progressBarCornerRadius
    }

    /**
     * Sets the background drawable
     * @param backgroundRes The drawable resource ID
     */
    fun setBackgroundDrawable(@DrawableRes backgroundRes: Int) {
        progressBarBackground = backgroundRes
        binding?.progressBarBgImageView?.setBackgroundResource(backgroundRes)
    }

    /**
     * Sets the animation duration in milliseconds
     * @param durationMs The animation duration
     */
    fun setAnimationDuration(durationMs: Long) {
        progressBarAnimationDuration = durationMs
        binding?.progressBarCover?.setAnimationDuration(durationMs)
    }

    /**
     * Enables or disables animation
     * @param enabled Whether animation should be enabled
     */
    fun setAnimationEnabled(enabled: Boolean) {
        progressBarAnimationEnabled = enabled
    }

    /**
     * Sets the mask style
     * @param maskStyle The mask style to use
     */
    fun setMaskStyle(maskStyle: MaskStyle) {
        progressBarMaskStyle = maskStyle
        binding?.progressBarCover?.setMaskStyle(maskStyle)
    }

    /**
     * Sets the progress bar style
     * @param style The progress bar style to use
     */
    fun setProgressBarStyle(style: ProgressBarStyle) {
        progressBarStyle = style
        val radius = if (style == ProgressBarStyle.ROUNDED) progressBarCornerRadius else 0f
        binding?.progressBarOuterFrame?.radius = radius
        binding?.progressBarLayout?.radius = radius
    }
}

/**
 * GradientProgressBarMaskLayer - Custom view that creates the mask effect for the progress bar
 * 
 * This view handles the visual masking of the progress bar to create the progress effect.
 * It supports different mask styles and smooth animations.
 */
class GradientProgressBarMaskLayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {

    private var maskColor: Int = "#FCB3AC".toColorInt()
    private var animationDuration: Long = CustomGradientProgressBar.DEFAULT_ANIMATION_DURATION
    private var maskStyle: CustomGradientProgressBar.MaskStyle = CustomGradientProgressBar.MaskStyle.ARC

    private val borderPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.TRANSPARENT
            strokeWidth = 0f
        }
    }

    private val fillPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = maskColor
        }
    }

    private var maskPath: Path? = null
    private var maskRect: RectF? = null
    private var animator: ValueAnimator? = null
    private var currentPercentage = 0
    private var viewWidth = 0
    private var viewHeight = 0

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        fillPaint.color = maskColor
        maskPath?.let { path ->
            canvas.drawPath(path, fillPaint)
            canvas.drawPath(path, borderPaint)
        }
    }

    /**
     * Sets the progress percentage
     * @param percentage The progress percentage (0-100)
     * @param animate Whether to animate the change
     */
    fun setProgress(percentage: Int, animate: Boolean) {
        val clampedPercentage = percentage.coerceIn(0, 100)
        
        // Hide mask when progress is 100%
        setVisibleOrInvisible(clampedPercentage < 100)
        
        if (clampedPercentage >= 100) {
            currentPercentage = clampedPercentage
            return
        }

        if (animate && animationDuration > 0) {
            startAnimation(clampedPercentage)
        } else {
            currentPercentage = clampedPercentage
            updateMaskPath()
            invalidate()
        }
    }

    /**
     * Sets the mask color
     * @param color The color to use for the mask
     */
    fun setMaskColor(@ColorInt color: Int) {
        maskColor = color
        invalidate()
    }

    /**
     * Sets the animation duration
     * @param duration The animation duration in milliseconds
     */
    fun setAnimationDuration(duration: Long) {
        animationDuration = duration
    }

    /**
     * Sets the mask style
     * @param style The mask style to use
     */
    fun setMaskStyle(style: CustomGradientProgressBar.MaskStyle) {
        maskStyle = style
        updateMaskPath()
        invalidate()
    }

    private fun startAnimation(targetPercentage: Int) {
        animator?.cancel()
        animator = ValueAnimator.ofInt(currentPercentage, targetPercentage).apply {
            duration = animationDuration
            addUpdateListener(this@GradientProgressBarMaskLayer)
            start()
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val animatedValue = animation.animatedValue as? Int ?: return
        currentPercentage = animatedValue
        updateMaskPath()
        invalidate()
    }

    private fun updateMaskPath() {
        when (maskStyle) {
            CustomGradientProgressBar.MaskStyle.ARC -> updateArcMaskPath()
            CustomGradientProgressBar.MaskStyle.LINEAR -> updateLinearMaskPath()
        }
    }

    private fun updateArcMaskPath() {
        if (viewWidth <= 0 || viewHeight <= 0) return

        val center = Point(viewWidth / 2, viewHeight / 2)
        val radius = (viewHeight / 2).toFloat()
        val arcSweep = 180f
        val arcOffset = -90f
        val arcCenterX = (currentPercentage.toFloat() / 100f) * viewWidth.toFloat() - radius

        maskPath = Path()
        maskRect = RectF(
            arcCenterX - radius,
            center.y - radius,
            arcCenterX + radius,
            center.y + radius
        )

        maskPath?.apply {
            arcTo(maskRect!!, arcOffset + arcSweep, -arcSweep)
            addRect(
                arcCenterX,
                center.y - radius,
                viewWidth.toFloat(),
                center.y + radius + 1,
                Path.Direction.CW
            )
            close()
        }
    }

    private fun updateLinearMaskPath() {
        if (viewWidth <= 0 || viewHeight <= 0) return

        val maskWidth = (currentPercentage.toFloat() / 100f) * viewWidth.toFloat()
        
        maskPath = Path()
        maskPath?.addRect(
            maskWidth,
            0f,
            viewWidth.toFloat(),
            viewHeight.toFloat(),
            Path.Direction.CW
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        updateMaskPath()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator = null
    }
}
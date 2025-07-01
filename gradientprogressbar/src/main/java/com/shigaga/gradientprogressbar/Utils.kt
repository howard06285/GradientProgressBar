package com.shigaga.gradientprogressbar

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Utility class for common dimension conversions and calculations
 */
internal object Utils {
    
    /**
     * Converts dp to pixels
     * @param dp the dp value to convert
     * @return the pixel value
     */
    fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 
            dp.toFloat(), 
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    /**
     * Converts dp to pixels
     * @param dp the dp value to convert
     * @return the pixel value
     */
    fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 
            dp, 
            Resources.getSystem().displayMetrics
        )
    }

    /**
     * Converts pixels to dp
     * @param px the pixel value to convert
     * @param context the context (optional)
     * @return the dp value
     */
    fun pxToDp(px: Float, context: Context? = null): Float {
        val metrics = context?.resources?.displayMetrics ?: Resources.getSystem().displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * Converts pixels to sp
     * @param context the context
     * @param px the pixel value to convert
     * @return the sp value
     */
    fun pxToSp(context: Context, px: Float): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return px / scaledDensity
    }
}
package com.shigaga.gradientprogressbar

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

object Utils {
    private val TAG = javaClass.simpleName

    fun dpToPx(dp: Int): Int {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics)).toInt()
    }

    fun dpToPx(dp: Float): Float {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics)).toFloat()
    }

    fun convertPixelsToDp(px: Float, context: Context?): Float {
        return if (context != null) {
            val resources = context.resources
            val metrics = resources.displayMetrics
            px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        } else {
            val metrics = Resources.getSystem().displayMetrics
            px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }

    fun pixelsToSp(context: Context, px: Float): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return px / scaledDensity
    }

}
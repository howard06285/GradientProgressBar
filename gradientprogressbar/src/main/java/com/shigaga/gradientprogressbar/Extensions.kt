package com.shigaga.gradientprogressbar

import android.view.View

/**
 * Extension functions for View visibility management
 */

/**
 * Sets view visibility to VISIBLE or GONE based on the isVisible parameter
 * @param isVisible true to show the view, false to hide it with GONE
 */
internal fun View.setVisibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * Sets view visibility to VISIBLE or INVISIBLE based on the isVisible parameter
 * @param isVisible true to show the view, false to hide it with INVISIBLE
 */
internal fun View.setVisibleOrInvisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}
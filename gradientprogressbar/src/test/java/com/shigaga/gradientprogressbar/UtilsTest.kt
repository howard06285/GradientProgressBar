package com.shigaga.gradientprogressbar

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Utils class
 */
class UtilsTest {

    @Test
    fun testDpToPx_int() {
        val dp = 10
        val px = Utils.dpToPx(dp)
        assertTrue("DP to PX conversion should return positive value", px > 0)
    }

    @Test
    fun testDpToPx_float() {
        val dp = 10.5f
        val px = Utils.dpToPx(dp)
        assertTrue("DP to PX conversion should return positive value", px > 0f)
    }

    @Test
    fun testPxToDp() {
        val px = 100f
        val dp = Utils.pxToDp(px)
        assertTrue("PX to DP conversion should return positive value", dp > 0f)
    }

    @Test
    fun testDpToPxAndBack() {
        val originalDp = 20f
        val px = Utils.dpToPx(originalDp)
        val backToDp = Utils.pxToDp(px)
        
        // Allow for some rounding error
        val delta = 0.1f
        assertEquals("Converting DP->PX->DP should return approximately same value", 
                    originalDp, backToDp, delta)
    }

    @Test
    fun testZeroValues() {
        assertEquals("Zero DP should convert to zero PX", 0, Utils.dpToPx(0))
        assertEquals("Zero DP should convert to zero PX", 0f, Utils.dpToPx(0f), 0f)
        assertEquals("Zero PX should convert to zero DP", 0f, Utils.pxToDp(0f), 0f)
    }

    @Test
    fun testNegativeValues() {
        val negativeDp = -10
        val px = Utils.dpToPx(negativeDp)
        assertTrue("Negative DP should convert to negative PX", px < 0)
    }
}
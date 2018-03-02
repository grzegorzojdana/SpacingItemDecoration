package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

/**
 *
 *
 * @author Grzegorz Ojdana (grzegorz@nativetap.io)
 * Created 2018-02-27
 */
@RunWith(RobolectricTestRunner::class)
class SpacingTest {

    @Test
    fun zero() {
        val spacing = Spacing(
                horizontal = 12,
                vertical = 24,
                edges = Rect(12, 0, 54, 40),
                item = Rect(10, 5, 10, 5)
        )
        spacing.zero()
        assertEquals(spacing, Spacing())
    }

}
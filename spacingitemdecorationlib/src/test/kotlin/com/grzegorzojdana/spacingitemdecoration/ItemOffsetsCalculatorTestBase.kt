package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
abstract class ItemOffsetsCalculatorTestBase {

    protected fun test(calculator: ItemOffsetsCalculator,
                       offsetsRequest: ItemOffsetsCalculator.OffsetsRequest,
                       expectedOutRect: Rect) {
        val result = Rect()
        calculator.getItemOffsets(result, offsetsRequest)
        assertEquals(expectedOutRect, result)
    }

}
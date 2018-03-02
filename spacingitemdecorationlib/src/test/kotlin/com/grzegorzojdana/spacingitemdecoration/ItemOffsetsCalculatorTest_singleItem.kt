package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_singleItem : ItemOffsetsCalculatorTestBase() {

    @Test
    fun noSpacing() {
        val calc = ItemOffsetsCalculator(Spacing())
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 1)
        test(calc, offsetsRequest, Rect())
    }

    @Test
    fun edges() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(8, 5, 6, 10)))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 1)
        test(calc, offsetsRequest.apply { row = 0 }, Rect(8, 5, 6, 10))
    }

    @Test
    fun item() {
        val calc = ItemOffsetsCalculator(
                Spacing(item = Rect(2, 2, 3, 4)))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 1)
        test(calc, offsetsRequest.apply { row = 0 }, Rect(2, 2, 3, 4))
    }

    @Test
    fun horizontalAndVertical() {
        val calc = ItemOffsetsCalculator(
                Spacing(horizontal = 4, vertical = 6))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 1)
        test(calc, offsetsRequest.apply { row = 0 }, Rect())
    }

    @Test
    fun allSpacingProperties() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(2, 3, 2, 4),
                        item = Rect(0, 1, 1, 1),
                        horizontal = 5, vertical = 5))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 1)
        test(calc, offsetsRequest.apply { row = 0 }, Rect(2, 4, 3, 5))
    }

}
package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_singleColumn : ItemOffsetsCalculatorTestBase() {

    @Test
    fun noSpacing() {
        val calc = ItemOffsetsCalculator(Spacing())
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 3, cols = 1)
        val emptyRect = Rect()
        test(calc, offsetsRequest.apply { row = 0 }, emptyRect)
        test(calc, offsetsRequest.apply { row = 1 }, emptyRect)
        test(calc, offsetsRequest.apply { row = 2 }, emptyRect)
    }

    @Test
    fun edges() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(8, 5, 6, 10)))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 3, cols = 1)
        // sum of vertical spacing: 15; need to take 15/3 = 5 px from each item's height
        test(calc, offsetsRequest.apply { row = 0 }, Rect(8, 5, 6, 0))
        test(calc, offsetsRequest.apply { row = 1 }, Rect(8, 0, 6, 5))
        test(calc, offsetsRequest.apply { row = 2 }, Rect(8, -5, 6, 10))
    }

    @Test
    fun item() {
        val calc = ItemOffsetsCalculator(
                Spacing(item = Rect(2, 2, 3, 4)))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 3, cols = 1)
        // sum of vertical spacing: 3*(2+4) = 18; need to take 18/3 = 6 px from each item's height
        test(calc, offsetsRequest.apply { row = 0 }, Rect(2, 2, 3, 4))
        test(calc, offsetsRequest.apply { row = 1 }, Rect(2, 2, 3, 4))
        test(calc, offsetsRequest.apply { row = 2 }, Rect(2, 2, 3, 4))
    }

    @Test
    fun horizontalAndVertical() {
        val calc = ItemOffsetsCalculator(
                Spacing(horizontal = 4, vertical = 6))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 3, cols = 1)
        // sum of vertical spacing: 2*6 = 12; need to take 12/3 = 4 px from each item's height
        test(calc, offsetsRequest.apply { row = 0 }, Rect(0, 0, 0, 4))
        test(calc, offsetsRequest.apply { row = 1 }, Rect(0, 2, 0, 2))
        test(calc, offsetsRequest.apply { row = 2 }, Rect(0, 4, 0, 0))
    }

    @Test
    fun allSpacingProperties() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(2, 3, 2, 4),
                        item = Rect(1, 1, 1, 1),
                        horizontal = 4, vertical = 4))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 3, cols = 1)
        // vertical spacing: edges: 7, items: 3*2 = 6; vertical: 2*4 = 8; sum: 21; need to take 21/3 = 7 px from each item's height
        test(calc, offsetsRequest.apply { row = 0 }, Rect(3, 4, 3, 3))
        test(calc, offsetsRequest.apply { row = 1 }, Rect(3, 3, 3, 4))
        test(calc, offsetsRequest.apply { row = 2 }, Rect(3, 2, 3, 5))
    }

}
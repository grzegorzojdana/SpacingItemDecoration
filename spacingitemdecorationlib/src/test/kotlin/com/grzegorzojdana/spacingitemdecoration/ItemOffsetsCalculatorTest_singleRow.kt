package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_singleRow : ItemOffsetsCalculatorTestBase() {

    @Test
    fun noSpacing() {
        val calc = ItemOffsetsCalculator(Spacing())
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 3)
        val emptyRect = Rect()
        test(calc, offsetsRequest.apply { col = 0 }, emptyRect)
        test(calc, offsetsRequest.apply { col = 1 }, emptyRect)
        test(calc, offsetsRequest.apply { col = 2 }, emptyRect)
    }

    @Test
    fun edges() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(6, 4, 12, 10)))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 3)
        // sum of horizontal spacing: 18; need to take 18/3 = 6 px from each item's width
        test(calc, offsetsRequest.apply { col = 0 }, Rect(6, 4, 0, 10))
        test(calc, offsetsRequest.apply { col = 1 }, Rect(0, 4, 6, 10))
        test(calc, offsetsRequest.apply { col = 2 }, Rect(-6, 4, 12, 10))
    }

    @Test
    fun item() {
        val calc = ItemOffsetsCalculator(
                Spacing(item = Rect(3, 2, 1, 4)))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 3)
        // sum of horizontal spacing: 3*(3+1) = 12; need to take 12/3 = 4 px from each item's width
        test(calc, offsetsRequest.apply { col = 0 }, Rect(3, 2, 1, 4))
        test(calc, offsetsRequest.apply { col = 1 }, Rect(3, 2, 1, 4))
        test(calc, offsetsRequest.apply { col = 2 }, Rect(3, 2, 1, 4))
    }

    @Test
    fun horizontalAndVertical() {
        val calc = ItemOffsetsCalculator(
                Spacing(horizontal = 4, vertical = 6))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 4)
        // sum of horizontal spacing: 3*4 = 12; need to take 12/4 = 3 px from each item's width
        test(calc, offsetsRequest.apply { col = 0 }, Rect(0, 0, 3, 0))
        test(calc, offsetsRequest.apply { col = 1 }, Rect(1, 0, 2, 0))
        test(calc, offsetsRequest.apply { col = 2 }, Rect(2, 0, 1, 0))
        test(calc, offsetsRequest.apply { col = 3 }, Rect(3, 0, 0, 0))
    }

    @Test
    fun allSpacingProperties() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(5, 3, 2, 4),
                        item = Rect(1, 1, 1, 1),
                        horizontal = 4, vertical = 4))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 3)
        // horizontal spacing: edges: 7, items: 3*2 = 6; horizontal: 2*4 = 8; sum: 18; need to take 21/3 = 7 px from each item's width
        test(calc, offsetsRequest.apply { col = 0 }, Rect(6, 4, 1, 5))
        test(calc, offsetsRequest.apply { col = 1 }, Rect(5, 4, 2, 5))
        test(calc, offsetsRequest.apply { col = 2 }, Rect(4, 4, 3, 5))
    }
    
}
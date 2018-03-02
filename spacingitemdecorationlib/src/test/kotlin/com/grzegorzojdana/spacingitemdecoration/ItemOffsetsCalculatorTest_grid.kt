package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_grid : ItemOffsetsCalculatorTestBase() {

    @Test
    fun noSpacing() {
        val calc = ItemOffsetsCalculator(Spacing())
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 2, cols = 2)
        val emptyRect = Rect()
        test(calc, offsetsRequest.apply { row = 0; col = 0 }, emptyRect)
        test(calc, offsetsRequest.apply { row = 0; col = 1 }, emptyRect)
        test(calc, offsetsRequest.apply { row = 1; col = 0 }, emptyRect)
        test(calc, offsetsRequest.apply { row = 1; col = 1 }, emptyRect)
    }

    @Test
    fun edges() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(6, 4, 12, 10)))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 2, cols = 3)
        // sum of horizontal spacing: 18; need to take 18/3 = 6 px from each item's width
        // sum of vertical spacing: 14; need to take 14/2 = 7 px from each item's height
        test(calc, offsetsRequest.apply { row = 0; col = 0 }, Rect(6, 4, 0, 3))
        test(calc, offsetsRequest.apply { row = 0; col = 1 }, Rect(0, 4, 6, 3))
        test(calc, offsetsRequest.apply { row = 0; col = 2 }, Rect(-6, 4, 12, 3))
        test(calc, offsetsRequest.apply { row = 1; col = 0 }, Rect(6, -3, 0, 10))
        test(calc, offsetsRequest.apply { row = 1; col = 1 }, Rect(0, -3, 6, 10))
        test(calc, offsetsRequest.apply { row = 1; col = 2 }, Rect(-6, -3, 12, 10))
    }

    @Test
    fun item() {
        val calc = ItemOffsetsCalculator(
                Spacing(item = Rect(3, 2, 1, 4)))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 2, cols = 3)
        // for only item offsets, each item have the same offset, which is equal to specified spacing
        val expectedOffsets = Rect(3, 2, 1, 4)
        test(calc, offsetsRequest.apply { row = 0; col = 0 }, expectedOffsets)
        test(calc, offsetsRequest.apply { row = 0; col = 1 }, expectedOffsets)
        test(calc, offsetsRequest.apply { row = 0; col = 2 }, expectedOffsets)
        test(calc, offsetsRequest.apply { row = 1; col = 0 }, expectedOffsets)
        test(calc, offsetsRequest.apply { row = 1; col = 1 }, expectedOffsets)
        test(calc, offsetsRequest.apply { row = 1; col = 2 }, expectedOffsets)
    }

    @Test
    fun horizontalAndVertical() {
        val calc = ItemOffsetsCalculator(
                Spacing(horizontal = 3, vertical = 6))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 3, cols = 3)
        // sum of horizontal spacing: 2*3 = 6; need to take 6/3 = 2 px from each item's width
        // sum of vertical spacing: 2*6 = 12; need to take 12/3 = 4 px from each item's height
        test(calc, offsetsRequest.apply { row = 0; col = 0 }, Rect(0, 0, 2, 4))
        test(calc, offsetsRequest.apply { row = 0; col = 1 }, Rect(1, 0, 1, 4))
        test(calc, offsetsRequest.apply { row = 0; col = 2 }, Rect(2, 0, 0, 4))
        test(calc, offsetsRequest.apply { row = 1; col = 0 }, Rect(0, 2, 2, 2))
        test(calc, offsetsRequest.apply { row = 1; col = 1 }, Rect(1, 2, 1, 2))
        test(calc, offsetsRequest.apply { row = 1; col = 2 }, Rect(2, 2, 0, 2))
        test(calc, offsetsRequest.apply { row = 2; col = 0 }, Rect(0, 4, 2, 0))
        test(calc, offsetsRequest.apply { row = 2; col = 1 }, Rect(1, 4, 1, 0))
        test(calc, offsetsRequest.apply { row = 2; col = 2 }, Rect(2, 4, 0, 0))
    }

    @Test
    fun allSpacingProperties() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(4, 3, 2, 4),
                        item = Rect(1, 2, 1, 2),
                        horizontal = 8, vertical = 4))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 3, cols = 2)
        // horizontal spacing: edges: 6, items: 2*2 = 4; horizontal: 1*8 = 8; sum: 18; need to take 18/2 = 9 px from each item's width
        // vertical spacing: edges: 7, items: 3*4 = 12; vertical: 2*4 = 8; sum: 21; need to take 27/3 = 9 px from each item's height
        test(calc, offsetsRequest.apply { row = 0; col = 0 }, Rect(5, 5, 4, 4))
        test(calc, offsetsRequest.apply { row = 0; col = 1 }, Rect(6, 5, 3, 4))
        test(calc, offsetsRequest.apply { row = 1; col = 0 }, Rect(5, 4, 4, 5))
        test(calc, offsetsRequest.apply { row = 1; col = 1 }, Rect(6, 4, 3, 5))
        test(calc, offsetsRequest.apply { row = 2; col = 0 }, Rect(5, 3, 4, 6))
        test(calc, offsetsRequest.apply { row = 2; col = 1 }, Rect(6, 3, 3, 6))
    }
    
}
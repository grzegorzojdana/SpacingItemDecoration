package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_gridSpanSize : ItemOffsetsCalculatorTestBase() {

    @Test
    fun horizontalSpan() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(5, 3, 3, 4),
                        item = Rect(2, 2, 0, 2),
                        horizontal = 4, vertical = 4))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 3, cols = 4)
        // horizontal spacing: edges: 8, items: 4*2 = 8; horizontal: 3*4 = 12; sum: 28; need to take 28/4 = 7 px from each item's width
        // vertical spacing: edges: 7, items: 3*4 = 12; vertical: 2*4 = 8; sum: 21; need to take 27/3 = 9 px from each item's height
        // Table layout:
        //   0123
        //  +----+
        // 0|AAB |
        // 1|CCCD|
        // 2| EFF|
        //  +----+
        test(calc, offsetsRequest.apply { row = 0; col = 0; spanSizeH = 2 }, Rect(7, 5, 1, 4))
        test(calc, offsetsRequest.apply { row = 0; col = 2; spanSizeH = 1 }, Rect(5, 5, 2, 4))
        test(calc, offsetsRequest.apply { row = 1; col = 0; spanSizeH = 3 }, Rect(7, 4, 2, 5))
        test(calc, offsetsRequest.apply { row = 1; col = 3; spanSizeH = 1 }, Rect(4, 4, 3, 5))
        test(calc, offsetsRequest.apply { row = 2; col = 1; spanSizeH = 1 }, Rect(6, 3, 1, 6))
        test(calc, offsetsRequest.apply { row = 2; col = 2; spanSizeH = 2 }, Rect(5, 3, 3, 6))
    }

    @Test
    fun verticalSpan() {
        val calc = ItemOffsetsCalculator(
                Spacing(edges = Rect(5, 4, 3, 4),
                        item = Rect(2, 3, 0, 1),
                        horizontal = 4, vertical = 4))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 4, cols = 4)
        // horizontal spacing: edges: 8, items: 4*2 = 8; horizontal: 3*4 = 12; sum: 28; need to take 28/4 = 7 px from each item's width
        // vertical spacing: edges: 8, items: 4*4 = 16; vertical: 3*4 = 12; sum: 36; need to take 36/4 = 9 px from each item's height
        // Table layout:
        //   0123
        //  +----+
        // 0|ACD |
        // 1|ACE |
        // 2|BCEF|
        // 3| C F|
        //  +----+
        test(calc, offsetsRequest.apply { row = 0; col = 0; spanSizeV = 2 }, Rect(7, 7, 0, 3))
        test(calc, offsetsRequest.apply { row = 2; col = 0; spanSizeV = 1 }, Rect(7, 5, 0, 4))
        test(calc, offsetsRequest.apply { row = 0; col = 1; spanSizeV = 4 }, Rect(6, 7, 1, 5))
        test(calc, offsetsRequest.apply { row = 0; col = 2; spanSizeV = 1 }, Rect(5, 7, 2, 2))
        test(calc, offsetsRequest.apply { row = 1; col = 2; spanSizeV = 2 }, Rect(5, 6, 2, 4))
        test(calc, offsetsRequest.apply { row = 2; col = 3; spanSizeV = 2 }, Rect(4, 5, 3, 5))
    }

}
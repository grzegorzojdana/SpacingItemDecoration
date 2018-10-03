package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_gridSpanSize : ItemOffsetsCalculatorTestBase() {

    @Test
    fun horizontalSpan() {
        val spacing = Spacing(
                edges = Rect(5, 3, 3, 4),
                item = Rect(2, 2, 0, 2),
                horizontal = 4,
                vertical = 4)
        // horizontal spacing: edges: 8, items: 4*2 = 8; horizontal: 3*4 = 12; sum: 28; need to take 28/4 = 7 px from each item's width
        // vertical spacing: edges: 7, items: 3*4 = 12; vertical: 2*4 = 8; sum: 21; need to take 27/3 = 9 px from each item's height
        // Table layout:
        //   0123
        //  +----+
        // 0|AAB |
        // 1|CCCD|
        // 2| EFF|
        //  +----+
        onCalcWithSpacing(spacing) {
            table(rows = 3, cols = 4) {
                cell(0, 0) { spanSizeH = 2 } expects offsets(7, 5, 1, 4)
                cell(0, 2) { spanSizeH = 1 } expects offsets(5, 5, 2, 4)
                cell(1, 0) { spanSizeH = 3 } expects offsets(7, 4, 2, 5)
                cell(1, 3) { spanSizeH = 1 } expects offsets(4, 4, 3, 5)
                cell(2, 1) { spanSizeH = 1 } expects offsets(6, 3, 1, 6)
                cell(2, 2) { spanSizeH = 2 } expects offsets(5, 3, 3, 6)
            }
        }
    }

    @Test
    fun verticalSpan() {
        val spacing = Spacing(
                edges = Rect(5, 4, 3, 4),
                item = Rect(2, 3, 0, 1),
                horizontal = 4,
                vertical = 4)
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
        onCalcWithSpacing(spacing) {
            table(rows = 4, cols = 4) {
                cell(0, 0) { spanSizeV = 2 } expects offsets(7, 7, 0, 3)
                cell(2, 0) { spanSizeV = 1 } expects offsets(7, 5, 0, 4)
                cell(0, 1) { spanSizeV = 4 } expects offsets(6, 7, 1, 5)
                cell(0, 2) { spanSizeV = 1 } expects offsets(5, 7, 2, 2)
                cell(1, 2) { spanSizeV = 2 } expects offsets(5, 6, 2, 4)
                cell(2, 3) { spanSizeV = 2 } expects offsets(4, 5, 3, 5)
            }
        }
    }

}
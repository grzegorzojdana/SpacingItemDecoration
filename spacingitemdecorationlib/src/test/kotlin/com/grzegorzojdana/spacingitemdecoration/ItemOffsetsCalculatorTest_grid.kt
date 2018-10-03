package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_grid : ItemOffsetsCalculatorTestBase() {

    @Test
    fun noSpacing() {
        onCalcWithSpacing(Spacing()) {
            table(rows = 2, cols = 2) {
                eachCellExpects(noOffsets())
            }
        }
    }

    @Test
    fun edges() {
        val spacing = Spacing(
                edges = Rect(6, 4, 12, 10))
        // sum of horizontal spacing: 18; need to take 18/3 = 6 px from each item's width
        // sum of vertical spacing: 14; need to take 14/2 = 7 px from each item's height
        onCalcWithSpacing(spacing) {
            table(rows = 2, cols = 3) {
                cell(0, 0) expects offsets(6, 4, 0, 3)
                cell(0, 1) expects offsets(0, 4, 6, 3)
                cell(0, 2) expects offsets(-6, 4, 12, 3)
                cell(1, 0) expects offsets(6, -3, 0, 10)
                cell(1, 1) expects offsets(0, -3, 6, 10)
                cell(1, 2) expects offsets(-6, -3, 12, 10)
            }
        }
    }

    @Test
    fun item() {
        val spacing = Spacing(
                item = Rect(3, 2, 1, 4))
        // for only item offsets, each item have the same offset, which is equal to specified spacing
        val expectedOffsets = offsets(3, 2, 1, 4)
        onCalcWithSpacing(spacing) {
            table(rows = 2, cols = 3) {
                eachCellExpects(expectedOffsets)
            }
        }
    }

    @Test
    fun horizontalAndVertical() {
        val spacing = Spacing(horizontal = 3, vertical = 6)
        // sum of horizontal spacing: 2*3 = 6; need to take 6/3 = 2 px from each item's width
        // sum of vertical spacing: 2*6 = 12; need to take 12/3 = 4 px from each item's height
        onCalcWithSpacing(spacing) {
            table(rows = 3, cols = 3) {
                cell(0,0) expects offsets(0, 0, 2, 4)
                cell(0,1) expects offsets(1, 0, 1, 4)
                cell(0,2) expects offsets(2, 0, 0, 4)
                cell(1,0) expects offsets(0, 2, 2, 2)
                cell(1,1) expects offsets(1, 2, 1, 2)
                cell(1,2) expects offsets(2, 2, 0, 2)
                cell(2,0) expects offsets(0, 4, 2, 0)
                cell(2,1) expects offsets(1, 4, 1, 0)
                cell(2,2) expects offsets(2, 4, 0, 0)
            }
        }
    }

    @Test
    fun allSpacingProperties() {
        val spacing = Spacing(
                edges = Rect(4, 3, 2, 4),
                item = Rect(1, 2, 1, 2),
                horizontal = 8,
                vertical = 4)
        // horizontal spacing: edges: 6, items: 2*2 = 4; horizontal: 1*8 = 8; sum: 18; need to take 18/2 = 9 px from each item's width
        // vertical spacing: edges: 7, items: 3*4 = 12; vertical: 2*4 = 8; sum: 21; need to take 27/3 = 9 px from each item's height
        onCalcWithSpacing(spacing) {
            table(rows = 3, cols = 2) {
                cell(0, 0) expects offsets(5, 5, 4, 4)
                cell(0, 1) expects offsets(6, 5, 3, 4)
                cell(1, 0) expects offsets(5, 4, 4, 5)
                cell(1, 1) expects offsets(6, 4, 3, 5)
                cell(2, 0) expects offsets(5, 3, 4, 6)
                cell(2, 1) expects offsets(6, 3, 3, 6)
            }
        }
    }
    
}
package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_singleColumn : ItemOffsetsCalculatorTestBase() {

    @Test
    fun noSpacing() {
        onCalcWithSpacing(Spacing()) {
            table(rows = 3, cols = 1) {
                eachCellExpects(noOffsets())
            }
        }
    }

    @Test
    fun edges() {
        val spacing = Spacing(
                edges = Rect(8, 5, 6, 10))
        // sum of vertical spacing: 15; need to take 15/3 = 5 px from each item's height
        onCalcWithSpacing(spacing) {
            table(rows = 3, cols = 1) {
                cell(0, 0) expects offsets(8, 5, 6, 0)
                cell(1, 0) expects offsets(8, 0, 6, 5)
                cell(2, 0) expects offsets(8, -5, 6, 10)
            }
        }
    }

    @Test
    fun item() {
        val spacing = Spacing(
                item = Rect(2, 2, 3, 4))
        // sum of vertical spacing: 3*(2+4) = 18; need to take 18/3 = 6 px from each item's height
        onCalcWithSpacing(spacing) {
            table(rows = 3, cols = 1) {
                eachCellExpects(offsets(2, 2, 3, 4))
            }
        }
    }

    @Test
    fun horizontalAndVertical() {
        val spacing = Spacing(
                horizontal = 4,
                vertical = 6)
        // sum of vertical spacing: 2*6 = 12; need to take 12/3 = 4 px from each item's height
        onCalcWithSpacing(spacing) {
            table(rows = 3, cols = 1) {
                cell(0, 0) expects offsets(0, 0, 0, 4)
                cell(1, 0) expects offsets(0, 2, 0, 2)
                cell(2, 0) expects offsets(0, 4, 0, 0)
            }
        }
    }

    @Test
    fun allSpacingProperties() {
        val spacing = Spacing(
                edges = Rect(2, 3, 2, 4),
                item = Rect(1, 1, 1, 1),
                horizontal = 4,
                vertical = 4)
        // vertical spacing: edges: 7, items: 3*2 = 6; vertical: 2*4 = 8; sum: 21; need to take 21/3 = 7 px from each item's height
        onCalcWithSpacing(spacing) {
            table(rows = 3, cols = 1) {
                cell(0, 0) expects offsets(3, 4, 3, 3)
                cell(1, 0) expects offsets(3, 3, 3, 4)
                cell(2, 0) expects offsets(3, 2, 3, 5)
            }
        }
    }
}
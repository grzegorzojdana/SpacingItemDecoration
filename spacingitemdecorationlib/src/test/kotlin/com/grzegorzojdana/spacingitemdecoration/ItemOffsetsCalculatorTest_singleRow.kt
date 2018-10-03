package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_singleRow : ItemOffsetsCalculatorTestBase() {

    @Test
    fun noSpacing() {
        onCalcWithSpacing(Spacing()) {
            table(rows = 1, cols = 3) {
                eachCellExpects(noOffsets())
            }
        }
    }

    @Test
    fun edges() {
        val spacing = Spacing(
                edges = Rect(6, 4, 12, 10))
        // sum of horizontal spacing: 18; need to take 18/3 = 6 px from each item's width
        onCalcWithSpacing(spacing) {
            table(rows = 1, cols = 3) {
                cell(0, 0) expects offsets(6, 4, 0, 10)
                cell(0, 1) expects offsets(0, 4, 6, 10)
                cell(0, 2) expects offsets(-6, 4, 12, 10)
            }
        }
    }

    @Test
    fun item() {
        val spacing = Spacing(
                item = Rect(3, 2, 1, 4))
        val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest(rows = 1, cols = 3)
        // sum of horizontal spacing: 3*(3+1) = 12; need to take 12/3 = 4 px from each item's width
        onCalcWithSpacing(spacing) {
            table(rows = 1, cols = 3) {
                eachCellExpects(offsets(3, 2, 1, 4))
            }
        }
    }

    @Test
    fun horizontalAndVertical() {
        val spacing = Spacing(
                horizontal = 4,
                vertical = 6)
        // sum of horizontal spacing: 3*4 = 12; need to take 12/4 = 3 px from each item's width
        onCalcWithSpacing(spacing) {
            table(rows = 1, cols = 4) {
                cell(0, 0) expects offsets(0, 0, 3, 0)
                cell(0, 1) expects offsets(1, 0, 2, 0)
                cell(0, 2) expects offsets(2, 0, 1, 0)
                cell(0, 3) expects offsets(3, 0, 0, 0)
            }
        }
    }

    @Test
    fun allSpacingProperties() {
        val spacing = Spacing(
                edges = Rect(5, 3, 2, 4),
                item = Rect(1, 1, 1, 1),
                horizontal = 4,
                vertical = 4)
        // horizontal spacing: edges: 7, items: 3*2 = 6; horizontal: 2*4 = 8; sum: 18; need to take 21/3 = 7 px from each item's width
        onCalcWithSpacing(spacing) {
            table(rows = 1, cols = 3) {
                cell(0, 0) expects offsets(6, 4, 1, 5)
                cell(0, 1) expects offsets(5, 4, 2, 5)
                cell(0, 2) expects offsets(4, 4, 3, 5)
            }
        }
    }
    
}
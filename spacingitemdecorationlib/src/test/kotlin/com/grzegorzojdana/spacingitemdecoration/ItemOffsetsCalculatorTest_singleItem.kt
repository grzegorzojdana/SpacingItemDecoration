package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.Test

class ItemOffsetsCalculatorTest_singleItem : ItemOffsetsCalculatorTestBase() {

    @Test
    fun noSpacing() {
        onCalcWithSpacing(Spacing()) {
            table(rows = 1, cols = 1) {
                cell(0, 0) expects noOffsets()
            }
        }
    }

    @Test
    fun edges() {
        val spacing = Spacing(
                edges = Rect(8, 5, 6, 10))
        onCalcWithSpacing(spacing) {
            table(rows = 1, cols = 1) {
                cell(0, 0) expects offsets(8, 5, 6, 10)
            }
        }
    }

    @Test
    fun item() {
        val spacing = Spacing(
                item = Rect(2, 2, 3, 4))
        onCalcWithSpacing(spacing) {
            table(rows = 1, cols = 1) {
                cell(0, 0) expects offsets(2, 2, 3, 4)
            }
        }
    }

    @Test
    fun horizontalAndVertical() {
        val spacing = Spacing(
                horizontal = 4,
                vertical = 6)
        onCalcWithSpacing(spacing) {
            table(rows = 1, cols = 1) {
                cell(0, 0) expects noOffsets()
            }
        }
    }

    @Test
    fun allSpacingProperties() {
        val spacing = Spacing(
                edges = Rect(2, 3, 2, 4),
                item = Rect(0, 1, 1, 1),
                horizontal = 5,
                vertical = 5)
        onCalcWithSpacing(spacing) {
            table(rows = 1, cols = 1) {
                cell(0, 0) expects offsets(2, 4, 3, 5)
            }
        }
    }

}
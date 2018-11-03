package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
abstract class ItemOffsetsCalculatorTestBase {

    protected fun onCalcWithSpacing(spacing: Spacing, block: ItemOffsetsCalculator.() -> Unit) {
        ItemOffsetsCalculator(spacing).block()
    }

    protected fun ItemOffsetsCalculator.table(
            rows: Int,
            cols: Int,
            block: TableContext.() -> Unit
    ) {
        val request = ItemOffsetsCalculator.OffsetsRequest(rows = rows, cols = cols)
        TableContext(calc = this, request = request).block()
    }

    protected class TableContext(val calc: ItemOffsetsCalculator,
                                 val request: ItemOffsetsCalculator.OffsetsRequest) {
        fun cell(row: Int,
                 col: Int,
                 transform: (ItemOffsetsCalculator.OffsetsRequest.() -> Unit)? = null
        ): TableContext {
            request.row = row
            request.col = col
            transform?.let { request.it() }
            return this
        }

        fun forEachCell(predicate: TableContext.(row: Int, col: Int) -> Unit) {
            for (row in 0 until request.rows) {
                request.row = row
                for (col in 0 until request.cols) {
                    request.col = col
                    this.predicate(row, col)
                }
            }
        }

        infix fun expects(expectedOffsets: Rect) {
            val result = Rect()
            calc.getItemOffsets(result, request)
            assertEquals(expectedOffsets, result)
        }

        fun eachCellExpects(expected: Rect) {
            forEachCell { _, _ -> expects(expected) }
        }
    }

    protected fun offsets(left: Int, top: Int, right: Int, bottom: Int): Rect {
        return Rect(left, top, right, bottom)
    }

    protected fun noOffsets(): Rect = Rect()
}
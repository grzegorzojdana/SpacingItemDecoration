package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect

/**
 * Calculates RecyclerView item offsets depending on data prepared by [ItemOffsetsRequestBuilder].
 *
 * This class expects item position translated from RecyclerView LayoutManager coordinates to
 * grid table coordinates with first row on top and first column on left. Translation is done
 * by [ItemOffsetsRequestBuilder].
 */
open class ItemOffsetsCalculator(
        spacing: Spacing
) {

    /**
     * Input data for [ItemOffsetsCalculator].
     */
    data class OffsetsRequest(
            // indices of item in the table
            var row: Int = 0,
            var col: Int = 0,
            // size of item
            var spanSizeH: Int = 1,
            var spanSizeV: Int = 1,
            // total number of rows and cols in the table
            var rows: Int = 0,
            var cols: Int = 0
    ) {
        val lastRow: Int get() = row + spanSizeV - 1
        val lastCol: Int get() = col + spanSizeH - 1
    }

    open var spacing: Spacing = spacing
        set(value) { field = value; invalidatePrecalculatedValues() }
    
    private var precalculatedValuesInvalid = true
    // data used to determine precalculated values; stored to compare inside getItemOffsets() and determine are precalculated values valid or not
    private var rowsOfPrecalculatedValues = 0
    private var colsOfPrecalculatedValues = 0

    /* Precalculated values, constant for given spacing, items count and layout manager params. */

    private var startMargin: Int = 0
    private var itemDistanceH: Int = 0
    private var itemDeltaH: Float = 0F
    
    private var topMargin: Int = 0
    private var itemDistanceV: Int = 0
    private var itemDeltaV: Float = 0F


    /**
     * Calculate offsets for item specified by [OffsetsRequest] instance.
     * [offsetsRequest] `rows` and `cols` can't be `0`.
     */
    open fun getItemOffsets(outRect: Rect, offsetsRequest: OffsetsRequest) {
        updatePrecalculatedValuesValidityFor(offsetsRequest)
        if (precalculatedValuesInvalid) {
            validatePrecalculatedValues(offsetsRequest)
        }

        outRect.setEmpty()

        outRect.left = Math.round(startMargin + offsetsRequest.col * itemDeltaH)
        if (offsetsRequest.spanSizeH == 1) {
            outRect.right = Math.round(itemDistanceH - outRect.left - itemDeltaH)
        } else {
            outRect.right = Math.round(itemDistanceH - startMargin - (offsetsRequest.col + offsetsRequest.spanSizeH) * itemDeltaH)
        }

        outRect.top = Math.round(topMargin + offsetsRequest.row * itemDeltaV)
        if (offsetsRequest.spanSizeV == 1) {
            outRect.bottom = Math.round(itemDistanceV - outRect.top - itemDeltaV)
        } else {
            outRect.bottom = Math.round(itemDistanceV - topMargin - (offsetsRequest.row + offsetsRequest.spanSizeV) * itemDeltaV)
        }
    }
    
    open fun invalidatePrecalculatedValues() {
        precalculatedValuesInvalid = true
    }

    private fun updatePrecalculatedValuesValidityFor(offsetsRequest: OffsetsRequest) {
        if (offsetsRequest.rows != rowsOfPrecalculatedValues || offsetsRequest.cols != colsOfPrecalculatedValues) {
            precalculatedValuesInvalid = true
        }
    }

    private fun validatePrecalculatedValues(offsetsRequest: OffsetsRequest) {
        val offsetsSumH = spacing.edges.horizontalSum() + spacing.item.horizontalSum()
        val offsetsSumV = spacing.edges.verticalSum() + spacing.item.verticalSum()

        startMargin = spacing.edges.left + spacing.item.left
        itemDistanceH = spacing.horizontal + spacing.item.horizontalSum()
        itemDeltaH = itemDistanceH - (itemDistanceH * (offsetsRequest.cols - 1) + offsetsSumH) / offsetsRequest.cols.toFloat()

        topMargin = spacing.edges.top + spacing.item.top
        itemDistanceV = spacing.vertical + spacing.item.verticalSum()
        itemDeltaV = itemDistanceV - (itemDistanceV * (offsetsRequest.rows - 1) + offsetsSumV) / offsetsRequest.rows.toFloat()
        
        precalculatedValuesInvalid = false

        rowsOfPrecalculatedValues = offsetsRequest.rows
        colsOfPrecalculatedValues = offsetsRequest.cols
    }
}

private fun Rect.horizontalSum(): Int = left + right
private fun Rect.verticalSum(): Int = top + bottom
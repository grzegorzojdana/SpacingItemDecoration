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
    
    private var areCachedValuesInvalid = true
    // Data used to determine precalculated values; stored to compare inside getItemOffsets()
    // and determine if cached values are valid or not.
    private var cachedRows = 0
    private var cachedCols = 0

    // Precalculated (cached) values. Need to be calculated again when spacing, item count or
    // layout manager params change.

    private var startMargin: Int = 0
    private var topMargin: Int = 0
    private var itemDistanceH: Int = 0
    private var itemDistanceV: Int = 0
    private var itemDeltaH: Float = 0F
    private var itemDeltaV: Float = 0F


    /**
     * Calculate offsets for item specified by [OffsetsRequest] instance.
     * [offsetsRequest] `rows` and `cols` can't be `0`.
     */
    open fun getItemOffsets(outRect: Rect, offsetsRequest: OffsetsRequest) {
        updatePrecalculatedValuesValidityFor(offsetsRequest)
        if (areCachedValuesInvalid) {
            validatePrecalculatedValues(offsetsRequest)
        }

        outRect.apply {
            left   = Math.round(startMargin + offsetsRequest.col * itemDeltaH)
            top    = Math.round(topMargin + offsetsRequest.row * itemDeltaV)
            right  = Math.round(itemDistanceH - left - offsetsRequest.spanSizeH * itemDeltaH)
            bottom = Math.round(itemDistanceV - top - offsetsRequest.spanSizeV * itemDeltaV)
        }
    }
    
    open fun invalidatePrecalculatedValues() {
        areCachedValuesInvalid = true
    }

    private fun updatePrecalculatedValuesValidityFor(offsetsRequest: OffsetsRequest) {
        if (offsetsRequest.rows != cachedRows || offsetsRequest.cols != cachedCols) {
            areCachedValuesInvalid = true
        }
    }

    private fun validatePrecalculatedValues(offsetsRequest: OffsetsRequest) {
        with (spacing) {
            startMargin   = edges.left + item.left
            itemDistanceH = horizontal + item.left + item.right
            itemDeltaH    = (horizontal - edges.left - edges.right) / offsetsRequest.cols.toFloat()

            topMargin     = edges.top + item.top
            itemDistanceV = vertical + item.top + item.bottom
            itemDeltaV    = (vertical - edges.top - edges.bottom) / offsetsRequest.rows.toFloat()
        }

        areCachedValuesInvalid = false

        cachedRows = offsetsRequest.rows
        cachedCols = offsetsRequest.cols
    }
}
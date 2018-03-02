package com.grzegorzojdana.spacingitemdecoration

/**
 * Determine params for [ItemOffsetsCalculator] basing on given RecyclerView layout manager specifics.
 */
open class ItemOffsetsRequestBuilder {

    /**
     * Data that describes position of item that offsets are desired, and some of its list layout
     * manager params.
     */
    data class ItemOffsetsParams(
            /* Item data */
            var spanIndex: Int  = 0,
            var groupIndex: Int = 0,
            var spanSize: Int   = 1,
            /* Layout manager data */
            var spanCount: Int  = 1,
            var groupCount: Int = 1,
            var isLayoutVertical: Boolean = true,
            var isLayoutReverse: Boolean = false
    )

    /**
     * Fill [offsetsRequest] basing on RecyclerView item and layout params passed by
     * [itemOffsetsParams]. All fields of [offsetsRequest] will be set.
     */
    open fun fillItemOffsetsRequest(itemOffsetsParams: ItemOffsetsParams,
                                    offsetsRequest: ItemOffsetsCalculator.OffsetsRequest) {
        val groupIndexAdjustedToReverse =
                if (!itemOffsetsParams.isLayoutReverse) itemOffsetsParams.groupIndex
                else itemOffsetsParams.groupCount - itemOffsetsParams.groupIndex - 1

        // cols and rows number
        if (itemOffsetsParams.isLayoutVertical) {
            offsetsRequest.cols = itemOffsetsParams.spanCount
            offsetsRequest.rows = itemOffsetsParams.groupCount
        } else {
            offsetsRequest.cols = itemOffsetsParams.groupCount
            offsetsRequest.rows = itemOffsetsParams.spanCount
        }

        // item row and column
        if (itemOffsetsParams.isLayoutVertical) {
            offsetsRequest.col = itemOffsetsParams.spanIndex
            offsetsRequest.row = groupIndexAdjustedToReverse
        } else {
            offsetsRequest.col = groupIndexAdjustedToReverse
            offsetsRequest.row = itemOffsetsParams.spanIndex
        }

        // span size
        if (itemOffsetsParams.isLayoutVertical) {
            offsetsRequest.spanSizeH = itemOffsetsParams.spanSize
            offsetsRequest.spanSizeV = 1
        } else {
            offsetsRequest.spanSizeH = 1
            offsetsRequest.spanSizeV = itemOffsetsParams.spanSize
        }
    }

}
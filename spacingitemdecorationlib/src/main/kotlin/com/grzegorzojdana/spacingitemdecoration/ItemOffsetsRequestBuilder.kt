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
            var isLayoutReverse: Boolean  = false
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

        with (offsetsRequest) {
            if (itemOffsetsParams.isLayoutVertical) {
                row       = groupIndexAdjustedToReverse
                col       = itemOffsetsParams.spanIndex

                spanSizeH = itemOffsetsParams.spanSize
                spanSizeV = 1

                rows      = itemOffsetsParams.groupCount
                cols      = itemOffsetsParams.spanCount
            } else {
                row       = itemOffsetsParams.spanIndex
                col       = groupIndexAdjustedToReverse

                spanSizeH = 1
                spanSizeV = itemOffsetsParams.spanSize

                rows      = itemOffsetsParams.spanCount
                cols      = itemOffsetsParams.groupCount
            }
        }
    }
}
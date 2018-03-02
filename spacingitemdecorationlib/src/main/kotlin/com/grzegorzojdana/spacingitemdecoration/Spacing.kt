package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Rect

/**
 * Set of offsets available to define for [SpacingItemDecoration].
 */
data class Spacing(

        /**
         * Horizontal offset between every two items, in pixels.
         */
        var horizontal: Int = 0,

        /**
         * Vertical offset between every two items, in pixels.
         */
        var vertical: Int = 0,

        /**
         * Set of offsets that can be used to move items from parent edges toward parent's center.
         * Think about it like a list view padding.
         * Values in pixels are expected.
         */
        var edges: Rect = Rect(),

        /**
         * Offsets added to each of item edges, in pixels.
         */
        var item: Rect = Rect()
) {

    /**
     * Set all spacing to `0`.
     */
    fun zero() {
        horizontal = 0
        vertical = 0
        edges.setEmpty()
        item.setEmpty()
    }

}
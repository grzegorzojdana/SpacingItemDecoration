package com.grzegorzojdana.spacingitemdecorationapp.model

import androidx.recyclerview.widget.OrientationHelper

/**
 * Immutable model of RecyclerView LayoutManager configuration.
 */
data class ListLayoutConfig(
        val layoutType: Int          = LAYOUT_TYPE_LINEAR,
        val spanCount: Int           = 3,
        val orientation: Int         = OrientationHelper.VERTICAL,
        val reversed: Boolean        = false,
        val rtl: Boolean             = false,
        /** Allow to item have span size greater than 1. */
        val allowItemSpan: Boolean   = false
) {
    companion object {
        const val LAYOUT_TYPE_LINEAR         = 0
        const val LAYOUT_TYPE_GRID           = 1
        const val LAYOUT_TYPE_STAGGERED_GRID = 2
    }
}
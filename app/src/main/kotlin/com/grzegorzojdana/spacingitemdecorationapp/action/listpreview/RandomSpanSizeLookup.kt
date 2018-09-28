package com.grzegorzojdana.spacingitemdecorationapp.action.listpreview

import androidx.recyclerview.widget.GridLayoutManager
import java.util.*

/**
 * [SpanSizeLookup] implementation that provides pseudorandomly selected span size for each item.
 */
class RandomSpanSizeLookup(
        var spanCount: Int,
        val seed: Long = System.currentTimeMillis(),
        var preferSmallerSpans: Boolean = false
): GridLayoutManager.SpanSizeLookup() {

    private val random = Random()

    override fun getSpanSize(position: Int): Int {
        if (spanCount < 2) return 1
        random.setSeed(seed + position)
        if (preferSmallerSpans && random.nextBoolean()) {
            return 1 + random.nextInt(spanCount / 2)
        }
        return 1 + random.nextInt(spanCount)
    }
}
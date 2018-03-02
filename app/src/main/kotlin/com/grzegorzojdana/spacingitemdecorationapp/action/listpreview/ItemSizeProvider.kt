package com.grzegorzojdana.spacingitemdecorationapp.action.listpreview

import android.graphics.Point
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import com.grzegorzojdana.spacingitemdecorationapp.R
import com.grzegorzojdana.spacingitemdecorationapp.extensions.dimenPx
import com.grzegorzojdana.spacingitemdecorationapp.extensions.dpToPxInt
import java.util.*


/**
 * Provides dimensions of list item.
 */
interface ItemSizeProvider {
    /**
     * Determine dimensions of item view and fill passed [size] with expected item's with and height.
     */
    fun provideItemSize(size: Point, itemView: View, position: Int)
}

/**
 * Choose item dimensions basing on [layoutManager] properties. [seed] is used to randomize
 * values.
 */
class LayoutManagerDependentItemSizeProvider(
        var layoutManager: RecyclerView.LayoutManager,
        val seed: Long = System.currentTimeMillis()
) : ItemSizeProvider {

    private val random = Random()

    override fun provideItemSize(size: Point, itemView: View, position: Int) {
        val regularItemWidth = itemView.resources.dimenPx(R.dimen.list_preview_item_width)
        val regularItemHeight = itemView.resources.dimenPx(R.dimen.list_preview_item_height)

        val lm = layoutManager
        when (lm) {
            is LinearLayoutManager -> {
                // Linear or Grid
                if (lm.orientation == OrientationHelper.VERTICAL) {
                    size.set(ViewGroup.LayoutParams.MATCH_PARENT, regularItemHeight)
                } else {
                    size.set(regularItemWidth, ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
            is StaggeredGridLayoutManager -> {
                val length = determineLengthOfStaggeredGridItem(itemView, position)
                if (lm.orientation == OrientationHelper.VERTICAL) {
                    size.set(ViewGroup.LayoutParams.MATCH_PARENT, length)
                } else {
                    size.set(length, ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
            else -> {
                size.set(regularItemWidth, regularItemHeight)
            }
        }
    }

    private fun determineLengthOfStaggeredGridItem(itemView: View, position: Int): Int {
        // using bitmask instead of nextInt(16) for better performance and more interesting results, regardless of distribution
        val x = 8 + (generatePseudorandomFor(position) and 0x0F)
        return (x * itemView.resources.dpToPxInt(8))
    }

    private fun generatePseudorandomFor(position: Int): Int {
        return random.apply { setSeed(seed + position) }.nextInt()
    }

}
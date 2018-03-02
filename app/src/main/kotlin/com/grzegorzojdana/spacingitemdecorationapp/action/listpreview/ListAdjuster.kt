package com.grzegorzojdana.spacingitemdecorationapp.action.listpreview

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.grzegorzojdana.spacingitemdecorationapp.model.ListLayoutConfig

/**
 * To adjust previewed list to [ListLayoutConfig] changes.
 */
class ListAdjuster {

    fun adjustListToConfig(list: RecyclerView, config: ListLayoutConfig) {
        if (config.layoutType != determineLayoutTypeOf(list.layoutManager)) {
            list.layoutManager = makeLayoutManagerFromConfig(list.context, config)
        }
        else list.layoutManager?.apply {
            when (this) {
            // check Grid before Linear because GridLayoutManager is subclass of LinearLayoutManager
                is GridLayoutManager -> {
                    orientation    = config.orientation
                    reverseLayout  = config.reversed
                    spanCount      = config.spanCount
                }
                is LinearLayoutManager -> {
                    orientation   = config.orientation
                    reverseLayout = config.reversed
                }
                is StaggeredGridLayoutManager -> {
                    orientation   = config.orientation
                    reverseLayout = config.reversed
                    spanCount     = config.spanCount
                }
            }
        }

        // regardless if layout manager instance have been changed above or not
        list.layoutManager?.apply {
            if (this is GridLayoutManager) {
                spanSizeLookup = adjustSpanSizeLookupFor(this, config)
                spanSizeLookup.invalidateSpanIndexCache()
            }
        }
    }

    private fun createSpanSizeLookupForGrid(spanCount: Int): GridLayoutManager.SpanSizeLookup {
        return RandomSpanSizeLookup(spanCount, preferSmallerSpans = true).apply {
            isSpanIndexCacheEnabled = true
        }
    }

    private fun adjustSpanSizeLookupFor(layoutManager: GridLayoutManager,
                                        config: ListLayoutConfig): GridLayoutManager.SpanSizeLookup {
        return layoutManager.spanSizeLookup.run {
            when (this) {
                is RandomSpanSizeLookup -> {
                    if (config.allowItemSpan) this.apply { spanCount = config.spanCount }
                    else GridLayoutManager.DefaultSpanSizeLookup()
                }
                else -> {
                    if (config.allowItemSpan) createSpanSizeLookupForGrid(config.spanCount)
                    else this
                }
            }
        }
    }

    private fun determineLayoutTypeOf(layoutManager: RecyclerView.LayoutManager?): Int {
        return when (layoutManager) {
        // check grid before linear - because grid is subclass of linear
            is GridLayoutManager -> ListLayoutConfig.LAYOUT_TYPE_GRID
            is LinearLayoutManager -> ListLayoutConfig.LAYOUT_TYPE_LINEAR
            is StaggeredGridLayoutManager -> ListLayoutConfig.LAYOUT_TYPE_STAGGERED_GRID
            else -> -1
        }
    }

    private fun makeLayoutManagerFromConfig(context: Context,
                                            config: ListLayoutConfig): RecyclerView.LayoutManager {
        return when (config.layoutType) {
            ListLayoutConfig.LAYOUT_TYPE_LINEAR -> {
                LinearLayoutManager(context, config.orientation, config.reversed)
            }
            ListLayoutConfig.LAYOUT_TYPE_GRID -> {
                GridLayoutManager(context, config.spanCount, config.orientation, config.reversed)
            }
            ListLayoutConfig.LAYOUT_TYPE_STAGGERED_GRID -> {
                StaggeredGridLayoutManager(config.spanCount, config.orientation)
            }
            else -> throw IllegalArgumentException("Unknown type of layout manager: ${config.layoutType}")
        }
    }

}
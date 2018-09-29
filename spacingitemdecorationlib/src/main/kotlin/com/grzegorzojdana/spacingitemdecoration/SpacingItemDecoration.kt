package com.grzegorzojdana.spacingitemdecoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * [ItemDecoration] implementation that adds specified spacing to [RecyclerView]s items elements.
 * For flexibility, there are several offsets to define. Each of them can be used separately, or
 * together, depending on desired effect.
 */
class SpacingItemDecoration(
        /**
         * Desired offsets of RecyclerView items. See [Spacing].
         */
        spacing: Spacing
): ItemDecoration() {

    var itemOffsetsCalculator = ItemOffsetsCalculator(spacing)
    var itemOffsetsRequestBuilder = ItemOffsetsRequestBuilder()

    private val itemOffsetsParams = ItemOffsetsRequestBuilder.ItemOffsetsParams()
    private val offsetsRequest = ItemOffsetsCalculator.OffsetsRequest()

    private var cachedGroupCount: Int = -1

    private val drawing: Drawing by lazy(LazyThreadSafetyMode.NONE) { Drawing() }


    /**
     * Desired offsets of RecyclerView items. See [Spacing].
     * Returned object can be modified, but [invalidateSpacing] need to be called to apply changes.
     */
    var spacing: Spacing
        get() = itemOffsetsCalculator.spacing
        set(value) { itemOffsetsCalculator.spacing = value }

    /**
     * If you use `GridLayoutManager` with non-default `SpanSizeLookup` but its
     * `getSpanSize(position)` method always returns `1`, you can set this property to `true`
     * to improve performance.
     */
    var hintSpanSizeAlwaysOne: Boolean = false

    /**
     * Enable to improve performance for GridLayoutManager with relatively big number of list items
     * (thousands). This will make once determined items group count held to use for items offsets
     * calculations. If items count or layout manager specification (orientation, span size lookup,
     * spans) are changed, group count will also change, so if you set [cachedGroupCount] to `true`,
     * remember to call [invalidate] in that case.
     */
    var isGroupCountCacheEnabled: Boolean = false

    /**
     * Set to enable drawing applied spacing by this decoration. Useful for debugging.
     * @see [drawingConfig]
     */
    var isSpacingDrawingEnabled: Boolean = false

    /**
     * Colors used to mark spacing if [isSpacingDrawingEnabled] is set to `true`.
     */
    var drawingConfig: DrawingConfig = DrawingConfig()


    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) < 0) {
            outRect.setEmpty()
            return
        }

        determineItemOffsetsParams(view, parent, state, itemOffsetsParams)
        itemOffsetsRequestBuilder.fillItemOffsetsRequest(itemOffsetsParams, offsetsRequest)
        itemOffsetsCalculator.getItemOffsets(outRect, offsetsRequest)
    }

    private fun determineItemOffsetsParams(view: View,
                                           parent: RecyclerView,
                                           state: RecyclerView.State,
                                           itemOffsetsParams: ItemOffsetsRequestBuilder.ItemOffsetsParams) {
        val layoutManager = parent.layoutManager
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount // parent.adapter.itemCount

        when (layoutManager) {
            null -> throw IllegalArgumentException("RecyclerView without layout manager")
            is GridLayoutManager -> {
                val spanSizeLookup = layoutManager.spanSizeLookup
                val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams
                val clampedSpanCount = Math.max(layoutManager.spanCount, 1)
                val determinedGroupCount = getGridGroupCount(itemCount, layoutManager)

                itemOffsetsParams.apply {
                    spanIndex        = layoutParams.spanIndex
                    groupIndex       = spanSizeLookup.getSpanGroupIndex(itemPosition, clampedSpanCount)
                    spanSize         = layoutParams.spanSize
                    spanCount        = clampedSpanCount
                    groupCount       = determinedGroupCount
                    isLayoutVertical = (layoutManager.orientation == OrientationHelper.VERTICAL)
                    isLayoutReverse  = layoutManager.reverseLayout
                }
            }
            is StaggeredGridLayoutManager -> {
                val itemLayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams

                // could write some logic to determine and cache group index for each item
                // could access to Span object in item layout params through reflection

                itemOffsetsParams.apply {
                    spanIndex        = itemLayoutParams.spanIndex
                    groupIndex       = 0
                    spanSize         = if (itemLayoutParams.isFullSpan) layoutManager.spanCount else 1
                    spanCount        = layoutManager.spanCount
                    groupCount       = 1
                    isLayoutVertical = (layoutManager.orientation == OrientationHelper.VERTICAL)
                    isLayoutReverse  = layoutManager.reverseLayout
                }
            }
            is LinearLayoutManager -> {
                itemOffsetsParams.apply {
                    spanIndex        = 0
                    groupIndex       = itemPosition
                    spanSize         = 1
                    spanCount        = 1
                    groupCount       = itemCount
                    isLayoutVertical = (layoutManager.orientation == OrientationHelper.VERTICAL)
                    isLayoutReverse  = layoutManager.reverseLayout
                }
            }
            else -> throw IllegalArgumentException(
                    "Unsupported layout manager: ${layoutManager::class.java.simpleName}")
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (!isSpacingDrawingEnabled) return

        val itemsCount = parent.childCount
        if (itemsCount == 0) return

        parent.getDrawingRect(drawing.visibleRect)

        for (i in 0 until itemsCount) {
            val itemView = parent.getChildAt(i)

            with (drawing) {

                // item spacing

                drawingRect.set(
                        itemView.left - spacing.item.left,
                        itemView.top - spacing.item.top,
                        itemView.right + spacing.item.right,
                        itemView.bottom + spacing.item.bottom)
                drawRect(canvas, drawingConfig.itemColor)

                // horizontal spacing
                // no checking if item is most right item, we will draw edges spacing anyway

                drawingRect.left = itemView.right + spacing.item.right
                drawingRect.right = drawingRect.left + spacing.horizontal
                drawRect(canvas, drawingConfig.horizontalColor)

                // vertical spacing

                drawingRect.set(
                        itemView.left - spacing.item.left,
                        itemView.bottom + spacing.item.bottom,
                        itemView.right + spacing.item.right,
                        itemView.bottom + spacing.item.bottom + spacing.vertical)
                drawRect(canvas, drawingConfig.verticalColor)
            }
        }

        // edges

        if (spacing.edges.isAllZeros()) return
        val extremeItems = parent.getExtremeChildren()

        with (drawing) {
            paint.color = drawingConfig.edgeColor

            determineItemOffsetsParams(extremeItems[0], parent, state, itemOffsetsParams)
            itemOffsetsRequestBuilder.fillItemOffsetsRequest(itemOffsetsParams, offsetsRequest)
            if (offsetsRequest.col == 0) {
                drawingRect.set(
                        0,
                        0,
                        Math.min(extremeItems[0].left - spacing.item.left,
                                 spacing.edges.left),
                        visibleRect.bottom)
                drawRect(canvas)
            }

            determineItemOffsetsParams(extremeItems[1], parent, state, itemOffsetsParams)
            itemOffsetsRequestBuilder.fillItemOffsetsRequest(itemOffsetsParams, offsetsRequest)
            if (offsetsRequest.row == 0) {
                drawingRect.set(
                        0,
                        0,
                        visibleRect.right,
                        Math.min(extremeItems[1].top - spacing.item.top,
                                 spacing.edges.top))
                drawRect(canvas)
            }

            determineItemOffsetsParams(extremeItems[2], parent, state, itemOffsetsParams)
            itemOffsetsRequestBuilder.fillItemOffsetsRequest(itemOffsetsParams, offsetsRequest)
            if (offsetsRequest.lastCol == offsetsRequest.cols - 1) {
                drawingRect.set(
                        Math.max(extremeItems[2].right + spacing.item.right,
                                 visibleRect.right - spacing.edges.right),
                        0,
                        visibleRect.right,
                        visibleRect.bottom)
                drawRect(canvas)
            }

            determineItemOffsetsParams(extremeItems[3], parent, state, itemOffsetsParams)
            itemOffsetsRequestBuilder.fillItemOffsetsRequest(itemOffsetsParams, offsetsRequest)
            if (offsetsRequest.lastRow == offsetsRequest.rows - 1) {
                drawingRect.set(
                        0,
                        Math.max(extremeItems[3].bottom + spacing.item.bottom,
                                 visibleRect.bottom - spacing.edges.bottom),
                        visibleRect.right,
                        visibleRect.bottom)
                drawRect(canvas)
            }
        }
    }

    /**
     * Invalidate caches [spacing]. Should be called if [spacing] properties was modified without
     * setting [spacing] field.
     */
    fun invalidateSpacing() {
        itemOffsetsCalculator.invalidatePrecalculatedValues()
    }

    /**
     * Invalidate cached values.
     */
    fun invalidate() {
        invalidateSpacing()
        cachedGroupCount = -1
    }

    private fun getGridGroupCount(itemCount: Int, layoutManager: GridLayoutManager): Int {
        if (isGroupCountCacheEnabled && cachedGroupCount > 0)
            return cachedGroupCount

        val spanSizeLookup = layoutManager.spanSizeLookup
        val clampedSpanCount = Math.max(layoutManager.spanCount, 1)

        val groupCount: Int = when {
            hintSpanSizeAlwaysOne || spanSizeLookup is GridLayoutManager.DefaultSpanSizeLookup -> {
                Math.ceil(itemCount / clampedSpanCount.toDouble()).toInt()
            }
            else -> {
                val lastItemIndex = if (layoutManager.reverseLayout) 0 else itemCount - 1
                spanSizeLookup.getSpanGroupIndex(lastItemIndex, clampedSpanCount) + 1
            }
        }

        if (isGroupCountCacheEnabled)
            cachedGroupCount = groupCount

        return groupCount
    }


    data class DrawingConfig(
            var edgeColor: Int       = Color.parseColor("#F44336"), // Red 500
            var itemColor: Int       = Color.parseColor("#FFEB3B"), // Yellow 500
            var horizontalColor: Int = Color.parseColor("#00BCD4"), // Cyan 500
            var verticalColor: Int   = Color.parseColor("#76FF03") // Light Green A400
    )


    /**
     * Stuff used to draw spacings.
     */
    private class Drawing(
            val drawingRect: Rect = Rect(),
            val visibleRect: Rect = Rect(),
            val paint: Paint = Paint().apply {
                style = Paint.Style.FILL
            }
    ) {
        fun drawRect(canvas: Canvas, color: Int) {
            paint.color = color
            canvas.drawRect(drawingRect, paint)
        }

        fun drawRect(canvas: Canvas) = canvas.drawRect(drawingRect, paint)
    }

}


/**
 * Find most left, top, right and bottom children and return array with 4 items.
 * If list doesn't have children, empty array is returned.
 */
private fun RecyclerView.getExtremeChildren(): Array<View> {
    if (childCount == 0) return emptyArray()

    val firstChild = getChildAt(0)
    val extremeChildren = Array<View>(4, { firstChild })

    for (i in 1 until childCount) {
        val child = getChildAt(i)
        if (child.left   < extremeChildren[0].left)   extremeChildren[0] = child
        if (child.top    < extremeChildren[1].top)    extremeChildren[1] = child
        if (child.right  > extremeChildren[2].right)  extremeChildren[2] = child
        if (child.bottom > extremeChildren[3].bottom) extremeChildren[3] = child
    }

    return extremeChildren
}

/**
 * Check if all Rect edges are `0`.
 */
private fun Rect.isAllZeros(): Boolean {
    return (left == 0 && top == 0 && right == 0 && bottom == 0)
}

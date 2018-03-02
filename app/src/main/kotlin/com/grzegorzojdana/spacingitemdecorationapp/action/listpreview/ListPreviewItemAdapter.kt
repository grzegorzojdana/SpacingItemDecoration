package com.grzegorzojdana.spacingitemdecorationapp.action.listpreview

import android.graphics.Point
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.grzegorzojdana.spacingitemdecorationapp.R

/**
 * Simple adapter that will provide sequence of positive numbers as items of list.
 */
class ListPreviewItemAdapter(
        var numberCount: Int,
        var itemSizeProvider: ItemSizeProvider? = null,
        var itemViewAdjuster: ItemViewAdjuster? = null
): RecyclerView.Adapter<ListPreviewItemAdapter.ViewHolder>() {

    private val itemSize: Point = Point()

    var itemClickListener: ListItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
                .from(parent?.context)
                .inflate(R.layout.layout_preview_list_item, parent, false)

        val viewHolder = ViewHolder(itemView)
        itemView.setOnClickListener { itemClickListener?.invoke(viewHolder) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val number = getNumberForPosition(position)
        holder.number = number

        itemSizeProvider?.let {
            it.provideItemSize(itemSize, holder.itemView, position)
            holder.itemView.layoutParams.width = itemSize.x
            holder.itemView.layoutParams.height = itemSize.y
        }

        itemViewAdjuster?.adjustListItemViewOnBind(holder.itemView, position)
    }

    override fun getItemCount(): Int = numberCount

    override fun getItemId(position: Int): Long = position.toLong()

    fun getNumberForPosition(position: Int): Int {
        // counting numbers from 1 to numberCount, inclusive
        return position + 1
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var number: Int = 0
            set(value) { field = value; updateModelViews() }

        fun updateModelViews() {
            itemView.findViewById<TextView>(R.id.text)?.let {
                it.text = number.toString()
            }
        }
    }

}

typealias ListItemClickListener = (ListPreviewItemAdapter.ViewHolder) -> Unit
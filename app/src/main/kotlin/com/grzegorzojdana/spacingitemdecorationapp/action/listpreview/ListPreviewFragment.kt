package com.grzegorzojdana.spacingitemdecorationapp.action.listpreview

import androidx.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration
import com.grzegorzojdana.spacingitemdecorationapp.R
import com.grzegorzojdana.spacingitemdecorationapp.R.id.list
import com.grzegorzojdana.spacingitemdecorationapp.model.ListLayoutConfig
import com.grzegorzojdana.spacingitemdecorationapp.util.NullIgnoreObserver
import kotlinx.android.synthetic.main.fragment_list_preview.*

/**
 * List preview view.
 */
class ListPreviewFragment: Fragment() {

    private lateinit var viewModel: ListPreviewViewModel
    private val adapter: ListPreviewItemAdapter = ListPreviewItemAdapter(0)
    private val spacingItemDecoration = SpacingItemDecoration(Spacing())
    private lateinit var itemSizeProvider: LayoutManagerDependentItemSizeProvider
    private val itemViewAdjuster = ListConfigItemViewAdjuster()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_preview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupList()

        viewModel = ViewModelProviders.of(this).get(ListPreviewViewModel::class.java)
        viewModel.listLayoutConfig.observe(this, ListLayoutConfigObserver)

        viewModel.decorationConfig.observe(this, NullIgnoreObserver {
            spacingItemDecoration.isSpacingDrawingEnabled = it.enableDrawSpacing
            list?.invalidateItemDecorations()
        })

        viewModel.spacing.observe(this, NullIgnoreObserver {
            spacingItemDecoration.spacing = it
            list?.invalidateItemDecorations()
        })

        viewModel.itemCount.observe(this, NullIgnoreObserver { itemCount ->
            val delta = itemCount - adapter.numberCount
            adapter.numberCount = itemCount
            if (delta > 0) {
                adapter.notifyItemRangeInserted(itemCount - delta, delta)
            } else if (delta < 0) {
                adapter.notifyItemRangeRemoved(itemCount, -delta)
            }
            list?.invalidateItemDecorations()
        })
    }

    private fun setupList() {
        adapter.itemClickListener = {
            val itemView = it.itemView
            list?.layoutManager?.getDecorationRect(itemView)?.let {
                val msg = getString(R.string.preview_message_item_offsets, it.toString())
                Toast.makeText(itemView.context, msg, Toast.LENGTH_SHORT).show()
            }
        }
        adapter.setHasStableIds(true)

        list.setHasFixedSize(true)
        list.adapter = this.adapter
        list.layoutManager = LinearLayoutManager(list.context)
        list.addItemDecoration(spacingItemDecoration)

        itemSizeProvider = LayoutManagerDependentItemSizeProvider(list.layoutManager!!)
        adapter.itemSizeProvider = itemSizeProvider
        adapter.itemViewAdjuster = itemViewAdjuster
    }

    private val ListLayoutConfigObserver = NullIgnoreObserver<ListLayoutConfig> {
        val list = list ?: return@NullIgnoreObserver

        viewModel.listAdjuster.adjustListToConfig(list, it)

        itemSizeProvider.layoutManager = list.layoutManager!!
        itemViewAdjuster.listLayoutConfig = it

        // need to refresh item views sizing, which is done in onBindViewHolder()
        adapter.notifyDataSetChanged()
        list.invalidateItemDecorations()
    }
}


private fun RecyclerView.LayoutManager.getDecorationRect(itemView: View) = Rect(
            getLeftDecorationWidth(itemView),
            getTopDecorationHeight(itemView),
            getRightDecorationWidth(itemView),
            getBottomDecorationHeight(itemView)
)
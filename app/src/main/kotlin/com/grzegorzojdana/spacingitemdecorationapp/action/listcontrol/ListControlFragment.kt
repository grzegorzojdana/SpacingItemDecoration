package com.grzegorzojdana.spacingitemdecorationapp.action.listcontrol

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import com.grzegorzojdana.spacingitemdecorationapp.R
import com.grzegorzojdana.spacingitemdecorationapp.model.DecorationConfig
import com.grzegorzojdana.spacingitemdecorationapp.model.ListLayoutConfig
import com.grzegorzojdana.spacingitemdecorationapp.util.NullIgnoreObserver
import kotlinx.android.synthetic.main.fragment_list_control.*


class ListControlFragment: Fragment() {

    private lateinit var viewModel: ListControlViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view.context)

        viewModel = ViewModelProviders.of(this).get(ListControlViewModel::class.java).also {
            it.listLayoutConfig.observe(this, ListLayoutConfigObserver)
            it.decorationConfig.observe(this, DecorationConfigObserver)
            it.itemCount.observe(this, NullIgnoreObserver { seekBarItemCount.progress = it })
        }
    }

    private fun setupViews(context: Context) {
        spinnerLayout.adapter = ArrayAdapter.createFromResource(
                context, R.array.layout_manager_types, android.R.layout.simple_spinner_item)

        spinnerLayout.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateListLayoutConfig { it.copy(layoutType = position) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        radioGroupOrientation.setOnCheckedChangeListener { _, checkedId ->
            viewModel.updateListLayoutConfig {
                it.copy(orientation = when (checkedId) {
                    R.id.radioHorizontal -> OrientationHelper.HORIZONTAL
                    R.id.radioVertical   -> OrientationHelper.VERTICAL
                    else                 -> it.orientation
                })
            }
        }

        switchReversed.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateListLayoutConfig { it.copy(reversed = isChecked) }
        }

        seekBarItemCount.setOnSeekBarChangeListener(SeekBarProgressChangedListener({ _, progress, fromUser ->
            if (fromUser) {
                viewModel.itemCount.value = progress
            }
            labelItemCountValue.text = progress.toString()
        }))

        seekBarSpan.setOnSeekBarChangeListener(SeekBarProgressChangedListener({ _, progress, fromUser ->
            val newSpan = progress + 1
            if (fromUser) {
                viewModel.updateListLayoutConfig { it.copy(spanCount = newSpan) }
            }
            labelSpanValue.text = newSpan.toString()
        }))

        cbAllowItemSpan.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateListLayoutConfig { it.copy(allowItemSpan = isChecked) }
        }

        cbDrawSpacing.setOnCheckedChangeListener{ _, isChecked ->
            viewModel.updateDecorationConfig { it.copy(enableDrawSpacing = isChecked) }
        }
    }

    private val ListLayoutConfigObserver = NullIgnoreObserver<ListLayoutConfig> {
        spinnerLayout.setSelection(it.layoutType)

        radioGroupOrientation.check(when(it.orientation) {
            OrientationHelper.HORIZONTAL -> R.id.radioHorizontal
            OrientationHelper.VERTICAL   -> R.id.radioVertical
            else -> -1
        })

        switchReversed.isChecked = it.reversed

        if (it.layoutType == ListLayoutConfig.LAYOUT_TYPE_LINEAR) {
            seekBarSpan.progress = 0
            seekBarSpan.isEnabled = false
        } else {
            seekBarSpan.progress = it.spanCount - 1
            seekBarSpan.isEnabled = true
        }

        val allowItemsSpanning = viewModel.allowItemsSpanning(it.layoutType)
        cbAllowItemSpan.isEnabled = allowItemsSpanning
        cbAllowItemSpan.isChecked = allowItemsSpanning && it.allowItemSpan
    }

    private val DecorationConfigObserver = NullIgnoreObserver<DecorationConfig> {
        cbDrawSpacing.isChecked = it.enableDrawSpacing
    }
}


private class SeekBarProgressChangedListener(
        val callback: ((seekBar: SeekBar, progress: Int, fromUser: Boolean) -> Unit)
): SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        callback(seekBar, progress, fromUser)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}
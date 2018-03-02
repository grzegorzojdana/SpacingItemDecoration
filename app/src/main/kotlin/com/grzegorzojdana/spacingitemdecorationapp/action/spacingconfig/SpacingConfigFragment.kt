package com.grzegorzojdana.spacingitemdecorationapp.action.spacingconfig

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecorationapp.R
import com.grzegorzojdana.spacingitemdecorationapp.extensions.dpToPx
import com.grzegorzojdana.spacingitemdecorationapp.extensions.pxToDp
import com.grzegorzojdana.spacingitemdecorationapp.util.NullIgnoreObserver
import kotlinx.android.synthetic.main.fragment_spacing_config.*

/**
 * View where user can configure spacing.
 */
class SpacingConfigFragment: Fragment(), NumberPicker.OnValueChangeListener {

    private val SPACING_PICKER_STEP = 4F

    private lateinit var viewModel: SpacingConfigViewModel


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_spacing_config, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupModelViews(view)
        viewModel = ViewModelProviders.of(this).get(SpacingConfigViewModel::class.java)
        viewModel.spacing.observe(this, NullIgnoreObserver { updateModelViews(it) })
    }

    private fun setupModelViews(mainView: View) {
        btnSetZero.setOnClickListener { viewModel.setZeroSpacing() }
        btnSetDefault.setOnClickListener { viewModel.setDefaultSpacing() }

        val pickerMinValue = 0
        val pickerMaxValue = 16
        val displayValues = (pickerMinValue..pickerMaxValue)
                .map { SpacingPickerLabelFormatter.format(it) }
                .toTypedArray()

        (mainView as? ViewGroup)?.let {
            for (i in 0 until mainView.childCount) {
                (mainView.getChildAt(i) as? NumberPicker)?.apply {
                    minValue = pickerMinValue
                    maxValue = pickerMaxValue
                    // using display values instead of setting Formatter to avoid problem with label rendering
//                setFormatter(PickerLabelFormatter)
                    displayedValues = displayValues
                    setOnValueChangedListener(this@SpacingConfigFragment)
                    descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                }
            }
        }
    }

    private fun updateModelViews(spacing: Spacing) {
        fun spacingToPickerValue(spacingPx: Int): Int
                = resources.pxToDp(spacingPx.toFloat() / SPACING_PICKER_STEP).toInt()

        pickerEdgeLeft.value   = spacingToPickerValue(spacing.edges.left)
        pickerEdgeTop.value    = spacingToPickerValue(spacing.edges.top)
        pickerEdgeRight.value  = spacingToPickerValue(spacing.edges.right)
        pickerEdgeBottom.value = spacingToPickerValue(spacing.edges.bottom)

        pickerItemLeft.value   = spacingToPickerValue(spacing.item.left)
        pickerItemTop.value    = spacingToPickerValue(spacing.item.top)
        pickerItemRight.value  = spacingToPickerValue(spacing.item.right)
        pickerItemBottom.value = spacingToPickerValue(spacing.item.bottom)

        pickerHorizontal.value = spacingToPickerValue(spacing.horizontal)
        pickerVertical.value   = spacingToPickerValue(spacing.vertical)
    }

    override fun onValueChange(picker: NumberPicker, oldVal: Int, newVal: Int) {
        val currentSpacing = viewModel.spacing.value ?: return
        val spacingNewValue = resources.dpToPx(newVal * SPACING_PICKER_STEP).toInt()
        viewModel.spacing.value = currentSpacing.apply {
            when (picker.id) {
                R.id.pickerEdgeLeft   -> edges.left = spacingNewValue
                R.id.pickerEdgeTop    -> edges.top = spacingNewValue
                R.id.pickerEdgeRight  -> edges.right = spacingNewValue
                R.id.pickerEdgeBottom -> edges.bottom = spacingNewValue

                R.id.pickerItemLeft   -> item.left = spacingNewValue
                R.id.pickerItemTop    -> item.top = spacingNewValue
                R.id.pickerItemRight  -> item.right = spacingNewValue
                R.id.pickerItemBottom -> item.bottom = spacingNewValue

                R.id.pickerHorizontal -> horizontal = spacingNewValue
                R.id.pickerVertical   -> vertical = spacingNewValue
            }
        }
    }

    private val SpacingPickerLabelFormatter = NumberPicker.Formatter { value ->
        val valueAsDp = (value * SPACING_PICKER_STEP).toInt()
        resources?.getString(R.string.spacing_config_picker_formatter_dp, valueAsDp)
    }

}
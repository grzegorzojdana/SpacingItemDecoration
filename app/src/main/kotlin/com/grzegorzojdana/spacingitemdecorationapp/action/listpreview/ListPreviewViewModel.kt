package com.grzegorzojdana.spacingitemdecorationapp.action.listpreview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecorationapp.model.DecorationConfig
import com.grzegorzojdana.spacingitemdecorationapp.model.ListDataRepository
import com.grzegorzojdana.spacingitemdecorationapp.model.ListLayoutConfig

/**
 * To pass model into list preview view.
 */
class ListPreviewViewModel: ViewModel() {

    private val listDataRepository = ListDataRepository

    // read-only properties since we not modify them, only need to setup preview list
    val listLayoutConfig: LiveData<ListLayoutConfig> get() = listDataRepository.listLayoutConfig
    val decorationConfig: LiveData<DecorationConfig> get() = listDataRepository.decorationConfig
    val spacing: LiveData<Spacing> get() = listDataRepository.spacing
    val itemCount: LiveData<Int> get() = listDataRepository.itemCount

    val listAdjuster: ListAdjuster = ListAdjuster()
}
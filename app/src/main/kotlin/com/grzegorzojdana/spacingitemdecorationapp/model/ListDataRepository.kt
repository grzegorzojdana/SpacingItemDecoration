package com.grzegorzojdana.spacingitemdecorationapp.model

import androidx.lifecycle.MutableLiveData
import com.grzegorzojdana.spacingitemdecoration.Spacing

/**
 * Holds configuration of list and spacing.
 */
object ListDataRepository {

    val emptyListDataProvider = EmptyListDataProvider()

    var defaultListDataProvider: ListDataProvider = emptyListDataProvider

    val listLayoutConfig = MutableLiveData<ListLayoutConfig>()
    val decorationConfig = MutableLiveData<DecorationConfig>()
    val spacing = MutableLiveData<Spacing>()
    val itemCount = MutableLiveData<Int>()


    init {
        resetToDefaults()
    }

    fun resetToDefaults() = setWithProvider(defaultListDataProvider)

    private fun setWithProvider(listDataProvider: ListDataProvider) {
        listDataProvider.let {
            listLayoutConfig.value = it.listLayoutConfig
            decorationConfig.value = it.decorationConfig
            spacing.value          = it.spacing
            itemCount.value        = it.itemCount
        }
    }

}
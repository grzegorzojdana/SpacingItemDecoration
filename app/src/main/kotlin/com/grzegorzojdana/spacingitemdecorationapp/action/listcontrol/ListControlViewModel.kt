package com.grzegorzojdana.spacingitemdecorationapp.action.listcontrol

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.grzegorzojdana.spacingitemdecorationapp.model.DecorationConfig
import com.grzegorzojdana.spacingitemdecorationapp.model.ListDataRepository
import com.grzegorzojdana.spacingitemdecorationapp.model.ListLayoutConfig

/**
 * To modify list preview properties.
 */
class ListControlViewModel: ViewModel() {

    private val listDataRepository = ListDataRepository

    val listLayoutConfig: MutableLiveData<ListLayoutConfig> get() = listDataRepository.listLayoutConfig
    val decorationConfig: MutableLiveData<DecorationConfig> get() = listDataRepository.decorationConfig
    val itemCount: MutableLiveData<Int> get() = listDataRepository.itemCount

    fun allowItemsSpanning(layoutType: Int): Boolean = when(layoutType) {
        ListLayoutConfig.LAYOUT_TYPE_GRID,
        ListLayoutConfig.LAYOUT_TYPE_STAGGERED_GRID -> true
        else -> false
    }

    fun updateListLayoutConfig(block: (currentConfig: ListLayoutConfig) -> ListLayoutConfig) {
        listLayoutConfig.value?.let {
            listLayoutConfig.value = block(it)
        }
    }

    fun updateDecorationConfig(block: (currentConfig: DecorationConfig) -> DecorationConfig) {
        decorationConfig.value?.let {
            decorationConfig.value = block(it)
        }
    }

}
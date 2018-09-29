package com.grzegorzojdana.spacingitemdecorationapp.action.spacingconfig

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecorationapp.model.ListDataRepository

class SpacingConfigViewModel: ViewModel() {

    private val listDataRepository: ListDataRepository = ListDataRepository

    val spacing: MutableLiveData<Spacing> get() = listDataRepository.spacing

    fun setZeroSpacing() {
        spacing.value = Spacing()
    }

    fun setDefaultSpacing() {
        spacing.value = listDataRepository.defaultListDataProvider.spacing
    }
}
package com.grzegorzojdana.spacingitemdecorationapp.action.spacingconfig

import android.graphics.Rect
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecorationapp.model.ListDataProvider
import com.grzegorzojdana.spacingitemdecorationapp.model.ListDataRepository
import com.grzegorzojdana.spacingitemdecorationapp.model.ListLayoutConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@RunWith(RobolectricTestRunner::class)
class SpacingConfigViewModelTest {

    @Test
    fun setDefaultSpacing() {
        val defaultSpacing = Spacing(
                horizontal = 12,
                vertical = 8,
                edges = Rect(0, 6, 0, 12))
        val defaultConfigProvider = object : ListDataProvider {
            override val listLayoutConfig: ListLayoutConfig get() = ListLayoutConfig()
            override val spacing: Spacing get() = defaultSpacing.copy()
            override val itemCount: Int = 8
        }

        val listDataRepository = ListDataRepository
        listDataRepository.defaultListDataProvider = defaultConfigProvider

        val viewModel = SpacingConfigViewModel()

        viewModel.spacing.value?.apply {
            horizontal = 20
            vertical = 4
            item = Rect(12, 4, 6, 4)
        }

        assertNotEquals(defaultSpacing, viewModel.spacing.value)

        viewModel.setDefaultSpacing()
        assertEquals(defaultSpacing, viewModel.spacing.value)
        assertEquals(12, viewModel.spacing.value?.horizontal)
    }

}
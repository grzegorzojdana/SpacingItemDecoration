package com.grzegorzojdana.spacingitemdecorationapp.action.main

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecorationapp.R
import com.grzegorzojdana.spacingitemdecorationapp.extensions.dpToPxInt
import com.grzegorzojdana.spacingitemdecorationapp.model.DecorationConfig
import com.grzegorzojdana.spacingitemdecorationapp.model.ListDataProvider
import com.grzegorzojdana.spacingitemdecorationapp.model.ListDataRepository
import com.grzegorzojdana.spacingitemdecorationapp.model.ListLayoutConfig

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_bottom_sheet)

        setupDefaultConfigValues()

        if (savedInstanceState == null) {
            ListDataRepository.resetToDefaults()
        }
    }

    private fun setupDefaultConfigValues() {
        ListDataRepository.defaultListDataProvider = object : ListDataProvider {
            override val listLayoutConfig get() = ListLayoutConfig(
                    layoutType = ListLayoutConfig.LAYOUT_TYPE_GRID,
                    spanCount = 3)

            override val decorationConfig: DecorationConfig get() = DecorationConfig(
                    enableDrawSpacing = false)

            override val spacing get() = Spacing(
                    horizontal = resources.dpToPxInt(16),
                    vertical = resources.dpToPxInt(8),
                    edges = Rect(
                            resources.dpToPxInt(16),
                            resources.dpToPxInt(8),
                            resources.dpToPxInt(16),
                            resources.dpToPxInt(8)))

            override val itemCount get() = 11
        }
    }
}

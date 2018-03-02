package com.grzegorzojdana.spacingitemdecorationapp.model

import com.grzegorzojdana.spacingitemdecoration.Spacing

/**
 * To provide list data.
 */
interface ListDataProvider {
    val listLayoutConfig: ListLayoutConfig
    val decorationConfig: DecorationConfig
    val spacing: Spacing
    val itemCount: Int
}

/**
 * Provides objects created by its default constructors, and `0` item count.
 */
class EmptyListDataProvider : ListDataProvider {
    override val listLayoutConfig: ListLayoutConfig get() = ListLayoutConfig()
    override val decorationConfig: DecorationConfig get() = DecorationConfig()
    override val spacing: Spacing get() = Spacing()
    override val itemCount: Int get() = 0
}

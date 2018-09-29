package com.grzegorzojdana.spacingitemdecorationapp.util

import androidx.lifecycle.Observer

/**
 * [Observer] implementation that will ignore changes value to `null`.
 */
class NullIgnoreObserver<T>(
        private val onChangedToNotNull: (value: T) -> Unit
) : Observer<T> {
    override fun onChanged(value: T?) {
        if (value != null) onChangedToNotNull(value)
    }
}
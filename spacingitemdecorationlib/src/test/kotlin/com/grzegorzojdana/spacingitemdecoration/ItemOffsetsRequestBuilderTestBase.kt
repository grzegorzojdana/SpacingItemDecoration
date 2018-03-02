package com.grzegorzojdana.spacingitemdecoration

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
abstract class ItemOffsetsRequestBuilderTestBase(
        protected val offsetsRequestBuilder: ItemOffsetsRequestBuilder = ItemOffsetsRequestBuilder()
) {

    protected fun test(itemOffsetsParams: ItemOffsetsRequestBuilder.ItemOffsetsParams,
                       expectedResult: ItemOffsetsCalculator.OffsetsRequest) {
        val result = ItemOffsetsCalculator.OffsetsRequest()
        offsetsRequestBuilder.fillItemOffsetsRequest(itemOffsetsParams, result)
        assertEquals(expectedResult, result)
    }
}
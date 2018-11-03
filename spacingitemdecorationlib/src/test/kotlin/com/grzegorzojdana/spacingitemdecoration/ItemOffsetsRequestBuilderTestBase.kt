package com.grzegorzojdana.spacingitemdecoration

import com.grzegorzojdana.spacingitemdecoration.ItemOffsetsCalculator.OffsetsRequest
import com.grzegorzojdana.spacingitemdecoration.ItemOffsetsRequestBuilder.ItemOffsetsParams
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
abstract class ItemOffsetsRequestBuilderTestBase(
        protected val offsetsRequestBuilder: ItemOffsetsRequestBuilder = ItemOffsetsRequestBuilder()
) {

    protected fun givenParams(params: ItemOffsetsParams,
                              contextBlock: ParamsContext.() -> Unit): ParamsContext {
        return ParamsContext(
                offsetsParams = params,
                offsetsRequestBuilder = offsetsRequestBuilder).apply(contextBlock)
    }

    protected class ParamsContext(val offsetsParams: ItemOffsetsParams,
                                  val offsetsRequestBuilder: ItemOffsetsRequestBuilder) {
        fun on(paramsConditions: ItemOffsetsParams.() -> Unit): ParamsContext {
            offsetsParams.paramsConditions()
            return this
        }

        infix fun expects(expected: OffsetsRequest) {
            val result = OffsetsRequest()
            offsetsRequestBuilder.fillItemOffsetsRequest(offsetsParams, result)
            assertEquals(expected, result)
        }
    }
}
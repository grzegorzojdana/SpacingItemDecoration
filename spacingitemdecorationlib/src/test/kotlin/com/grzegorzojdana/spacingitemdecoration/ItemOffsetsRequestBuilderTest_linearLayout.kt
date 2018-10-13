package com.grzegorzojdana.spacingitemdecoration

import com.grzegorzojdana.spacingitemdecoration.ItemOffsetsCalculator.OffsetsRequest
import com.grzegorzojdana.spacingitemdecoration.ItemOffsetsRequestBuilder.ItemOffsetsParams
import org.junit.Test

class ItemOffsetsRequestBuilderTest_linearLayout: ItemOffsetsRequestBuilderTestBase() {

    @Test
    fun vertical() {
        val params = ItemOffsetsParams(
                spanIndex = 0, spanSize = 1,
                spanCount = 1, groupCount = 3,
                isLayoutVertical = true, isLayoutReverse = false)
        val expectedResult = OffsetsRequest(
                spanSizeH = 1, spanSizeV = 1, rows = 3, cols = 1)

        givenParams(params) {
            on { groupIndex = 0 } expects expectedResult.apply { row = 0; col = 0 }
            on { groupIndex = 1 } expects expectedResult.apply { row = 1; col = 0 }
            on { groupIndex = 2 } expects expectedResult.apply { row = 2; col = 0 }
        }
    }

    @Test
    fun horizontal() {
        val params = ItemOffsetsParams(
                spanIndex = 0, spanSize = 1,
                spanCount = 1, groupCount = 3,
                isLayoutVertical = false, isLayoutReverse = false)
        val expectedResult = OffsetsRequest(
                spanSizeH = 1, spanSizeV = 1, rows = 1, cols = 3)

        givenParams(params) {
            on { groupIndex = 0 } expects expectedResult.apply { row = 0; col = 0 }
            on { groupIndex = 1 } expects expectedResult.apply { row = 0; col = 1 }
            on { groupIndex = 2 } expects expectedResult.apply { row = 0; col = 2 }
        }
    }

    @Test
    fun verticalReverse() {
        val params = ItemOffsetsParams(
                spanIndex = 0, spanSize = 1,
                spanCount = 1, groupCount = 3,
                isLayoutVertical = true, isLayoutReverse = true)
        val expectedResult = OffsetsRequest(
                spanSizeH = 1, spanSizeV = 1, rows = 3, cols = 1)

        givenParams(params) {
            on { groupIndex = 0 } expects expectedResult.apply { row = 2; col = 0 }
            on { groupIndex = 1 } expects expectedResult.apply { row = 1; col = 0 }
            on { groupIndex = 2 } expects expectedResult.apply { row = 0; col = 0 }
        }
    }

    @Test
    fun horizontalReverse() {
        val params = ItemOffsetsParams(
                spanIndex = 0, spanSize = 1,
                spanCount = 1, groupCount = 3,
                isLayoutVertical = false, isLayoutReverse = true)
        val expectedResult = OffsetsRequest(
                spanSizeH = 1, spanSizeV = 1, rows = 1, cols = 3)

        givenParams(params) {
            on { groupIndex = 0 } expects expectedResult.apply { row = 0; col = 2 }
            on { groupIndex = 1 } expects expectedResult.apply { row = 0; col = 1 }
            on { groupIndex = 2 } expects expectedResult.apply { row = 0; col = 0 }
        }
    }

}
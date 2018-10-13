package com.grzegorzojdana.spacingitemdecoration

import com.grzegorzojdana.spacingitemdecoration.ItemOffsetsCalculator.OffsetsRequest
import com.grzegorzojdana.spacingitemdecoration.ItemOffsetsRequestBuilder.ItemOffsetsParams
import org.junit.Test

class ItemOffsetsRequestBuilderTest_gridLayout: ItemOffsetsRequestBuilderTestBase() {

    @Test
    fun vertical() {
        val params = ItemOffsetsParams(
                spanSize = 1, spanCount = 3, groupCount = 2,
                isLayoutVertical = true, isLayoutReverse = false)
        val expectedResult = OffsetsRequest(
                spanSizeH = 1, spanSizeV = 1, rows = 2, cols = 3)

        givenParams(params) {
            on { groupIndex = 0; spanIndex = 0 } expects expectedResult.apply { row = 0; col = 0 }
            on { groupIndex = 0; spanIndex = 1 } expects expectedResult.apply { row = 0; col = 1 }
            on { groupIndex = 0; spanIndex = 2 } expects expectedResult.apply { row = 0; col = 2 }
            on { groupIndex = 1; spanIndex = 0 } expects expectedResult.apply { row = 1; col = 0 }
            on { groupIndex = 1; spanIndex = 1 } expects expectedResult.apply { row = 1; col = 1 }
            on { groupIndex = 1; spanIndex = 2 } expects expectedResult.apply { row = 1; col = 2 }
        }
    }

    @Test
    fun horizontal() {
        val params = ItemOffsetsParams(
                spanSize = 1, spanCount = 3, groupCount = 2,
                isLayoutVertical = false, isLayoutReverse = false)
        val expectedResult = OffsetsRequest(
                spanSizeH = 1, spanSizeV = 1, rows = 3, cols = 2)

        givenParams(params) {
            on { groupIndex = 0; spanIndex = 0 } expects expectedResult.apply { row = 0; col = 0 }
            on { groupIndex = 0; spanIndex = 1 } expects expectedResult.apply { row = 1; col = 0 }
            on { groupIndex = 0; spanIndex = 2 } expects expectedResult.apply { row = 2; col = 0 }
            on { groupIndex = 1; spanIndex = 0 } expects expectedResult.apply { row = 0; col = 1 }
            on { groupIndex = 1; spanIndex = 1 } expects expectedResult.apply { row = 1; col = 1 }
            on { groupIndex = 1; spanIndex = 2 } expects expectedResult.apply { row = 2; col = 1 }
        }
    }

    @Test
    fun verticalReverse() {
        val params = ItemOffsetsParams(
                spanSize = 1, spanCount = 3, groupCount = 2,
                isLayoutVertical = true, isLayoutReverse = true)
        val expectedResult = OffsetsRequest(
                spanSizeH = 1, spanSizeV = 1, rows = 2, cols = 3)

        givenParams(params) {
            on { groupIndex = 0; spanIndex = 0 } expects expectedResult.apply { row = 1; col = 0 }
            on { groupIndex = 0; spanIndex = 1 } expects expectedResult.apply { row = 1; col = 1 }
            on { groupIndex = 0; spanIndex = 2 } expects expectedResult.apply { row = 1; col = 2 }
            on { groupIndex = 1; spanIndex = 0 } expects expectedResult.apply { row = 0; col = 0 }
            on { groupIndex = 1; spanIndex = 1 } expects expectedResult.apply { row = 0; col = 1 }
            on { groupIndex = 1; spanIndex = 2 } expects expectedResult.apply { row = 0; col = 2 }
        }
    }

    @Test
    fun horizontalReverse() {
        val params = ItemOffsetsParams(
                spanSize = 1, spanCount = 3, groupCount = 2,
                isLayoutVertical = false, isLayoutReverse = true)
        val expectedResult = OffsetsRequest(
                spanSizeH = 1, spanSizeV = 1, rows = 3, cols = 2)

        givenParams(params) {
            on { groupIndex = 0; spanIndex = 0 } expects expectedResult.apply { row = 0; col = 1 }
            on { groupIndex = 0; spanIndex = 1 } expects expectedResult.apply { row = 1; col = 1 }
            on { groupIndex = 0; spanIndex = 2 } expects expectedResult.apply { row = 2; col = 1 }
            on { groupIndex = 1; spanIndex = 0 } expects expectedResult.apply { row = 0; col = 0 }
            on { groupIndex = 1; spanIndex = 1 } expects expectedResult.apply { row = 1; col = 0 }
            on { groupIndex = 1; spanIndex = 2 } expects expectedResult.apply { row = 2; col = 0 }
        }
    }

}
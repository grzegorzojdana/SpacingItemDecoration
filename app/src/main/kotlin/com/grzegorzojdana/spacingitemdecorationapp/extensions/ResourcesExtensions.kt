package com.grzegorzojdana.spacingitemdecorationapp.extensions

import android.content.res.Resources


fun Resources.dpToPx(dp: Float): Float {
    return dp * displayMetrics.density
}
fun Resources.dpToPxInt(dp: Int): Int {
    return (dp * displayMetrics.density).toInt()
}

fun Resources.pxToDp(px: Float): Float {
    return px / displayMetrics.density
}

fun Resources.dimenPx(dimenResId: Int): Int = getDimensionPixelSize(dimenResId)
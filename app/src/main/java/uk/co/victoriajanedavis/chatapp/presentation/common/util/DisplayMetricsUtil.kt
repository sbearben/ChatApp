package uk.co.victoriajanedavis.chatapp.presentation.common.util

import android.content.res.Resources

private const val SCREEN_TABLET_DP_WIDTH = 600

val isTabletLayout: Boolean
    get() = isScreenW(SCREEN_TABLET_DP_WIDTH)

// Return true if the width in DP of the device is equal or greater than the given value
private fun isScreenW(widthDp: Int): Boolean {
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    return screenWidth >= widthDp
}
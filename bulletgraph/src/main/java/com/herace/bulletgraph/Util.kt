package com.herace.bulletgraph

import android.content.res.Resources

object Util {
    fun dpToPx(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }
}

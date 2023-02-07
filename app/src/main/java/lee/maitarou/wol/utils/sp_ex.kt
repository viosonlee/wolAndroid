package lee.maitarou.wol.utils

import android.content.Context
import android.content.SharedPreferences

/**
 *Author:viosonlee
 *Date:2023/2/7
 *DESCRIPTION:
 */
fun Context.sp(block: (SharedPreferences.() -> Unit)? = null): SharedPreferences {
    val preferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
    block?.invoke(preferences)
    return preferences
}
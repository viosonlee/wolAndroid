package lee.maitarou.wol

import android.util.Base64


/**
 *Author:viosonlee
 *Date:2023/2/7
 *DESCRIPTION:
 */


val String.alias: String
    get() = this.replace(":", "_").replace("-","_")

package lee.maitarou.wol.push

import android.content.Context
import android.content.Intent
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.service.JPushMessageReceiver
import lee.maitarou.wol.wol.WolSender
import lee.maitarou.wol.wol.WolService

/**
 *Author:viosonlee
 *Date:2023/2/7
 *DESCRIPTION:
 */
class JPushReceiver : JPushMessageReceiver() {
    override fun onMessage(context: Context?, message: CustomMessage?) {
        super.onMessage(context, message)
        context?.startService(Intent(context, WolService::class.java))
    }

    override fun onAliasOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onAliasOperatorResult(p0, p1)
        if (p1?.sequence == WolSender.setTagOptionID) {
            WolSender.onSetTag(p1.errorCode == 0)
        }
    }
}
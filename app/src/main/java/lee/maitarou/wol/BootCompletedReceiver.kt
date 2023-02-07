package lee.maitarou.wol

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import lee.maitarou.wol.MainActivity

/**
 * 开机自启
 */
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }
}
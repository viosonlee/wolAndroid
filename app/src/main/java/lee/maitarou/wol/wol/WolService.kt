package lee.maitarou.wol.wol

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

/**
 *Author:viosonlee
 *Date:2023/2/7
 *DESCRIPTION:
 */
class WolService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        WolSender.sendWol(this) { _, msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
package lee.maitarou.wol.wol

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import lee.maitarou.wol.R

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
        //保活通知

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("WolService", "WolService Notification", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannel(notificationChannel)
            Notification.Builder(this, "WolService")
        } else {
            Notification.Builder(this)
        }
        startForeground(
            9,
            builder
                .setContentText("wol保活服务")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build()
        )
        return super.onStartCommand(intent, flags, startId)
    }
}
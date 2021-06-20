package wakeuprightnow.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.*
import android.os.Build
import android.widget.Toast
import wakeuprightnow.compassmode.CompassModeActivity
import wakeuprightnow.runningmode.RunningModeActivity
import wakeuprightnow.whacmolemode.WhacAMoleActivity
import wakeuprightnow.riddlemode.RiddleModeActivity

import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val c: Calendar = Calendar.getInstance()
        c.setTimeInMillis(System.currentTimeMillis())
        Toast.makeText(context, "現在是 " + c.get(Calendar.HOUR_OF_DAY) + " 點 " + c.get(Calendar.MINUTE) + " 分",
            Toast.LENGTH_LONG).show()
        sendNotification(context,"點擊來關閉鬧鐘")
        AudioPlay.playAudio(context, MainActivity.clock_song)
    }

    /* 通知 */
    private fun sendNotification(context: Context, msg: String) {
        val intent = Intent()
        when(MainActivity.clock_mode){
            0 ->{
                intent.setClass(context, RiddleModeActivity::class.java!!)
            }
            1 ->{
                intent.setClass(context, RunningModeActivity::class.java!!)
            }
            2 ->{
                intent.setClass(context, WhacAMoleActivity::class.java!!)
            }
            3 ->{
                intent.setClass(context, CompassModeActivity::class.java!!)
            }
        }
        intent.putExtra("EXTRA_MSG", msg)

        val pi = PendingIntent.getActivity(context,
            0, intent, 0)

        var notification: Notification? = null
        try {
            notification = getNotification(context, pi,
                context.getString(R.string.app_name), msg)
        } catch (e: Exception) {
        }
        if (notification != null) {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager
            notificationManager.notify(1, notification)
        }
    }

    companion object {
        val CHANNEL_ID = "wakeuprightnow.alarmclock"
    }

    private fun getNotification(context: Context, pi: PendingIntent,
                                title: String, msg: String): Notification? {

        var notification: Notification? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val channel = NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW)
                channel.setShowBadge(false)
                val notificationManager: NotificationManager =
                    context.getSystemService(NotificationManager::class.java)
                notificationManager!!.createNotificationChannel(channel)
                notification = Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContentIntent(pi)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(msg)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .build()
            } else if (Build.VERSION.SDK_INT >= 16){
                notification = Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContentIntent(pi)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(msg)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .build()
            }
        } catch (throwable: Throwable) {
            return null
        }
        return notification
    }

}
package wakeuprightnow.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import java.util.*

class Util {

    companion object {

        fun setPendingIntent(context: Context): PendingIntent {
            var intent: Intent = Intent()
            intent.setClass(context, AlarmReceiver::class.java)
            val pendingintent = PendingIntent.getBroadcast(
                context, 0, intent, 0);
            return pendingintent
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun setNextAlarm(context: Context, am: AlarmManager?,
                         pi: PendingIntent?, tpk:TimePicker) {
            //取得現在時間
            var c: Calendar = Calendar.getInstance()
            c.setTimeInMillis(System.currentTimeMillis());

            //若時間已過，設成隔天的鬧鐘
            if(tpk.hour < c.get(Calendar.HOUR_OF_DAY))
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1)
            else if(tpk.hour == c.get(Calendar.HOUR_OF_DAY)){
                if(tpk.minute < c.get(Calendar.MINUTE))
                    c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1)
            }
            c.set(Calendar.HOUR_OF_DAY, tpk.hour);
            c.set(Calendar.MINUTE,tpk.minute);
            c.set(Calendar.SECOND,0);
            c.set(Calendar.MILLISECOND,0);

            //設定broadcast
            if (Build.VERSION.SDK_INT >= 19) {
                // setWindow
                am?.setExact(AlarmManager.RTC_WAKEUP,
                    c.getTimeInMillis(), pi)
            } else {
                // setRepeating
                am?.set(AlarmManager.RTC_WAKEUP,
                    c.getTimeInMillis(), pi)
            }
        }
    }
}
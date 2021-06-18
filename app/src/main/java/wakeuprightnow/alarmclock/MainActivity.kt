package wakeuprightnow.alarmclock


import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    var pendingintent: PendingIntent? = null
    var am: AlarmManager? = null

    companion object {
        val MODE_ID: String = "MODE_ID"
        val SONG_ID: String = "SONG_ID"
        var clock_mode : Int = 0
        var clock_song : Int = R.raw.fast_and_run
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_set_time: Button = findViewById(R.id.btn_setAlarmTime)
        val btn_set_mode: Button = findViewById(R.id.btn_setAlarmMode)
        val btn_set_music: Button = findViewById(R.id.btn_setAlarmMusic)

        val tpk: TimePicker = findViewById(R.id.tpk)

        btn_set_time.setOnClickListener({ setAlarmTime(tpk) })
        btn_set_mode.setOnClickListener({ setAlarmMode() })
        btn_set_music.setOnClickListener({ setAlarmMusic() })

        pendingintent = Util.setPendingIntent(this)
        am = getSystemService(Context.ALARM_SERVICE) as?
                AlarmManager?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode === RESULT_OK && data != null) {
                clock_mode = data?.getIntExtra(MODE_ID,0)
            }
            2 -> if (resultCode === RESULT_OK && data != null) {
                clock_song = data?.getIntExtra(SONG_ID,R.raw.fast_and_run)
            }
        }
    }

    private fun setAlarmTime(tpk: TimePicker) {
        Util.setNextAlarm(this, am, pendingintent, tpk)
        /* 測試鬧鐘設定時間，實際設定在Util
        var c: Calendar = Calendar.getInstance()
        c.setTimeInMillis(System.currentTimeMillis());
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
        var msg = "鬧鐘將在" + c.get(Calendar.DAY_OF_MONTH) + "日" +
                  c.get(Calendar.HOUR_OF_DAY) + " : " + c.get(Calendar.MINUTE) +" 啟動"
         */
        var msg = "鬧鐘將在下次的 " + tpk.hour + " : " + tpk.minute + " 啟動"
        Toast.makeText(this, msg,
            Toast.LENGTH_SHORT).show();
    }

    private fun setAlarmMode() {
        val intent = Intent()
        intent.setClass(this, SetModeActivity::class.java)
        startActivityForResult(intent, 1);
    }

    private fun setAlarmMusic() {
        val intent = Intent()
        intent.setClass(this, SetMusicActivity::class.java)
        startActivityForResult(intent, 2);
    }

}
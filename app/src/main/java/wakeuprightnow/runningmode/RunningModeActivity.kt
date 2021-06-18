package wakeuprightnow.runningmode

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import wakeuprightnow.alarmclock.MainActivity
import wakeuprightnow.alarmclock.R

class RunningModeActivity : AppCompatActivity(), LocationListener {
    lateinit var locmgr: LocationManager
    lateinit var tv_initLoc: TextView
    lateinit var tv_currentLoc: TextView
    lateinit var tv_distance: TextView
    lateinit var btn_closeAlarm: Button
    lateinit var player: MediaPlayer

    var initloc: Location? = null
    var distance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_mode)
        tv_initLoc = findViewById(R.id.tv_initLoc)
        tv_currentLoc = findViewById(R.id.tv_currentLoc)
        tv_distance = findViewById(R.id.tv_distance)
        btn_closeAlarm = findViewById(R.id.btn_closeAlarm)
        btn_closeAlarm.setOnClickListener { closeAlarm() }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                1)
        } else {
            //初始化位置、播放鬧鐘音樂
            initLoc()
            initMusic()
            player.start()
        }

    }
    private fun initMusic() {
        try {
            player = MediaPlayer.create(this,
                MainActivity.clock_song)

            player.setOnCompletionListener {
                try {
                    player?.stop()
                    player?.prepare()
                    player?.start()
                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
        }
    }



    private fun initLoc() {
        locmgr = getSystemService(LOCATION_SERVICE) as
                LocationManager

        var loc: Location? = null
        try {
            loc = locmgr.getLastKnownLocation(
                LocationManager.GPS_PROVIDER)
            if (loc == null) {
                loc = locmgr.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER)

            }
        } catch (e: SecurityException) {
        }

        if (loc != null) {
            initloc = loc
            tv_initLoc.text = "("  + "%.3f".format(loc.latitude) + "," + "%.3f".format(loc.longitude) + ")"
        } else {
            tv_initLoc.text = "Cannot get location!"
        }

        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        val provider: String? = locmgr.getBestProvider(
            criteria, true)

        try {
            if (provider != null) {
                locmgr.requestLocationUpdates(provider,
                    1000, 1f, this)
            } else {
                locmgr.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000, 1f, this)
            }
        } catch (e: SecurityException) {
        }
    }
    private fun closeAlarm(){
        if(distance > 100){
            finish()
        }
        else{
            var msg= "還差: " + "%.3f".format(100 -distance) + " m"
            Toast.makeText(this, msg,
                Toast.LENGTH_SHORT).show();
        }
    }

    //關閉鬧鐘釋放資源
    override fun onStop() {
        super.onStop()
        player.stop()
        locmgr.removeUpdates(this)
        player.release()

    }

    override fun onLocationChanged(loc: Location) {
        tv_currentLoc.text = "(" + "%.3f".format(loc.latitude) + "," + "%.3f".format(loc.longitude) + ")"
        distance = loc.distanceTo(initloc).toDouble()
        tv_distance.text = ""  + "%.3f".format(distance) + " m"

    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }
}
package wakeuprightnow.compassmode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.Gravity
import android.view.Surface
import android.view.ViewGroup
import android.widget.*
import wakeuprightnow.alarmclock.MainActivity


class CompassModeActivity : AppCompatActivity(), SensorEventListener{
    lateinit var smgr: SensorManager
    lateinit var accel: Sensor
    lateinit var compass: Sensor
    lateinit var player: MediaPlayer

    var baccel: Boolean = false
    var bcompass: Boolean = false
    var accelValues = FloatArray(3, {0.0f})
    var compassValues = FloatArray(3, {0.0f})
    var rotationMatrix = FloatArray(9, {0.0f})
    var values = FloatArray(3, {0.0f})
    var azimuth: Float = 0.0f
    var targetString = ""
    var currentString = "_ _ _ _ _ _ _ _"
    var currentIndex = 0
    var target = floatArrayOf(0f, 324f, 288f, 252f, 216f, 180f, 144f, 108f, 72f, 36f)
    var stringArray = targetString.map { it.toString() }.toTypedArray()
    var isOver = false

    lateinit var btn: Button
    lateinit var  tv: TextView
    lateinit var arrow: ArrowView
    lateinit var  params: ViewGroup.LayoutParams
    lateinit var params2: FrameLayout.LayoutParams
    lateinit var  displayy: Display

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        generateTargetString()
        currentIndex = 0

        val metrics = DisplayMetrics()
        displayy = windowManager.defaultDisplay
        displayy.getMetrics(metrics)
        arrow = ArrowView(this, metrics.widthPixels, metrics.heightPixels)

        tv = TextView(this)
        tv.setTextColor(Color.BLACK)
        tv.textSize = 21.0f
        params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        params2 = FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params2.gravity = Gravity.BOTTOM

        btn = Button(this)
        btn.text = "關閉鬧鐘"
        btn.setTextColor(Color.BLACK)

        btn.isEnabled = false
        btn.isClickable = false
        btn.setBackgroundColor(Color.parseColor("#ADB5BD"))
        btn.setTextColor(Color.WHITE)

        setContentView(arrow)
        addContentView(tv, params)

        baccel = false
        bcompass = false
        smgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accel = smgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        compass = smgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        initMusic()
        player.start()

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

    override fun onResume(){
        super.onResume()
        smgr.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        smgr.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL)
    }


    override fun onStop() {
        super.onStop()
        player.stop()
        smgr.unregisterListener(this, accel)
        smgr.unregisterListener(this, compass)
        player.release()

    }
    override fun onSensorChanged(event: SensorEvent){
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            for(i in 0..2){
                accelValues[i] = event.values[i]
            }
            baccel = true
        } else if(event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
            for(i in 0..2){
                compassValues[i] = event.values[i]
            }
            bcompass = true
        }

        if(!baccel || !bcompass){
            return
        }

        if(SensorManager.getRotationMatrix(rotationMatrix, null, accelValues, compassValues)){
            var rotation = displayy.rotation
            if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrix)
            }
            SensorManager.getOrientation(rotationMatrix, values)
            update()
            baccel = false
            bcompass = false
        }
    }

    fun generateTargetString(){
        for(i in 0..7) {
            var num = (0..9).random().toString()
            if(i!=0){
                while(num == targetString[i-1].toString()){
                    num = (0..9).random().toString()
                }
            }
            targetString = targetString + num.toString()
            stringArray = targetString.map { it.toString() }.toTypedArray()
        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int){

    }

    fun inRange(degree: Float, target: Float): Boolean{
        if(target - 10f < 0f){
            return degree > target - 10f + 360f || degree < target + 10f
        }
        else if(target + 10f > 360f){
            return degree > target - 10f || degree < target + 10f - 360f
        }
        else{
            return target + 10f > degree && target - 10f < degree
        }

    }

    private fun update(){
        azimuth = Math.toDegrees(values[0].toDouble()).toFloat()
        if(azimuth < 0) azimuth += 360.0f

        var degree: Float = 0.0f - azimuth
        if (degree < 0) degree += 360.0f
        arrow.degree = degree
        setContentView(arrow)




        var pointNum = 0

        for(i in 0..9){
            if(inRange(azimuth, target[i])){
                pointNum = i
                arrow.pointNum = pointNum
            }
        }

        if(currentIndex < targetString.length){
            if(inRange(azimuth, target[stringArray[currentIndex].toInt()])) {
                val chars = currentString.toCharArray()
                chars[currentIndex * 2] = stringArray[currentIndex].toCharArray()[0]
                currentString = String(chars)
                /*currentString = currentString + stringArray[currentIndex]*/
                currentIndex = currentIndex + 1

            }

            if(currentIndex==targetString.length){
                overFunction()
            }

        }


        tv.text = "請轉動手機\n讓指針依序指到" + targetString + "來關閉鬧鐘\n" + "!!!記得讓手機維持平放!!!" + "\n目前數字: " + currentString

        btn.layoutParams = params2
        addContentView(tv, params)
        addContentView(btn, params2)
    }

    fun overFunction(){
        isOver = true
        btn.isEnabled = true
        btn.isClickable = true
        btn.setBackgroundColor(Color.parseColor("#52B69A"))
        btn.setTextColor(Color.BLACK)
        btn.setOnClickListener{
            Log.d("8787", "clickkkkkkk")
            onStop()
        }
        //關鬧鐘！！！！！

    }

}
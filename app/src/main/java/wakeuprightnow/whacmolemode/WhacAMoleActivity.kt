package wakeuprightnow.whacmolemode

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import wakeuprightnow.alarmclock.MainActivity
import wakeuprightnow.alarmclock.R
import wakeuprightnow.alarmclock.AudioPlay

class WhacAMoleActivity : AppCompatActivity() {
    //alarm items
    lateinit var player: MediaPlayer
    //layout items
    private val BUTTON_IDS = arrayOf(R.id.hole_1, R.id.hole_2, R.id.hole_3, R.id.hole_4, R.id.hole_5, R.id.hole_6, R.id.hole_7, R.id.hole_8, R.id.hole_9, R.id.hole_10, R.id.hole_11, R.id.hole_12, R.id.hole_13, R.id.hole_14, R.id.hole_15, R.id.hole_16)
    private lateinit var handler : Handler
    private lateinit var runnable : Runnable
    private lateinit var gameFrame : LinearLayout
    private lateinit var scoreBoard : TextView
    private lateinit var btn_shutdown_alarm : Button

    //game data
    private lateinit var holes : ArrayList<ImageButton>
    private var goal = 25
    private var mode = 3
    private var nextHole = 1
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mole_choose_level)
        val btn_easy = findViewById<Button>(R.id.mode_easy)
        val btn_hard = findViewById<Button>(R.id.mode_hard)
        btn_easy.setOnClickListener(ModeSwitch())
        btn_hard.setOnClickListener(ModeSwitch())
    }

    inner class ModeSwitch() : View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.mode_easy -> {
                    mode = 3
                    goal = 25
                }
                R.id.mode_hard -> {
                    mode = 4
                    goal = 50
                }
            }
            setContentView(R.layout.activity_whac_a_mole_mode)
            gameFrame = findViewById(R.id.frame_mole_game)
            scoreBoard = findViewById(R.id.score)
            btn_shutdown_alarm = findViewById(R.id.btn_shut_down)
            btn_shutdown_alarm.setOnClickListener {
                finish()
                AudioPlay.stopAudio()
            }
            holes = ArrayList()
            initBtn()
            initHandler()
        }
    }

    inner class HamsterClick() : View.OnClickListener{
        override fun onClick(view: View?){
            var hitHole = -1
            when(view?.id){
                R.id.hole_1 -> { hitHole = 0 }
                R.id.hole_2 -> { hitHole = 1 }
                R.id.hole_3 -> { hitHole = 2 }
                R.id.hole_4 -> { hitHole = 3 }
                R.id.hole_5 -> { hitHole = 4 }
                R.id.hole_6 -> { hitHole = 5 }
                R.id.hole_7 -> { hitHole = 6 }
                R.id.hole_8 -> { hitHole = 7 }
                R.id.hole_9 -> { hitHole = 8 }
                R.id.hole_10 -> { hitHole = 9 }
                R.id.hole_11 -> { hitHole = 10 }
                R.id.hole_12 -> { hitHole = 11 }
                R.id.hole_13 -> { hitHole = 12 }
                R.id.hole_14 -> { hitHole = 13 }
                R.id.hole_15 -> { hitHole = 14 }
                R.id.hole_16 -> { hitHole = 15 }
            }

            if( hitHole == nextHole ){
                score++
                scoreBoard.setText(score.toString() + " / " + goal.toString())
                holes[hitHole].setBackgroundResource(R.drawable.box)
                nextHole = -1
            }

        }
    }

    private fun initBtn(){
        var holeSize = 0
        when(mode){
            3 -> { holeSize = resources.getDimension(R.dimen.hole_size_easy).toInt() }
            4 -> { holeSize = resources.getDimension(R.dimen.hole_size_hard).toInt() }
            else -> { holeSize = resources.getDimension(R.dimen.hole_size_easy).toInt() }
        }
        for(i in 0..mode-1){
            var currFrame = LinearLayout(this)
            currFrame.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            currFrame.orientation = LinearLayout.HORIZONTAL
            currFrame.gravity = Gravity.CENTER
            gameFrame.addView(currFrame)
            for(j in 0..mode-1){
                var newHole = ImageButton(this)
                newHole.id = BUTTON_IDS[i*3+j]
                var param = LinearLayout.LayoutParams(holeSize, holeSize)
                param.setMargins(10,10,10,10)
                newHole.layoutParams = param
                newHole.setBackgroundResource(R.drawable.box)
                currFrame.addView(newHole)
                holes.add(newHole)
            }
        }

        for(btn in holes){
            btn.setOnClickListener(HamsterClick())
        }
    }

    private fun initHandler(){
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            if(finishGame()){
                scoreBoard.setText(R.string.finish_game)
                btn_shutdown_alarm.isEnabled = true
                handler.removeCallbacksAndMessages(null)
            }
            else{
                nextHole = (0..8).random()
                for(btn in holes){
                    btn.setBackgroundResource(R.drawable.box)
                }
                holes[nextHole].setBackgroundResource(R.drawable.hamster)
                val sec = (300..1500).random()
                handler.postDelayed(runnable, sec.toLong())

            }
        }
        handler.post(runnable)
    }

    private fun finishGame(): Boolean {
        if(score < 25){ return false }
        else{ return true }
    }
}
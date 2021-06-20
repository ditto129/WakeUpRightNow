package wakeuprightnow.riddlemode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import wakeuprightnow.alarmclock.R
import wakeuprightnow.alarmclock.AudioPlay

class RiddleModeActivity : AppCompatActivity() {

    // lateinit var tv_timeLimit: TextView
    lateinit var tv_correctAnswer: TextView
    lateinit var tv_question: TextView
    lateinit var et_answer: EditText
    lateinit var btn_answer: Button
    lateinit var btn_closeAlarm: Button
    lateinit var questionArrayList: ArrayList<Question>
    // lateinit var countDownTimer: CountDownTimer
    var answerQuestionNum: Int = 3
    var correctAnswer: Int = 0
    var randomQuestionNo: Int = (0..9).random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riddle_mode)

        // tv_timeLimit = findViewById(R.id.timeLimit)
        tv_correctAnswer = findViewById(R.id.correctQuestionAttempted)
        tv_question = findViewById(R.id.question)
        et_answer = findViewById(R.id.editAnswer)
        btn_answer = findViewById(R.id.answerQuestion)
        btn_answer.setOnClickListener{ checkAnswer(randomQuestionNo) }
        btn_closeAlarm = findViewById(R.id.closeAlarm)
        btn_closeAlarm.setOnClickListener{ closeAlarm() }

        questionArrayList = arrayListOf()
        getQuestionArrayList(questionArrayList)
        setQuestionToView(randomQuestionNo)
        // initCountDownTimer()
    }

    /*
    private fun initCountDownTimer() {
        countDownTimer = object: CountDownTimer(180000,1000) {
            override fun onTick(millisUntilFinished: Long) {
                //countDownSecond = (millisUntilFinished/1000).toString()
                tv_timeLimit.setText("限時" + (millisUntilFinished/1000).toString() + "秒內回答正確答案")
            }
            override fun onFinish() {
                showResult()
            }
        }
        countDownTimer.start()
    }*/

    private fun checkAnswer(questionNo: Int) {
        // 檢查答案正確與否
        if (questionArrayList.get(questionNo).answer == et_answer.text.toString()) {
            correctAnswer += 1
            Toast.makeText(this, "答對了！", Toast.LENGTH_SHORT).show()
        } else {
            answerQuestionNum += 1
            Toast.makeText(this, "答錯了！多加一題>w<", Toast.LENGTH_SHORT).show()
        }

        // 檢查回答正確題數是否達標
        if (correctAnswer == answerQuestionNum) {
            showResult()
        } else {
            randomQuestionNo = (0..29).random()
            setQuestionToView(randomQuestionNo)
        }
    }

    private fun closeAlarm() {
        finish()
        AudioPlay.stopAudio()
    }

    private fun showResult() {
        if (correctAnswer == answerQuestionNum) {
            tv_correctAnswer.setText("達標！")
            tv_question.setText("挑戰成功！")
        } else {
            tv_correctAnswer.setText("哎呀..還差一點！")
            tv_question.setText("挑戰失敗QQ")
        }
        btn_answer.isClickable = false
        btn_closeAlarm.isEnabled = true
    }

    private fun setQuestionToView(questionNo: Int) {
        // tv_timeLimit.setText("限時" + countDownSecond + "秒內正確回答" + answerQuestionNum.toString() + "個問題")
        tv_correctAnswer.setText("答對題數: " + correctAnswer.toString() + " / " + answerQuestionNum.toString())
        tv_question.setText(questionArrayList.get(questionNo).question)
    }

    private fun getQuestionArrayList(questionArrayList: ArrayList<Question>) {
        questionArrayList.add(Question("1+2+3+...+9+10=?","55"))
        questionArrayList.add(Question("小明帶503元去買198元的零食,店員會找他多少錢?","305"))
        questionArrayList.add(Question("99/9+9=?","20"))
        questionArrayList.add(Question("5x6x7=?","210"))
        questionArrayList.add(Question("原價550的衣服打85折後售價為?(四捨五入)","468"))
        questionArrayList.add(Question("171/3+171/9=?","76"))
        questionArrayList.add(Question("A<B,C>D,A=C,誰最大?","B"))
        questionArrayList.add(Question("5的三次方為?","125"))
        questionArrayList.add(Question("2sin(pi/2)=?","2"))
        questionArrayList.add(Question("4x4.5/3=?","6"))  // 10 數學
        questionArrayList.add(Question("大雄，你有90元，跟胖虎借10元後，你會有多少錢?(回答數字)","0"))
        questionArrayList.add(Question("3個人3天用3桶水，9個人9天用幾桶水?(回答數字)","9"))
        questionArrayList.add(Question("喜歡殺人叫殺人魔，喜歡逛costco叫(四字)?","好事多磨"))
        questionArrayList.add(Question("什麼布剪不斷?(兩字)","瀑布"))
        questionArrayList.add(Question("海獅獨處時會怎麼樣?(兩字)","害怕"))
        questionArrayList.add(Question("把大象裝進冰箱要幾步?(回答數字)","3"))
        questionArrayList.add(Question("誰生了約翰?(兩字)","花生"))
        questionArrayList.add(Question("綠豆跌倒後變成甚麼?(兩字)","紅豆"))
        questionArrayList.add(Question("士是誰殺的?(兩字)","黑松"))
        questionArrayList.add(Question("每天晚上十二點整要做什麼事?(三字)","抱佛腳"))  // 10 猜謎
        questionArrayList.add(Question("惡魔貓男是你今晚的?","惡夢"))
        questionArrayList.add(Question("甚麼東西能捶倒資本主義的高牆?","人民的法槌"))
        questionArrayList.add(Question("QQㄋㄟㄋㄟ好喝到咩噗茶是飲料?(四字)","珍珠奶茶"))
        questionArrayList.add(Question("填充:小孩子才做選擇，我_____!","全都要"))
        questionArrayList.add(Question("填充:你要不要吃_____?","哈密瓜"))
        questionArrayList.add(Question("填充:靠打架能解決問題嗎?要打去_____打!","練舞室"))
        questionArrayList.add(Question("誰攻擊珍妮佛羅培茲的村莊?","雪莉"))
        questionArrayList.add(Question("填充:前面有一隻超可愛的_____","狗勾"))
        questionArrayList.add(Question("填充:你為什麼不問問_____?","神奇海螺"))
        questionArrayList.add(Question("填充:欸幹!_____欸!嗚呼~這可以養嗎?","穿山甲"))  // 10 迷因
    }
}
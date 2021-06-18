package wakeuprightnow.alarmclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class SetModeActivity : AppCompatActivity() {
    var mode_id : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_mode)

        val modelist = ArrayList<ModeItem>()

        modelist.add(ModeItem("猜謎模式", 0))
        modelist.add(ModeItem("移動模式", 1))
        modelist.add(ModeItem("打地鼠模式", 2))
        modelist.add(ModeItem("指南針模式", 3))

        val listView: ListView = findViewById(R.id.lv_mode)

        listView.adapter = ModeArrayAdapter(this, modelist)

        listView.setOnItemClickListener{
                parent, view, position, id -> itemClicked(position)
        }
    }
    private fun itemClicked(position: Int) {
        when(position){
            0 -> {
                mode_id = 0
            }
            1 -> {
                mode_id = 1
            }
            2 -> {
                mode_id = 2
            }
            3 ->{
                mode_id = 3
            }
        }
        //跳回主頁面
        val intent = Intent()
        intent.setClass(this,MainActivity::class.java)
        intent.putExtra(MainActivity.MODE_ID, mode_id)
        setResult(RESULT_OK, intent);
        finish();
    }
}
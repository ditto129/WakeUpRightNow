package wakeuprightnow.alarmclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.ListView

class SetMusicActivity : AppCompatActivity() {

    var song_id : Int = R.raw.fast_and_run

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_music)

        val musiclist = ArrayList<MusicItem>()

        musiclist.add(MusicItem("Fast and Run - Nico Staf", R.raw.fast_and_run))
        musiclist.add(MusicItem("Calvin Harris - josh pan", R.raw.calvin_harris))
        musiclist.add(MusicItem("Yah Yah - josh pan", R.raw.yah_yah))

        val listView: ListView = findViewById(R.id.lv_music)

        listView.adapter = MusicArrayAdapter(this, musiclist)

        listView.setOnItemClickListener{
                parent, view, position, id -> itemClicked(position)
        }
    }
    private fun itemClicked(position: Int) {
        when(position){
            0 -> {
                song_id = R.raw.fast_and_run
            }
            1 -> {
                song_id = R.raw.calvin_harris
            }
            2 -> {
                song_id = R.raw.yah_yah
            }
        }

        //跳回主頁面
        val intent = Intent()
        intent.setClass(this,MainActivity::class.java)
        intent.putExtra(MainActivity.SONG_ID, song_id);
        setResult(RESULT_OK, intent);
        finish();
    }
}
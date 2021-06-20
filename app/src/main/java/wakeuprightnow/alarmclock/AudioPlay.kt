package wakeuprightnow.alarmclock

import android.content.Context
import android.media.MediaPlayer


object AudioPlay {
    var player: MediaPlayer? = null
    var lastResource: Int? = null

    fun playAudio(c: Context, id: Int, isLooping: Boolean = true) {
        createMediaPlayer(c, id)

        player?.let {
            it.isLooping = isLooping

            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    private fun createMediaPlayer(c: Context, id: Int) {
        player?.stop()
        player = MediaPlayer.create(c, id)
        lastResource = id
    }

    fun continuePlaying(c: Context, specificResource: Int? = null) {
        specificResource?.let {
            if (lastResource != specificResource) {
                createMediaPlayer(c, specificResource)
            }
        }

        player?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    fun pauseAudio() {
        player?.pause()
    }

    fun stopAudio(){
        player?.stop()
    }

}
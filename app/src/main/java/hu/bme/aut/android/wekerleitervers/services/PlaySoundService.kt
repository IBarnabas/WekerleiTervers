package hu.bme.aut.android.wekerleitervers.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import hu.bme.aut.android.wekerleitervers.R
import hu.bme.aut.android.wekerleitervers.activity.LocationDetailsActivity

class PlaySoundService : Service(), MediaPlayer.OnCompletionListener{
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.sound)
        mediaPlayer?.isLooping = false
        mediaPlayer?.setOnCompletionListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer?.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
    }

    override fun onCompletion(p0: MediaPlayer?) {
        this.stopSelf()
        LocationDetailsActivity.isPlaying = false
    }

}
package hu.bme.aut.android.wekerleitervers.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import hu.bme.aut.android.wekerleitervers.R
import hu.bme.aut.android.wekerleitervers.databinding.ActivityProjectDetailsBinding
import hu.bme.aut.android.wekerleitervers.navview.NavView

class DetailsProjectActivity : YouTubeBaseActivity() {
    private lateinit var binding: ActivityProjectDetailsBinding
    private val videoUrlId = "3yst8LTE9Qo"
    private lateinit var drawer: DrawerLayout
    private lateinit var navigation: NavView
    private lateinit var player: YouTubePlayer

    lateinit var handler: Handler
    private val updatePlayerTimeTask = object : Runnable {
        override fun run() {
            if (player.currentTimeMillis <= 124700) {
                handler.postDelayed(this, 1000)
            } else {
                player.seekToMillis(2500)
                player.pause()
                handler.removeCallbacks(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar.toolbar
        toolbar.title = resources.getString(R.string.curiosities)
        drawer = binding.drawerLayout
        val id = 3
        navigation = NavView(this, drawer, toolbar, id, binding.nawview.navView)
        navigation.create()

        val b = BitmapFactory.decodeResource(resources, R.drawable.csoportkep)
        binding.imGroup.setImageBitmap(b)


        binding.youtubePlayerV.initialize(
            resources.getString(R.string.google_maps_key),
            YoutubePlayerInitialize()
        )

        handler = Handler(Looper.getMainLooper())
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen((GravityCompat.START))) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    inner class YoutubePlayerInitialize : YouTubePlayer.OnInitializedListener {
        override fun onInitializationSuccess(
            p0: YouTubePlayer.Provider?,
            p1: YouTubePlayer?,
            p2: Boolean
        ) {
            if (p1 != null)
                player = p1
            p1?.setPlaybackEventListener(PlayBackEventListener())
            p1?.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
            p1?.loadVideo(videoUrlId, 0)
        }

        override fun onInitializationFailure(
            p0: YouTubePlayer.Provider?,
            p1: YouTubeInitializationResult?
        ) {
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.error_player),
                Toast.LENGTH_LONG
            ).show()
        }

    }

    inner class PlayBackEventListener : YouTubePlayer.PlaybackEventListener {
        override fun onPlaying() {
            handler.post(updatePlayerTimeTask)
        }

        override fun onPaused() {
            handler.removeCallbacks(updatePlayerTimeTask)
        }

        override fun onStopped() {}

        override fun onBuffering(p0: Boolean) {}

        override fun onSeekTo(p0: Int) {}

    }
}
package com.example.itunesapimusicsearch

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.itunesapimusicsearch.data.ITunesItem
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit


class PlayMusicActivity : AppCompatActivity() {
    private lateinit var item: ITunesItem
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playButton: Button
    private lateinit var seekBar: SeekBar
    private lateinit var progressText: TextView
    private lateinit var durationText: TextView
    private var isPlaying: Boolean = false
    private var currentDuration: Int = 0
    private var totalDuration: Int = 0
    private var isRepeatEnabled: Boolean = false
    private val handler = Handler()

    private val updateSeekBar = object : Runnable {
        override fun run() {
            if (isPlaying) {
                currentDuration = mediaPlayer.currentPosition
                seekBar.progress = currentDuration
                progressText.text = formatDuration(currentDuration)
                handler.postDelayed(this, 1000)
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        item = intent.getSerializableExtra("item") as ITunesItem

        val imageView: ImageView = findViewById(R.id.imageView)
        val albumTextView: TextView = findViewById(R.id.albumTextView)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val artistTextView: TextView = findViewById(R.id.artistTextView)
        playButton = findViewById(R.id.playButton)
        seekBar = findViewById(R.id.seekBar)
        progressText = findViewById(R.id.progressText)
        durationText = findViewById(R.id.durationText)

        Picasso.get().load(item.artworkUrl100).into(imageView)
        titleTextView.text = item.trackName
        albumTextView.text = item.collectionName
        artistTextView.text = item.artistName

        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(item.previewUrl)
        mediaPlayer.prepare()
        totalDuration = mediaPlayer.duration

        seekBar.max = totalDuration
        durationText.text = formatDuration(totalDuration)

        playButton.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    currentDuration = progress
                    mediaPlayer.seekTo(currentDuration)
                    progressText.text = formatDuration(currentDuration)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No implementation needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No implementation needed
            }
        })

        mediaPlayer.setOnCompletionListener {
            if (isRepeatEnabled) {
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            } else {
                pauseMusic()
                resetPlayback()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        playMusic()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        handler.removeCallbacks(updateSeekBar)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun playMusic() {
        mediaPlayer.start()
        isPlaying = true
        playButton.text = "Pause"
        handler.postDelayed(updateSeekBar, 0)
    }

    private fun pauseMusic() {
        mediaPlayer.pause()
        isPlaying = false
        playButton.text = "Play"
        handler.removeCallbacks(updateSeekBar)
    }

    private fun resetPlayback() {
        currentDuration = 0
        seekBar.progress = currentDuration
        progressText.text = formatDuration(currentDuration)
    }

    private fun formatDuration(duration: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong()) / 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

/*
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.itunesapimusicsearch.data.ITunesItem
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit


class PlayMusicActivity : AppCompatActivity() {
    private lateinit var item: ITunesItem
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playButton: Button
    private lateinit var seekBar: SeekBar
    private lateinit var progressText: TextView
    private lateinit var durationText: TextView
    private var isPlaying: Boolean = false
    private var currentDuration: Int = 0
    private var totalDuration: Int = 0
    private val handler = Handler()

    private val updateSeekBar = object : Runnable {
        override fun run() {
            if (isPlaying) {
                currentDuration = mediaPlayer.currentPosition
                seekBar.progress = currentDuration
                progressText.text = formatDuration(currentDuration)
                handler.postDelayed(this, 1000)
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        item = intent.getSerializableExtra("item") as ITunesItem

        val imageView: ImageView = findViewById(R.id.imageView)
        val albumTextView: TextView = findViewById(R.id.albumTextView)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val artistTextView: TextView = findViewById(R.id.artistTextView)
        playButton = findViewById(R.id.playButton)
        seekBar = findViewById(R.id.seekBar)
        progressText = findViewById(R.id.progressText)
        durationText = findViewById(R.id.durationText)

        Picasso.get().load(item.artworkUrl100).into(imageView)
        titleTextView.text = item.trackName
        albumTextView.text = item.collectionName
        artistTextView.text = item.artistName

        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(item.previewUrl)
        mediaPlayer.prepare()
        totalDuration = mediaPlayer.duration

        seekBar.max = totalDuration
        durationText.text = formatDuration(totalDuration)



        playButton.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    currentDuration = progress
                    mediaPlayer.seekTo(currentDuration)
                    progressText.text = formatDuration(currentDuration)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No implementation needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No implementation needed
            }
        })
        mediaPlayer.setOnCompletionListener {
            if (progressText == durationText) {
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        playMusic()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        handler.removeCallbacks(updateSeekBar)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun playMusic() {
        mediaPlayer.start()
        isPlaying = true
        playButton.text = "Pause"
        handler.postDelayed(updateSeekBar, 0)
    }

    private fun pauseMusic() {
        mediaPlayer.pause()
        isPlaying = false
        playButton.text = "Play"
        handler.removeCallbacks(updateSeekBar)
    }

    private fun formatDuration(duration: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong()) / 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
        return String.format("%02d:%02d", minutes, seconds)
    }
}


 */
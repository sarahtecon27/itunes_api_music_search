package com.example.itunesapimusicsearch

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.itunesapimusicsearch.data.ITunesItem
import com.squareup.picasso.Picasso

class MusicDetailsActivity : AppCompatActivity() {
    private lateinit var item: ITunesItem
    private lateinit var mediaPlayer: MediaPlayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        item = intent.getSerializableExtra("item") as ITunesItem

        val imageView: ImageView = findViewById(R.id.imageView)
        val albumTextView: TextView = findViewById(R.id.albumTextView)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val artistTextView: TextView = findViewById(R.id.artistTextView)
        val priceTextView: TextView = findViewById(R.id.priceTextView)
        val playButton: Button = findViewById(R.id.playButton)

        Picasso.get().load(item.artworkUrl100).into(imageView)
        titleTextView.text = item.trackName
        albumTextView.text = item.collectionName
        artistTextView.text = item.artistName
        priceTextView.text = item.trackPrice.toString()


        playButton.setOnClickListener {
            val intent = Intent(this, PlayMusicActivity::class.java)
            intent.putExtra("item", item)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        mediaPlayer.release()
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

}




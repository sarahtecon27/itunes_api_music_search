package com.example.itunesapimusicsearch

import ApiService
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesapimusicsearch.data.ITunesItem
import com.example.itunesapimusicsearch.data.ItunesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MusicAdapter
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MusicAdapter { item -> showMusicDetails(item) }
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        // Show the back button when typing in the search view
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            supportActionBar?.setDisplayHomeAsUpEnabled(hasFocus)
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                true
            }
            android.R.id.home -> {
                onBackPressed() // Go back to the previous page
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            searchView.setQuery("", false) // Clear the query text
            adapter.clearData() // Clear the search results
            supportActionBar?.title = "iTunes API Music Search"
        } else {
            super.onBackPressed()
        }
    }


    override fun onQueryTextSubmit(query: String): Boolean {
        performSearch(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return true
    }

    private fun performSearch(query: String) {
        val call = apiService.searchMusic(query)
        call.enqueue(object : Callback<ItunesData> {
            override fun onResponse(call: Call<ItunesData>, response: Response<ItunesData>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.results?.let {
                        adapter.setData(it)
                    }
                }
            }

            override fun onFailure(call: Call<ItunesData>, t: Throwable) {
                // Handle failure here
            }
        })
    }

    private fun showMusicDetails(item: ITunesItem) {
        val intent = Intent(this, MusicDetailsActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
    }
}

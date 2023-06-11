package com.example.itunesapimusicsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesapimusicsearch.data.ITunesItem
import com.squareup.picasso.Picasso

class MusicAdapter(private val onItemClick: (ITunesItem) -> Unit) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    private val items: MutableList<ITunesItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    fun setData(data: List<ITunesItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val albumTextView: TextView = itemView.findViewById(R.id.albumTextView)
        private val artistTextView: TextView = itemView.findViewById(R.id.artistTextView)

        fun bind(item: ITunesItem) {
            Picasso.get().load(item.artworkUrl100).into(imageView)
            titleTextView.text = item.trackName
            albumTextView.text = item.collectionName
            artistTextView.text = item.artistName
        }
    }

}

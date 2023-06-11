package com.example.itunesapimusicsearch.data

import java.io.Serializable


class ItunesData {

    val results = ArrayList<ITunesItem>()
}

data class ITunesItem(
    val artworkUrl100: String?,
    val artistName: String?,
    val artistViewUrl: String?,
    val trackName: String?,
    val trackPrice: Float?,
    val collectionName: String?,
    val previewUrl: String?,
) : Serializable

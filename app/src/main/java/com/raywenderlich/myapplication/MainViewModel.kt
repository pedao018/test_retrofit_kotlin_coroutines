package com.raywenderlich.myapplication

import androidx.lifecycle.ViewModel
import com.raywenderlich.myapplication.repository.ItunesRepo
import com.raywenderlich.myapplication.service.PodcastResponse

class MainViewModel(private val type: Int) : ViewModel() {
    var iTunesRepo: ItunesRepo? = null

    suspend fun searchPodcasts(term: String): List<PodcastSummaryViewData> {
        val results = iTunesRepo?.searchByTerm(term)

        if (results != null && results.isSuccessful) {
            val podcasts = results.body()?.results
            if (!podcasts.isNullOrEmpty()) {
                return podcasts.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
            }
        }
        return emptyList()
    }

    suspend fun searchPodcasts_Mutil(
        term: String,
        prevResult: String
    ): List<PodcastSummaryViewData> {
        val results = iTunesRepo?.searchByTerm(term)

        if (results != null && results.isSuccessful) {
            //Log.e("hahaha", "$term - $prevResult")
            val podcasts = results.body()?.results
            if (!podcasts.isNullOrEmpty()) {
                return podcasts.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
            }
        }
        return emptyList()
    }

    private fun itunesPodcastToPodcastSummaryView(
        itunesPodcast: PodcastResponse.ItunesPodcast
    ):
            PodcastSummaryViewData {
        return PodcastSummaryViewData(
            itunesPodcast.collectionCensoredName,
            itunesPodcast.releaseDate,
            itunesPodcast.artworkUrl100,
            itunesPodcast.feedUrl
        )
    }

    data class PodcastSummaryViewData(
        var name: String? = "",
        var lastUpdated: String? = "",
        var imageUrl: String? = "",
        var feedUrl: String? = ""
    )
}
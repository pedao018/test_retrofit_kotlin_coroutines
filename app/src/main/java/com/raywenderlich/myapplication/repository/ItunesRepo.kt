package com.raywenderlich.myapplication.repository

import com.raywenderlich.myapplication.service.ItunesService

class ItunesRepo(private val itunesService: ItunesService) {
    suspend fun searchByTerm(term: String) = itunesService.searchPodcastByTerm(term)
}

package com.bonnetrouge.toonup.Data

import com.bonnetrouge.toonup.API.StreamingApiService

class VideoRepository(private val apiService: StreamingApiService) {

	fun getPopularCartoons() = apiService.getPopularCartoons()

	fun getPopularMovies() = apiService.getPopularMovies()
}
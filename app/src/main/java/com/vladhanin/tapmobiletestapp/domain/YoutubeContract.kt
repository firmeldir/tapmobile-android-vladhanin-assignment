package com.vladhanin.tapmobiletestapp.domain

import com.github.michaelbull.result.Result

data class YoutubeEntity(
    val videoId: String,
    val thumbnailUrl: String
)

interface YoutubeContract {

    suspend fun search(
        searchQuery: String
    ) : Result<List<YoutubeEntity>, Unit>

    suspend fun more(
        searchQuery: String
    ) : Result<List<YoutubeEntity>, Unit>
}
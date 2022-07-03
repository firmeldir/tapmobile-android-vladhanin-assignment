package com.vladhanin.tapmobiletestapp.data

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.squareup.moshi.Moshi
import com.vladhanin.tapmobiletestapp.domain.YoutubeContract
import com.vladhanin.tapmobiletestapp.domain.YoutubeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YoutubeContractImpl (
    private val service: YoutubeRetrofitService
) : YoutubeContract {


    private val jsonRegex = Regex.fromLiteral("""/(\{(?:(?>[^{}"'\/]+)|(?>"(?:(?>[^\\"]+)|\\.)*")|(?>'(?:(?>[^\\']+)|\\.)*')|(?>\/\/.*\n)|(?>\/\*.*?\*\/)|(?-1))*\})/""")
    private val itemSectionRendererAdapter = Moshi.Builder().build().adapter(ItemSectionRendererDTO::class.java)
    private val continuationCommandAdapter = Moshi.Builder().build().adapter(ContinuationCommandDTO::class.java)

    private var lastSearchQuery = ""
    private var lastContinuation = ""

    override suspend fun search(searchQuery: String): Result<List<YoutubeEntity>, Unit> {
        lastSearchQuery = searchQuery
        return kotlin.runCatching {
            withContext(Dispatchers.IO) { service.getSearchRequest(searchQuery) }
        }.getOrNull()
            ?.let {
                if (it.isSuccessful) it.body() else null
            }?.let { html ->

                html.indexOf("\"continuationCommand\"")
                    .takeUnless { it == -1 }
                    ?.let { index ->
                        continuationCommandAdapter.fromJson(jsonRegex.find(html, index)?.value ?: "")
                    }
                    ?.let { dto ->
                        lastContinuation = dto.token
                    }

                html.indexOf("\"itemSectionRenderer\"")
                    .takeUnless { it == -1 }
                    ?.let { index ->
                        itemSectionRendererAdapter.fromJson(jsonRegex.find(html, index)?.value ?: "")
                    }
                    ?.let { dto ->
                        dto.contents.map { content ->
                            val thumbnailUrl = content.videoRenderer.thumbnail.thumbnails.getOrNull(0)?.url ?: ""
                            YoutubeEntity(content.videoRenderer.videoId, thumbnailUrl)
                        }
                    }
            }?.let { Ok(it) } ?: Err(Unit)
    }

    override suspend fun more(searchQuery: String): Result<List<YoutubeEntity>, Unit> {
        return kotlin.runCatching {
            val body = PostContinuationRequestDTO(lastContinuation)
            withContext(Dispatchers.IO) { service.postContinuationRequest(body) }
        }.getOrNull()
            ?.let {
                if (it.isSuccessful) it.body() else null
            }?.let {
                lastContinuation = it.onResponseReceivedCommands
                    .getOrNull(0)
                    ?.appendContinuationItemsAction
                    ?.continuationItems
                    ?.getOrNull(1)
                    ?.continuationItemRenderer
                    ?.continuationEndpoint
                    ?.continuationCommand
                    ?.token ?: ""

                it.onResponseReceivedCommands
                    .getOrNull(0)
                    ?.appendContinuationItemsAction
                    ?.continuationItems
                    ?.getOrNull(0)
                    ?.itemSectionRenderer
                    ?.contents
                    ?.map { content ->
                        val thumbnailUrl = content.videoRenderer.thumbnail.thumbnails.getOrNull(0)?.url ?: ""
                        YoutubeEntity(content.videoRenderer.videoId, thumbnailUrl)
                    }
            }?.let { Ok(it) } ?: Err(Unit)
    }
}
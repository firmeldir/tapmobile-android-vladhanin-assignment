package com.vladhanin.tapmobiletestapp.data

data class PostContinuationResponseDTO (
    val onResponseReceivedCommands: List<OnResponseReceivedCommand>
)

data class OnResponseReceivedCommand (
    val appendContinuationItemsAction: AppendContinuationItemsAction
)

data class AppendContinuationItemsAction (
    val continuationItems: List<ContinuationItem>,
)

data class ContinuationItem (
    val itemSectionRenderer: ItemSectionRendererDTO? = null,
    val continuationItemRenderer: ContinuationItemRenderer? = null
)

data class ItemSectionRendererDTO (
    val contents: List<Content>
)

data class Content (
    val videoRenderer: VideoRenderer
)

data class VideoRenderer (
    val videoId: String,

    val thumbnail: ReelWatchEndpointThumbnail,
)

data class ReelWatchEndpointThumbnail (
    val thumbnails: List<ThumbnailElement>,
    val isOriginalAspectRatio: Boolean? = null
)

data class ThumbnailElement (
    val url: String,
    val width: Long,
    val height: Long
)

data class ContinuationItemRenderer (
    val continuationEndpoint: ContinuationEndpoint
)

data class ContinuationEndpoint (
    val continuationCommand: ContinuationCommandDTO
)

data class ContinuationCommandDTO (
    val token: String,
    val request: String
)
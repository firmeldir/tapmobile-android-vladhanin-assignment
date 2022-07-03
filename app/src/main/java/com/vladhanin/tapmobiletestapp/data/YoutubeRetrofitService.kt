package com.vladhanin.tapmobiletestapp.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface YoutubeRetrofitService {

    @GET("/results")
    suspend fun getSearchRequest(
        @Query("search_query") searchQuery: String
    ) : Response<String>

    @POST("/youtubei/v1/search?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8&prettyPrint=false")
    suspend fun postContinuationRequest(@Body body: PostContinuationRequestDTO) : Response<PostContinuationResponseDTO>
}
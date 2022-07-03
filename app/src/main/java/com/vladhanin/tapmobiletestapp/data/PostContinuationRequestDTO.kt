package com.vladhanin.tapmobiletestapp.data

data class PostContinuationRequestDTO (
    val continuation: String,
    val context: Context = Context(),
)

data class Context (
    val client: Client = Client()
)

data class Client (
    val clientName: String = "WEB",
    val clientVersion: String = "2.20220701.01.00"
)
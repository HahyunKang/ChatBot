package com.app.chatbot.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers

interface GetMessage {
    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path="v1/chat/completions", hasBody = true)
    suspend fun getMessage(
        @Header(value = "Authorization") accessToken : String,
        @Body request: Request
    ) : MessageDto
}
package com.app.chatbot.data

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)

data class Chat(
    val role : String,
    val content : String
)
package com.app.chatbot.data

data class MessageDto(
    val choices: List<Choice>?,
    val created: Int?,
    val id: String?,
    val model: String?,
    val `object`: String?,
    val usage: Usage?
)
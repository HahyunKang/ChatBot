package com.app.chatbot.data

data class Request(
    val messages: List<UserMessage>,
    val model: String,
    val temperature: Double
)
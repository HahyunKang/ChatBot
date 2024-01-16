package com.app.chatbot.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.chatbot.SendMessageUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChatViewModel : ViewModel() {

    val sendMessageUseCase = SendMessageUseCase()
    private val _message = mutableStateOf("Loading")
    val message= _message
    fun sendMessage(message: String){
        sendMessageUseCase(message).onEach {
            if(it.choices!!.isNotEmpty()){
                _message.value = it.choices.get(0).message.content
            }
            else _message.value = "Loading"
        }.launchIn(viewModelScope)
    }

}
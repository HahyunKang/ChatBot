package com.app.chatbot

import android.util.Log
import com.app.chatbot.data.MessageDto
import com.app.chatbot.data.Request
import com.app.chatbot.data.UserMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class SendMessageUseCase {

    operator fun invoke(message :String) : Flow<MessageDto> = flow{
        try {
            val token = "Bearer ${CONSTANT.API_KEY}"
            val model ="gpt-3.5-turbo"
            Log.e("sendMessage",message)
        //    val jsonObject = JSONObject("{\"title\":\"${title}\",\"content\":\"${content}\"}").toString()
          //  val jsonBody = RequestBody.create("application/json".toMediaTypeOrNull(),jsonObject)
            emit(MessageDto(emptyList(),0,"","","",null))
            val list = listOf(UserMessage(role="user", content = message))
            val groupResponse = Retrofit.retrofit.getMessage(accessToken = token, Request(list,model,0.7))

            emit(groupResponse)
        }
        catch (e: HttpException) {
            Log.e("exception",e.message.toString())
        } catch (e: IOException) {
            Log.e("exception",e.message.toString())
        }
    }
}
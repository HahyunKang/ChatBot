package com.app.chatbot

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.chatbot.data.Chat
import com.app.chatbot.data.GetMessage
import com.app.chatbot.data.Message
import com.app.chatbot.ui.ChatViewModel
import com.app.chatbot.ui.theme.ChatBotTheme
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatBotTheme {
                val viewModel : ChatViewModel = viewModel()
                val message = viewModel.message
                val listState = rememberLazyListState()

                val userText = remember {
                    mutableStateOf("")
                }
                val messages = remember {
                    mutableStateListOf<Chat>()
                }
                val isMessage = remember{
                    mutableStateOf(false)
                }
                Log.e("message_Test in Main",message.value)

                LaunchedEffect(key1 = messages.size) {
                    if(messages.isNotEmpty())listState.animateScrollToItem(index = messages.size - 1)
                }
                if(message.value != "Loading"){
                    Log.e("message_Test in Main",message.value)
                    messages.add(Chat("assistant",message.value))
                    message.value = "Loading"
                }
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(7f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        state = listState
                    ){
                        itemsIndexed(messages){
                            index,item ->
                            if(item.role == "user"){
                                Log.e("message","message")
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Box(
                                        modifier =Modifier
                                            .background(
                                                Color(0xFF00BAB3),
                                                shape = RoundedCornerShape(12.dp,)
                                            )
                                            .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.8f)
                                    ) {
                                        Text(
                                            text = item.content,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                                        )
                                    }
                                }
                            }else {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Color(0xFFEFB8C8),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.8f)
                                    ) {
                                        Text(
                                            text = item.content,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }


                    Row(
                        modifier = Modifier.weight(1f)
                    ){
                        OutlinedTextField(
                            value = userText.value,
                            onValueChange = {
                                userText.value = it
                            },
                            enabled= true ,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(4f),
                            placeholder = {
                                Text(
                                    text = "텍스트를 입력해주세요",
                                    color = Color(0xFF9EA4AA)
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0x80C9CDD2),
                                focusedBorderColor = Color(0x80C9CDD2),
                                unfocusedPlaceholderColor = Color.Unspecified,
                                focusedPlaceholderColor = Color.Unspecified
                            )
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.send_message),contentDescription = null,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    messages.add(Chat(role = "user", content = userText.value))
                                    viewModel.sendMessage(userText.value)
                                    userText.value = ""
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatBotTheme {
        Greeting("Android")
    }
}
package com.app.chatbot

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.chatbot.data.Chat
import com.app.chatbot.ui.ChatViewModel
import com.app.chatbot.ui.theme.ChatBotTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ChatBotTheme {
                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    if (permissions[Manifest.permission.RECORD_AUDIO] == true) {
                        // 권한이 허용되었을 경우의 처리
                    } else {
                        // 권한이 거부되었을 경우 대화상자 표시
                    }
                }
                val userText = remember {
                mutableStateOf("")
            }
                val messages = remember {
                    mutableStateListOf<Chat>()
                }
                val isMessage = remember{
                    mutableStateOf(false)
                }
                val viewModel : ChatViewModel = viewModel()
                val message = viewModel.message
                val listState = rememberLazyListState()

                val listener: RecognitionListener = object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle) {
                        // 말하기 시작할 준비가되면 호출
                        Toast.makeText(applicationContext, "음성인식 시작", Toast.LENGTH_SHORT).show()
                    }

                    override fun onBeginningOfSpeech() {
                        // 말하기 시작했을 때 호출
                    }

                    override fun onRmsChanged(rmsdB: Float) {
                        // 입력받는 소리의 크기를 알려줌
                    }

                    override fun onBufferReceived(buffer: ByteArray) {
                        // 말을 시작하고 인식이 된 단어를 buffer에 담음
                    }

                    override fun onEndOfSpeech() {
                        // 말하기를 중지하면 호출
                    }

                    override fun onError(error: Int) {
                        // 네트워크 또는 인식 오류가 발생했을 때 호출
                        val message: String
                        message = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                            SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                            SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                            SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                            SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                            else -> "알 수 없는 오류임"
                        }
                        Toast.makeText(applicationContext, "에러 발생 : $message", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResults(results: Bundle) {
                        Log.e("TTS","Successful in TTS")
                        // 인식 결과가 준비되면 호출
                        // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
                        val matches =
                            results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        for (i in matches!!.indices) {
                            userText.value = matches[i]
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle) {
                        // 부분 인식 결과를 사용할 수 있을 때 호출
                    }

                    override fun onEvent(eventType: Int, params: Bundle) {
                        // 향후 이벤트를 추가하기 위해 예약
                    }
                };
                Log.e("message_Test in Main",message.value)

                LaunchedEffect(key1 = messages.size) {
                    if(messages.isNotEmpty())listState.animateScrollToItem(index = messages.size - 1)
                }

                LaunchedEffect(Unit) {
                    requestPermissionLauncher.launch(
                        arrayOf(Manifest.permission.RECORD_AUDIO)
                    )
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
                                        modifier = Modifier
                                            .background(
                                                Color(0xFF00BAB3),
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

                        val  context= LocalContext.current
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName()); // 여분의 키
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR")
                        Icon(
                            painter = painterResource(id = R.drawable.send_message),contentDescription = null,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    messages.add(Chat(role = "user", content = userText.value))
                                    viewModel.sendMessage(userText.value)
                                    userText.value = ""

//                                    val mRecognizer =
//                                        SpeechRecognizer.createSpeechRecognizer(context)
//                                    mRecognizer.setRecognitionListener(listener); // 리스너 설정
//                                    mRecognizer.startListening(intent);
                                }
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_keyboard_voice_24),contentDescription = null,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
//                                    messages.add(Chat(role = "user", content = userText.value))
//                                    viewModel.sendMessage(userText.value)
//                                    userText.value = ""

                                    val mRecognizer =
                                        SpeechRecognizer.createSpeechRecognizer(context)
                                    mRecognizer.setRecognitionListener(listener); // 리스너 설정
                                    mRecognizer.startListening(intent);
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
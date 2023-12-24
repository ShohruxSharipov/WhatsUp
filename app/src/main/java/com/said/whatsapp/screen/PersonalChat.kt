package com.said.whatsapp.screen

import android.content.ComponentCallbacks2
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.said.whatsapp.R
import com.said.whatsapp.model.Message
import com.said.whatsapp.model.User
import com.said.whatsapp.screen.ui.theme.LightBlue
import com.said.whatsapp.screen.ui.theme.LightGreen
import com.said.whatsapp.screen.ui.theme.LightOrange
import com.said.whatsapp.screen.ui.theme.LightPink
import com.said.whatsapp.screen.ui.theme.LightPurple
import com.said.whatsapp.screen.ui.theme.greeen
import com.said.whatsapp.screen.ui.theme.grey
import com.said.whatsapp.ui.theme.WhatsAppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PersonalChat : ComponentActivity(), ComponentCallbacks2 {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uid = intent.getStringExtra("uid_1")
                    val user_id = intent.getStringExtra("user_uid")
                    val letter = intent.getStringExtra("letter")

                    val name = remember {
                        mutableStateOf("")
                    }
                    val messages = remember {
                        mutableStateListOf(Message())
                    }

                    val colors = remember {
                        mutableStateListOf(LightBlue)
                    }
                    colors.add(LightGreen)
                    colors.add(LightOrange)
                    colors.add(LightPurple)
                    colors.add(LightPink)
                    val reference = Firebase.database.reference
                        .child("users")
                        .child(uid!!)
                        .child("message")
                        .child(user_id!!)

                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val children = snapshot.children
                            messages.clear()
                            children.forEach {
                                val message = it.getValue(Message::class.java)
                                messages.add(message ?: Message())
                                Log.d("tag1", "onDataChange: $messages")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("TAG", "onCancelled: ${error.message}")
                        }
                    })


                    val text = remember {
                        mutableStateOf(TextFieldValue(""))
                    }


                    val reference_1 = Firebase.database.reference.child("users")

                    reference_1.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val children = snapshot.children
                            children.forEach {
                                val userr = it.getValue(User::class.java)
                                if (userr?.uid == user_id) {
                                    name.value = userr.username!!
                                    Log.d("TAG2", "Greeting2: $userr")
                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("TAG", "onCancelled: ${error.message}")
                        }

                    })

                    Image(
                        painter = painterResource(id = R.drawable.img_2),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        val random = remember {
                            colors.random()
                        }
                        TopAppBar(
                            modifier = Modifier
                                .fillMaxWidth(),
                            title = { /*TODO*/ },
                            actions = {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp)
                                            .clickable {
                                                finish()
                                            },
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                    Card(
                                        modifier = Modifier
                                            .width(50.dp)
                                            .height(50.dp),
                                        shape = CircleShape,
                                        colors = CardDefaults.cardColors(
                                            containerColor = random,
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = letter!!,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White,
                                                fontSize = 24.sp,
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    Text(
                                        text = name.value,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(start = 10.dp),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = greeen)
                        )

                        Box(modifier = Modifier.fillMaxSize()) {

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(bottom = 65.dp)
                                    .align(Alignment.BottomCenter)
                            ) {
                                items(messages) {
                                    MessageRow(uid = uid.toString(), message = it)
                                }
                            }


                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    value = text.value,
                                    onValueChange = { newText ->
                                        text.value = newText
                                    },
                                    placeholder = {
                                        Text(text = "Message")
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            if (text.value != TextFieldValue("")) {
                                                val key = reference_1.push().key.toString()
                                                val currentDateTime = LocalDateTime.now()
                                                val formatter =
                                                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                                                val formattedDateTime =
                                                    currentDateTime.format(formatter)
                                                val message =
                                                    Message(
                                                        uid,
                                                        user_id,
                                                        text.value.text,
                                                        formattedDateTime
                                                    )
                                                reference_1.child(uid)
                                                    .child("message")
                                                    .child(user_id)
                                                    .child(key)
                                                    .setValue(message)
                                                reference_1.child(user_id)
                                                    .child("message")
                                                    .child(uid)
                                                    .child(key)
                                                    .setValue(message)

                                                text.value = TextFieldValue("")
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Send,
                                                contentDescription = "",
                                                tint = greeen
                                            )
                                        }
                                    },
                                    maxLines = 3
                                )
                            }
                        }
                    }

                }
            }
        }
    }

    @Composable
    fun MessageRow(uid: String, message: Message) {
        if (message.from.toString() != uid) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(vertical = 5.dp)
                        .padding(start = 10.dp)
                        .padding(end = 40.dp)
                )
                {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = message.text.toString()
                    )
                }
                Text(
                    text = message.data.toString(),
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 12.sp,
                    color = grey
                )
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Card(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(vertical = 5.dp)
                            .padding(start = 40.dp)
                            .padding(end = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LightGreen
                        ),
                    )
                    {
                        Text(
                            text = message.text.toString(),
                            modifier = Modifier
                                .padding(10.dp)
                        )

                    }
                    Text(
                        text = message.data.toString(),
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .padding(horizontal = 10.dp),
                        fontSize = 12.sp,
                        color = grey
                    )
                }
            }
        }
    }
}

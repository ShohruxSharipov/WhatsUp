package com.said.whatsapp.screen

import android.content.ComponentCallbacks2
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.said.whatsapp.MainActivity
import com.said.whatsapp.model.FirebaseData
import com.said.whatsapp.model.User
import com.said.whatsapp.screen.ui.theme.WhatsAppTheme
import com.said.whatsapp.screen.ui.theme.greeen
import java.io.File


class SettingsActivity : ComponentActivity(), ComponentCallbacks2 {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val current = intent.getStringExtra("current_user")

                    var menuExpanded by remember {
                        mutableStateOf(false)
                    }


                    val current_user = remember {
                        mutableStateOf(User())
                    }

                    val reference = Firebase.database.reference.child("users")

                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val children = snapshot.children
                            children.forEach {
                                val user = it.getValue(User::class.java)
                                if (user!!.uid == current) {
                                    current_user.value = user

                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("TAG", "onCancelled: ${error.message}")
                        }

                    })
                    val username = remember {
                        mutableStateOf("")
                    }
                    val password = remember {
                        mutableStateOf("")
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(verticalAlignment = Alignment.Top) {
                            CenterAlignedTopAppBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                title = {
                                    Text(
                                        text = "Settings",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = greeen
                                ),
                                navigationIcon = {
                                    Icon(imageVector = Icons.Default.ArrowBack,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .clickable { finish() })
                                }, actions = {
                                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "More",
                                            tint = Color.White
                                        )
                                    }


                                    DropdownMenu(
                                        expanded = menuExpanded,
                                        onDismissRequest = { menuExpanded = false }) {
                                        DropdownMenuItem(
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.ExitToApp,
                                                    contentDescription = "",
                                                    tint = Color.Red
                                                )
                                            },
                                            text = {
                                                Text("Log out", color = Color.Red)
                                            },
                                            onClick = {
                                                val intent = Intent(
                                                    this@SettingsActivity,
                                                    MainActivity::class.java
                                                )
                                                FirebaseData.saveUser(this@SettingsActivity, "")
                                                onTrimMemory(TRIM_MEMORY_COMPLETE)
                                                finishAffinity()
                                                startActivity(intent)
                                            },
                                        )
                                    }
                                })
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 10.dp)
                        ) {
                            OutlinedTextField(
                                value = username.value,
                                onValueChange = {
                                    username.value = it
                                },
                                label = {
                                    Text(text = "Username")
                                },
                                placeholder = { Text(text = "Enter new username") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = password.value,
                                onValueChange = { newText ->
                                    password.value = newText
                                },
                                label = {
                                    Text(text = "Password")
                                },
                                placeholder = { Text(text = "Enter new password") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    if (password.value.isNotEmpty() && username.value.isNotEmpty()) {
                                        reference
                                            .child(current_user.value.uid.toString())
                                            .child("password")
                                            .setValue(password.value)
                                        reference
                                            .child(current_user.value.uid.toString())
                                            .child("username")
                                            .setValue(username.value)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@SettingsActivity,
                                            "Bo'sh joylarni to'ldiring",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                },
                                colors = ButtonDefaults.buttonColors(containerColor = greeen)
                            ) {
                                Text(text = "Update")
                            }
                        }
                    }
                }
            }
        }
    }


}


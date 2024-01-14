package com.said.whatsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.said.whatsapp.model.FirebaseData
import com.said.whatsapp.screen.Contacts
import com.said.whatsapp.screen.LogIn
import com.said.whatsapp.screen.Password
import com.said.whatsapp.screen.PersonalChat
import com.said.whatsapp.ui.theme.WhatsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (FirebaseData.getSavedUser(this@MainActivity) != "") {
                        val intent = Intent(this, Password::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, LogIn::class.java)
                        startActivity(intent)
                    }
                    finish()
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
        WhatsAppTheme {
            Greeting("Android")
        }
    }
}
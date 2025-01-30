package com.shivangi.videocallingapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun JoinCallScreen(
    onJoin: (String) -> Unit
) {
    var roomText by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter Room Name for Video Call:")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = roomText,
            onValueChange = { roomText = it },
            label = { Text("Room Name") }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val room = roomText.text.trim()
            if (room.isNotEmpty()) {
                onJoin(room)
            }
        }) {
            Text("Join Call")
        }
    }
}

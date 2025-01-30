package com.shivangi.videocallingapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class VideoCallViewModel : ViewModel() {

    // If null => not in call, else we store the room name
    val currentRoomName = mutableStateOf<String?>(null)

    fun joinCall(room: String) {
        currentRoomName.value = room
    }

    fun leaveCall() {
        currentRoomName.value = null
    }
}

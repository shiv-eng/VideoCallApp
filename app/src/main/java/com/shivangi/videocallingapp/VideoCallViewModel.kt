package com.example.videocall

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class VideoCallViewModel : ViewModel() {

    // Holds the current room name. If null => not in a call.
    var roomName = mutableStateOf<String?>(null)
        private set

    fun joinCall(room: String) {
        roomName.value = room
    }

    fun leaveCall() {
        roomName.value = null
    }
}

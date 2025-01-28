package com.shivangi.videocallingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivangi.videocallingapp.webrtc.WebRtcManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CallUiState(
    val isInCall: Boolean = false,
    val statusMessage: String = ""
)

class CallViewModel : ViewModel() {
    private val _callUiState = MutableStateFlow(CallUiState())
    val callUiState: StateFlow<CallUiState> = _callUiState

    private val webRtcManager = WebRtcManager()

    fun startCall() {
        viewModelScope.launch {
            // Initialize & start local camera, etc.
            webRtcManager.initPeerConnectionFactory()
            webRtcManager.startLocalVideoCapture()

            _callUiState.value = _callUiState.value.copy(
                isInCall = true,
                statusMessage = "Call Started"
            )

            // In a real app: use your signaling to create/offer or join a room
        }
    }

    fun endCall() {
        viewModelScope.launch {
            webRtcManager.stopCall()

            _callUiState.value = _callUiState.value.copy(
                isInCall = false,
                statusMessage = "Call Ended"
            )
        }
    }
}

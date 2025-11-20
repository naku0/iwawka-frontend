package com.example.iwawka.ui.states

data class MediaState(
    val currentPlayingAudio: String? = null, // messageId
    val audioProgress: Float = 0f,
    val isRecordingVoice: Boolean = false,
    val voiceRecordingDuration: Long = 0L
)

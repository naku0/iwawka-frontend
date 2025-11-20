package com.example.iwawka.ui.states

data class AttachmentState(
    val uploadsInProgress: Map<String, UploadProgress> = emptyMap(),
    val failedUploads: Set<String> = emptySet()
)

data class UploadProgress(
    val progress: Float = 0f,
    val totalBytes: Long = 0,
    val uploadedBytes: Long = 0
)

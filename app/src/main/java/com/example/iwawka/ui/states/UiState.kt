package com.example.iwawka.ui.states

import Attachment

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val scrollToMessageId: String? = null,
    val fullscreenAttachment: Attachment? = null,
    val isEmojiPickerVisible: Boolean = false,
    val isAttachmentMenuVisible: Boolean = false,

    val isSelectionMode: Boolean = false,
    val isEditing: Boolean = false
)

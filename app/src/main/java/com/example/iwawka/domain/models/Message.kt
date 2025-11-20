package com.example.iwawka.domain.models

import Attachment
import kotlinx.serialization.Serializable
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf

enum class MessageType { TEXT, IMAGE, FILE, VOICE, SYSTEM }
enum class MessageStatus { SENDING, SENT, DELIVERED, READ, FAILED }

@Serializable
data class Message(
    //baza
    val id: String,
    val chatId: String,
    val text: String,
    val sender: User,
    val timestamp: Long,
    val type: MessageType = MessageType.TEXT,

    //statuses
    val status: MessageStatus = MessageStatus.SENDING,
    val isEdited: Boolean = false,
    val editTimestamp: Long? = null,

    //actions?
    val attachments: List<Attachment> = emptyList(),
    val replyToMessageId: String? = null,
    val reactions: ImmutableMap<String, ImmutableSet<String>> = persistentMapOf(),

    //metadata
    val readBy: ImmutableSet<String> = persistentSetOf(),
    val isDeleted: Boolean = false
) {
    fun isOwnMessage(currentUserId: String): Boolean = sender.id == currentUserId
    fun hasAttachments(): Boolean = attachments.isNotEmpty()
    fun isReply(): Boolean = replyToMessageId != null

}

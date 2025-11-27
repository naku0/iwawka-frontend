package com.example.iwawka.domain.repositories.impl

//import com.example.iwawka.domain.models.Attachment
//import com.example.iwawka.domain.models.AttachmentType рудименты
import com.example.iwawka.domain.models.Message
import com.example.iwawka.domain.repositories.interfaces.MessageRepository
import kotlinx.coroutines.delay

class MessageRepositoryImpl : MessageRepository {
    // временное хранилище, я хз как по другому сделать с сообщениями
    private val messages = mutableListOf<Message>().apply {
        add(Message(
            id = "1",
            text = "Привет! Как дела?",
            senderId = "1",
            chatId = "1",
            timestamp = "10:30",
            isFromMe = false
        ))
        add(Message(
            id = "2",
            text = "Всё отлично, спасибо!",
            senderId = "2",
            chatId = "1",
            timestamp = "10:32",
            isFromMe = true
        ))
        add(Message(
            id = "3",
            text = "Завтра встреча в 15:00",
            senderId = "2",
            chatId = "2",
            timestamp = "09:15",
            isFromMe = false
        ))
    }

    override suspend fun getMessage(messageId: String): Message? {
        delay(300)
        return messages.find { it.id == messageId }
    }

    override suspend fun getMessagesByChat(chatId: String): List<Message> {
        delay(300)
        return messages.filter { it.chatId == chatId }.sortedBy { it.timestamp }
    }

    override suspend fun sendMessage(message: Message): Boolean {
        delay(300)
        if (messages.any { it.id == message.id }) {
            return false
        }
        messages.add(message)
        return true
    }

    override suspend fun updateMessage(message: Message): Boolean {
        delay(300)
        val index = messages.indexOfFirst { it.id == message.id }
        if (index != -1) {
            messages[index] = message
            return true
        }
        return false
    }

    override suspend fun deleteMessage(messageId: String): Boolean {
        delay(300)
        return messages.removeIf { it.id == messageId }
    }

    override suspend fun getMessagesByUser(userId: String): List<Message> {
        delay(300)
        return messages.filter { it.senderId == userId }
    }
}

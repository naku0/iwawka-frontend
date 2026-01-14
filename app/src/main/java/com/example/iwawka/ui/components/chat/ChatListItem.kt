package com.example.iwawka.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.iwawka.domain.models.Chat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ChatListItem(
    chat: Chat,
    onChatClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val formattedTime = formatChatTime(chat.timestamp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onChatClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier.size(56.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(cs.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.userName.take(2).uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            if (chat.isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(cs.primary)
                        .border(2.dp, cs.background, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Main
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.userName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = cs.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = chat.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurface.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Right: time + unread
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (chat.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .height(22.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(cs.primary)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formatUnreadCount(chat.unreadCount),
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.onPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


private fun formatChatTime(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        formatter.format(dateTime)
    } catch (e: Exception) {
        timestamp.takeLast(5)
    }
}

private fun formatUnreadCount(count: Int): String {
    return when {
        count > 99 -> "99+"
        count > 0 -> count.toString()
        else -> ""
    }
}
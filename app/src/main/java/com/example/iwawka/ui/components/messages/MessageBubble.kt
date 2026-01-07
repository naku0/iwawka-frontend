package com.example.iwawka.ui.components.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.iwawka.domain.models.Message

@Composable
fun MessageBubble(message: Message) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(14.dp)

    val containerModifier = if (message.isFromMe) {
        Modifier
            .clip(shape)
            .background(cs.primary)
    } else {
        Modifier
            .clip(shape)
            .background(cs.surface)
            .border(1.dp, cs.outline, shape)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .widthIn(max = 280.dp)
                .then(containerModifier)
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (message.isFromMe) cs.onPrimary else cs.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (message.isFromMe)
                        cs.onPrimary.copy(alpha = 0.7f)
                    else
                        cs.onSurface.copy(alpha = 0.5f)
                )

                if (message.isFromMe && message.isRead) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Прочитано",
                        modifier = Modifier.size(16.dp),
                        tint = cs.onPrimary.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

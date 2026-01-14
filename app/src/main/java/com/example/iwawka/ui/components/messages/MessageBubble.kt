package com.example.iwawka.ui.components.messages

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.iwawka.domain.models.Message
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun MessageBubble(
    message: Message,
    selectionMode: Boolean,
    isSelected: Boolean,
    onClick: (Message) ->  Unit,
    onLongClick: (Message) -> Unit,

    onReply: (Message) -> Unit,
    onCopy: (Message) -> Unit,
    onDelete: (Message) -> Unit,
    onExplain: (Message) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(14.dp)

    var menuExpanded by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(IntOffset.Zero) }

    val density = LocalDensity.current

    val baseContainer = if (message.isFromMe) {
        Modifier
            .clip(shape)
            .background(cs.primary.copy(alpha = 0.65f))
    } else {
        Modifier
            .clip(shape)
            .background(cs.surface)
            .border(1.dp, cs.outline, shape)
    }

    val selectionOverlay = if (selectionMode && isSelected){
        Modifier.border(2.dp, cs.primary, shape)
    } else Modifier

    var bubbleTopLeft by remember { mutableStateOf(Offset.Zero) }

    Row(
        modifier = Modifier.
            fillMaxWidth()
            .onGloballyPositioned{ coordinates ->
                bubbleTopLeft = coordinates.localToWindow(Offset.Zero)
            }
            .combinedClickable(
                onClick = {
                    if (selectionMode){
                        onClick(message)
                    } else {
                        val x = with(density) {bubbleTopLeft.x.toDp().value}
                        val y = with(density){bubbleTopLeft.y.toDp().value}

                        menuOffset = IntOffset(bubbleTopLeft.x.roundToInt(), bubbleTopLeft.y.roundToInt())
                        menuExpanded = true
                    }
                },
                onLongClick = {
                    onLongClick(message)
                }
            ),
        horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start

    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .widthIn(max = 280.dp)
                .then(baseContainer)
                .then(selectionOverlay)
                .then(
                    if (selectionMode && isSelected) {
                            Modifier.background(cs.primary.copy(alpha = 0.15f), shape)
                        } else {
                             Modifier
                        }
                )
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
                    text = formatTime(message.timestamp),
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

        if(!selectionMode){
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = {menuExpanded = false},
            ) {
                DropdownMenuItem(
                    text = {Text("Ответить")},
                    onClick = { menuExpanded = false; onReply(message)}
                )
                DropdownMenuItem(
                    text = {Text("Копировать")},
                    onClick = {menuExpanded = false; onCopy(message)}
                )
                DropdownMenuItem(
                    text = {Text("Объяснить")},
                    onClick = {menuExpanded = false; onExplain(message)}
                )
                if(message.isFromMe) {
                    DropdownMenuItem(
                        text = { Text("Удалить") },
                        onClick = { menuExpanded = false; onDelete(message) }
                    )
                }
            }
        }
    }
}

private fun formatTime(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        formatter.format(dateTime)
    } catch (e: Exception) {
        timestamp.takeLast(5)
    }
}

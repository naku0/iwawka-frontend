package com.example.iwawka.ui.components.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NavItem(
    val id: String,
    val label: String,
    val icon: ImageVector
)

val navItems = listOf<NavItem>(
    NavItem("profile", "Профиль", Icons.Default.Person),
    NavItem("messages", "Чаты", Icons.Default.Email),
    NavItem("settings", "Настройки", Icons.Default.Settings),
)
@Composable
fun NavBubble(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) cs.primary.copy(alpha = 0.12f) else cs.surface.copy(alpha = 0f),
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (isSelected) cs.primary else cs.onSurfaceVariant,
                modifier = Modifier
                    .size(22.dp)
            )
        }
    }
}

@Composable
fun NavigationBar(
    selectedItemId: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = shape,
            color = cs.surface.copy(alpha = 0.85f),
            tonalElevation = 6.dp,
            shadowElevation = 10.dp,
            border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.12f)),
            modifier = Modifier
                .wrapContentWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(72.dp)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                navItems.forEach { item ->
                    NavBubble(
                        item = item,
                        isSelected = selectedItemId == item.id,
                        onClick = { onItemSelected(item.id) },
                        modifier = Modifier.width(84.dp)
                    )
                }
            }
        }
    }
}

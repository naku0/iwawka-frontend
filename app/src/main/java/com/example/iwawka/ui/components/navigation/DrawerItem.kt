package com.example.iwawka.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val isExit: Boolean = false
)

val drawerItems = listOf<DrawerItem>(
    DrawerItem("profile", "Профиль", Icons.Default.Person),
    DrawerItem("messages", "Сообщения", Icons.Default.Email),
    DrawerItem("settings", "Настройки", Icons.Default.Settings),
    DrawerItem("logout", "Выйти", Icons.AutoMirrored.Filled.Logout, isExit = true)

)

@Composable
fun DrawerItem(
    item: DrawerItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    NavigationDrawerItem(
        label = {
            Text(
                text = item.label,
                color = if (isSelected) cs.primary else cs.onSurface
            )
        },
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (isSelected) cs.primary else cs.onSurface
            )
        },
        colors = androidx.compose.material3.NavigationDrawerItemDefaults.colors(
            selectedContainerColor = cs.primary.copy(alpha = 0.08f),
            unselectedContainerColor = cs.surface,
            selectedIconColor = cs.primary,
            unselectedIconColor = cs.onSurface,
            selectedTextColor = cs.primary,
            unselectedTextColor = cs.onSurface
        ),
        modifier = modifier
    )
}

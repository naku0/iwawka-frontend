package com.example.iwawka.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.iwawka.domain.models.User
import androidx.compose.foundation.layout.Spacer


@Composable
fun AppDrawer(
    currentScreen: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    user: User?,
    onLogout: () -> Unit
){
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, RectangleShape)
            .verticalScroll(rememberScrollState())
    ) {
        DrawerHeader(
            username = user?.name ?: "It's you!",
            phone = user?.phone ?: "Your number"
        )

        drawerItems.forEach { item ->
            if (!item.isExit){
                DrawerItem(
                    item = item,
                    isSelected = currentScreen == item.id,
                    onClick = {onItemClick(item.id)}
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        drawerItems.find { it.isExit }?.let { logoutItem ->
            DrawerItem(
                item = logoutItem,
                isSelected = false,
                onClick = {onLogout()}
            )
        }
    }
}
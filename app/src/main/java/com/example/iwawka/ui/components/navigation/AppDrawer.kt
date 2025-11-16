package com.example.iwawka.ui.components.navigation

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    currentScreen: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    username: String = "Artem Korolev",
    phone: String = "+79818365630"
){
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        DrawerHeader(
            username = username,
            phone = phone
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

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.weight(1f)
        )

        drawerItems.find { it.isExit }?.let { logoutItem ->
            DrawerItem(
                item = logoutItem,
                isSelected = false,
                onClick = {}
            )
        }
    }
}
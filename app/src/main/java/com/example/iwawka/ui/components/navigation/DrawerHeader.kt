package com.example.iwawka.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun DrawerHeader(
    modifier: Modifier = Modifier,
    username: String,
    phone: String
) {
    val cs = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(cs.surface)
            .border(1.dp, cs.outline, RectangleShape),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = username,
                style = MaterialTheme.typography.headlineMedium,
                color = cs.onSurface
            )
            Text(
                text = phone,
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

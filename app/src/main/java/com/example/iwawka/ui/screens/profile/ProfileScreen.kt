package com.example.iwawka.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.iwawka.ui.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    userId: String = "1",
    onEditProfile: () -> Unit = {},
    onProfileClick: (String) -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .padding(16.dp)
    ) {
        when {
            profileState.isLoading -> LoadingState()
            profileState.error != null -> {
                ErrorState(
                    error = profileState.error!!,
                    onRetry = { viewModel.loadProfile(userId) },
                    onDismiss = { viewModel.clearError() }
                )
            }
            profileState.profile != null -> {
                ProfileContent(
                    profile = profileState.profile!!,
                    onEditProfile = onEditProfile
                )
            }
            else -> EmptyState()
        }
    }
}

@Composable
private fun ProfileContent(
    profile: com.example.iwawka.domain.models.Profile,
    onEditProfile: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val blockShape = RoundedCornerShape(12.dp)

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar (минимализм: primary с прозрачностью)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(cs.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.user.name.take(2).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = cs.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Edit button (минимализм: outline + primary)
            OutlinedButton(
                onClick = onEditProfile
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    tint = cs.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Редактировать",
                    color = cs.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // User info block (поверхность + бордер, без теней)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(blockShape)
                .border(1.dp, cs.outline, blockShape),
            color = cs.surface,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileInfoItem(
                    icon = Icons.Default.Person,
                    title = "Имя",
                    value = profile.user.name
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileInfoItem(
                    icon = Icons.Default.Phone,
                    title = "Телефон",
                    value = profile.user.phone
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileInfoItem(
                    icon = Icons.Default.Info,
                    title = "Статус",
                    value = profile.user.status
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bio block
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(blockShape)
                .border(1.dp, cs.outline, blockShape),
            color = cs.surface,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "О себе",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurface.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = cs.primary)
    }
}

@Composable
private fun ErrorState(
    error: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            color = cs.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Попробовать снова")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onDismiss) {
            Text(
                text = "Закрыть",
                color = cs.primary
            )
        }
    }
}

@Composable
private fun EmptyState() {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Профиль не найден",
            color = cs.onBackground.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ProfileInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 2.dp),
            tint = cs.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

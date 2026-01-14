package com.example.iwawka.ui.screens.settings

import android.graphics.Color as AColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.iwawka.ui.components.common.ThemeSwitcher
import com.example.iwawka.ui.theme.AppState
import com.example.iwawka.ui.theme.LocalAppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
) {
    val appState = LocalAppState.current
    val cs = MaterialTheme.colorScheme

    val isDarkTheme = cs.background.luminance() < 0.5f
    val smartDefault = if (isDarkTheme) Color.White else Color.Black

    val presets = listOf(
        smartDefault,             // умный дефолт
        Color(0xFF2563EB),        // blue
        Color(0xFF16A34A),        // green
        Color(0xFFDC2626),        // red
        Color(0xFFF59E0B),        // amber
        Color(0xFF7C3AED),        // violet
        Color(0xFF06B6D4),        // cyan
        Color(0xFFEC4899),        // pink
    )

    var sheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        ThemeSwitcher()

        // Акцент — заголовок + кнопка палитры
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Акцент",
                style = MaterialTheme.typography.titleMedium,
                color = cs.onBackground
            )
            TextButton(onClick = { sheetOpen = true }) {
                Text("Открыть палитру")
            }
        }

        if (appState.recentAccents.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Недавние",
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurface.copy(alpha = 0.7f)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    appState.recentAccents.take(6).forEach { stored ->
                        val isMarker = stored == AppState.ACCENT_SMART_DEFAULT
                        val argb = if(isMarker) smartDefault.toArgb() else stored
                        val c = Color(argb)
                        val selected = appState.accentArgb == argb
                        AccentDot(
                            color = c,
                            selected = selected,
                            onClick = {
                                appState.accentArgb = argb
                                appState.pushRecent(argb)
                            }
                        )
                    }
                }
            }
        }

        // Пресеты (сеткой 4x2 условно)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Пресеты",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurface.copy(alpha = 0.7f)
            )

            val rows = presets.chunked(4)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                rows.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { color ->
                            val isSmart =color.toArgb() == smartDefault.toArgb()
                            val selected = if (isSmart) appState.accentArgb == smartDefault.toArgb()
                                            else appState.accentArgb == color.toArgb()
                            AccentChip(
                                color = color,
                                selected = selected,
                                onClick = {
                                    if(isSmart){
                                        appState.accentArgb = smartDefault.toArgb()
                                        appState.pushRecent(AppState.ACCENT_SMART_DEFAULT)
                                    }
                                    else {
                                        val argb = color.toArgb()
                                        appState.accentArgb = argb
                                        appState.pushRecent(argb)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Превью
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Превью",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurface.copy(alpha = 0.7f)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable{sheetOpen = true}
                    .border(1.dp, cs.outline, RoundedCornerShape(14.dp))
                    .background(Color(appState.accentArgb))
            )
        }

        Text(
            text = "Другие настройки появятся здесь",
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onSurface.copy(alpha = 0.6f)
        )
    }

    if (sheetOpen) {
        AccentPickerBottomSheet(
            initialColor = Color(appState.accentArgb),
            sheetState = sheetState,
            onDismiss = { sheetOpen = false },
            onApply = { picked ->
                val argb = picked.toArgb()
                val smartArgb = smartDefault.toArgb()
                appState.accentArgb = argb
                if (argb == smartArgb){
                    appState.pushRecent(smartArgb)
                } else {
                    appState.pushRecent(argb)
                }
                sheetOpen = false
            }
        )
    }
}

@Composable
private fun AccentDot(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = cs.onSurface.copy(alpha = if (selected) 1f else 0.25f),
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    )
}

@Composable
private fun AccentChip(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(color)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = cs.onSurface.copy(alpha = if (selected) 1f else 0.22f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccentPickerBottomSheet(
    initialColor: Color,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onApply: (Color) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    var hue by remember { mutableFloatStateOf(0f) }         // 0..360
    var saturation by remember { mutableFloatStateOf(1f) }  // 0..1
    var value by remember { mutableFloatStateOf(1f) }       // 0..1

    LaunchedEffect(initialColor) {
        val hsv = FloatArray(3)
        AColor.colorToHSV(initialColor.toArgb(), hsv)
        hue = hsv[0]
        saturation = hsv[1]
        value = hsv[2]
    }

    val picked = Color.hsv(hue, saturation, value)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = cs.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Палитра акцента",
                style = MaterialTheme.typography.titleMedium,
                color = cs.onSurface
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, cs.outline, RoundedCornerShape(14.dp))
                    .background(picked)
            )

            Text("Hue", color = cs.onSurface.copy(alpha = 0.8f))
            Slider(value = hue, onValueChange = { hue = it }, valueRange = 0f..360f)

            Text("Saturation", color = cs.onSurface.copy(alpha = 0.8f))
            Slider(value = saturation, onValueChange = { saturation = it }, valueRange = 0f..1f)

            Text("Brightness", color = cs.onSurface.copy(alpha = 0.8f))
            Slider(value = value, onValueChange = { value = it }, valueRange = 0f..1f)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) { Text("Отмена") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { onApply(picked) }) { Text("Применить") }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

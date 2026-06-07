package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val JarvisColorScheme = darkColorScheme(
  primary = ArcBlue,
  onPrimary = DeepSpace,
  primaryContainer = ArcBlueDeep,
  onPrimaryContainer = HologramText,
  secondary = StarkGold,
  onSecondary = DeepSpace,
  tertiary = StarkRed,
  background = DeepSpace,
  onBackground = HologramText,
  surface = HologramDark,
  onSurface = HologramText,
  surfaceVariant = HologramDark,
  onSurfaceVariant = ArcBlueGlow,
  outline = HologramBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force sci-fi dark theme for the Stark Experience
  dynamicColor: Boolean = false, // Disable to preserve Jarvis Neon styling
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = JarvisColorScheme,
    typography = Typography,
    content = content
  )
}

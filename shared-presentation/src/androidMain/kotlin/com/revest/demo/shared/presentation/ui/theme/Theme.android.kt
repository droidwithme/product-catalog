package com.revest.demo.shared.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * A [RevestAppTheme] that uses the provided [dynamicColor] and [content].
 */
@Composable
internal actual fun RevestAppTheme(
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> RevestAppThemeDefaults.colorScheme
    }

    RevestAppTheme(
        colorScheme = colorScheme,
        content = content
    )
}

/**
 * Returns whether the dynamic theme is available.
 */
internal actual fun isDynamicThemeAvailable(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

package com.revest.demo.shared.presentation.ui.theme

import androidx.compose.runtime.Composable

/**
 * A [RevestAppTheme] that uses the provided [dynamicColor] and [content].
 */
@Composable
internal actual fun RevestAppTheme(
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    RevestAppTheme(content = content)
}

/**
 * Returns whether the dynamic theme is available.
 */
internal actual fun isDynamicThemeAvailable(): Boolean = false

package com.revest.demo.shared.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

/**
 * A [RevestAppTheme] that uses the provided [colorScheme], [shapes], and [typography].
 */
@Composable
internal fun RevestAppTheme(
    colorScheme: ColorScheme = RevestAppThemeDefaults.colorScheme,
    shapes: Shapes = RevestAppThemeDefaults.shapes,
    typography: Typography = RevestAppThemeDefaults.typography,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
        content = content
    )
}

/**
 * A [RevestAppTheme] that uses the provided [dynamicColor] and [content].
 */
@Composable
internal expect fun RevestAppTheme(
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
)

/**
 * Returns whether the dynamic theme is available.
 */
internal expect fun isDynamicThemeAvailable(): Boolean

/**
 * The default values used for [RevestAppTheme].
 */
internal object RevestAppThemeDefaults {

    /**
     * The default [ColorScheme] used for [RevestAppTheme].
     */
    val colorScheme: ColorScheme
        @Composable get() = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme

    /**
     * The default [Shapes] used for [RevestAppTheme].
     */
    val shapes: Shapes
        @Composable get() = MaterialTheme.shapes

    /**
     * The default [Typography] used for [RevestAppTheme].
     */
    val typography: Typography
        @Composable get() = MaterialTheme.typography

}

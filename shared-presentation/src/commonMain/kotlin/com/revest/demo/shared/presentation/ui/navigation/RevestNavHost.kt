package com.revest.demo.shared.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.revest.demo.shared.presentation.ui.screen.Screen
import com.revest.demo.shared.presentation.ui.screen.detail.ProductDetailScreen
import com.revest.demo.shared.presentation.ui.screen.home.HomeScreen
import com.revest.demo.shared.presentation.ui.screen.image.FullscreenImageScreen

@Composable
internal fun RevestNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HomeScreen.route(params = Unit),
) {
    val screens: List<Screen<*>> = listOf(
        HomeScreen,
        ProductDetailScreen,
        FullscreenImageScreen,
    )

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        screens.forEach { screen ->
            with(screen) {
                composable(navController = navController)
            }
        }
    }
}

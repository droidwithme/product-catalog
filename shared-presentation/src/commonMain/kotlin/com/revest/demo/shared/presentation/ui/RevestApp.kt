package com.revest.demo.shared.presentation.ui

import androidx.compose.runtime.Composable
import com.revest.demo.shared.presentation.ui.navigation.RevestNavHost
import com.revest.demo.shared.presentation.ui.theme.RevestAppTheme
import org.koin.compose.KoinContext

@Composable
public fun RevestApp(): Unit = KoinContext {


    RevestAppTheme() {
        RevestNavHost()
    }
}

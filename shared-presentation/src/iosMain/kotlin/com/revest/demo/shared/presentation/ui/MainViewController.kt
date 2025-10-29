package com.revest.demo.shared.presentation.ui

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

@Suppress("FunctionName")
public fun MainViewController(): UIViewController = ComposeUIViewController(configure = {
    enforceStrictPlistSanityCheck = false
}) {
    RevestApp()
}

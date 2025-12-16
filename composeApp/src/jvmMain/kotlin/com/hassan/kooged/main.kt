package com.hassan.kooged

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hassan.kooged.di.initKoin

fun main() {
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Kooged",
        ) {
            App()
        }
    }
}
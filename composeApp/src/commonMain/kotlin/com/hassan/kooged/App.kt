package com.hassan.kooged

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hassan.kooged.navigation.NavigationRoot
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun App() {
    AppInternal()
}

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun AppInternal() {

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            NavigationRoot()
        }
    }
}

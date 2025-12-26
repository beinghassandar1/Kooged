package com.hassan.kooged.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hassan.kooged.navigation.Route

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
//    viewModel: CalculatorAgentViewModel = koinViewModel(),
    goToScreen: (Route) -> Unit,
) {
    Content(
        modifier = modifier,
        goToScreen = goToScreen
    )
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    goToScreen: (Route) -> Unit,
) {

    Scaffold(
        modifier = modifier,
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    Button(
                        content = {
                            Text(text = "Complete Sentence Agent")
                        },
                        onClick = {
                            goToScreen(Route.SimpleAgent)
                        }
                    )
                }

                item {
                    Button(
                        content = {
                            Text(text = "Test Generator Agent")
                        },
                        onClick = {
                            goToScreen(Route.TestGeneratorAgent)
                        }
                    )
                }

                item {
                    Button(
                        content = {
                            Text(text = "Text Moderation Agent")
                        },
                        onClick = {
                            goToScreen(Route.TextModerationAgent)
                        }
                    )
                }

                item {
                    Button(
                        content = {
                            Text(text = "Image Moderation Agent")
                        },
                        onClick = {
                            goToScreen(Route.ImageModerationAgent)
                        }
                    )
                }

            }
        }
    )


}
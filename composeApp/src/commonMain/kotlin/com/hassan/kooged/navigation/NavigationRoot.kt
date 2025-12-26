package com.hassan.kooged.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.hassan.kooged.agents.completeSentences.screens.CompleteSentenceAgentsScreen
import com.hassan.kooged.agents.imageModeration.screens.ImageModerationAgentScreen
import com.hassan.kooged.agents.practiceLanguage.screens.TestGeneratorAgentsScreen
import com.hassan.kooged.agents.textModeration.screens.TextModerationAgentScreen
import com.hassan.kooged.screens.MainScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val rootBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.MainScreen::class, Route.MainScreen.serializer())
                    subclass(Route.SimpleAgent::class, Route.SimpleAgent.serializer())
                    subclass(
                        Route.TextModerationAgent::class,
                        Route.TextModerationAgent.serializer()
                    )
                    subclass(Route.TestGeneratorAgent::class, Route.TestGeneratorAgent.serializer())
                    subclass(
                        Route.ImageModerationAgent::class,
                        Route.ImageModerationAgent.serializer()
                    )
                }
            }
        },
        Route.MainScreen
    )
    NavDisplay(
        modifier = modifier,
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.MainScreen> {
                MainScreen(
                    goToScreen = {
                        rootBackStack.add(it)
                    }
                )
            }
            entry<Route.SimpleAgent> {
                CompleteSentenceAgentsScreen(
                    goBack = {
                        rootBackStack.removeLastOrNull()
                    }
                )

            }
            entry<Route.TestGeneratorAgent> {
                TestGeneratorAgentsScreen(
                    goBack = {
                        rootBackStack.removeLastOrNull()
                    }
                )
            }
            entry<Route.TextModerationAgent> {
                TextModerationAgentScreen(
                    goBack = {
                        rootBackStack.removeLastOrNull()
                    }
                )
            }

            entry<Route.ImageModerationAgent> {
                ImageModerationAgentScreen(
                    goBack = {
                        rootBackStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
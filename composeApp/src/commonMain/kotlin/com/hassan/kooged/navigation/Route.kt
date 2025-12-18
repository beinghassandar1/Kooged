package com.hassan.kooged.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object MainScreen : Route

    @Serializable
    data object SimpleAgent : Route
}
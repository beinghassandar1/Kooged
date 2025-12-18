package com.hassan.kooged.agents.completeSentences

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SimpleWeatherForecast")
@LLMDescription("Output for the agent that completes sentences")
data class SentenceSuggestionOutputEntity(
    @property:LLMDescription("Suggestion for completing the sentence")
    val suggestion: String,
    @property:LLMDescription("Translation of the suggestion")
    val translation: String,
)

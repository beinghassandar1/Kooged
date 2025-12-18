package com.hassan.kooged.agents.completeSentences

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CompleteSentenceSuggestions")
@LLMDescription("Output for the agent that completes sentences")
data class CompleteSentenceAgentOutput(
    @property:LLMDescription("List of suggestions for completing the sentence")
    val suggestions: List<SentenceSuggestionOutputEntity>
)

package com.hassan.kooged.agents

import ai.koog.agents.core.agent.AIAgent
import com.hassan.kooged.agents.completeSentences.CompleteSentenceAgentOutput

interface CompleteSentenceAgentProvider {

    val title: String
        get() = "Complete Sentence Agent"

    val description: String
        get() = "Agent that completes sentences"

    suspend fun provideAgent(
        onToolCallEvent: suspend (String) -> Unit,
        onErrorEvent: suspend (String) -> Unit,
        onAssistantMessage: suspend (String) -> String
    ): AIAgent<String, CompleteSentenceAgentOutput>
}

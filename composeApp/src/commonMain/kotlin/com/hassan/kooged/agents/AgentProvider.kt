package com.hassan.kooged.agents

import ai.koog.agents.core.agent.AIAgent

/**
 * Interface for agent factory
 */
interface AgentProvider {

    suspend fun provideAgent(
        onToolCallEvent: suspend (String) -> Unit,
        onErrorEvent: suspend (String) -> Unit,
        onAssistantMessage: suspend (String) -> String
    ): AIAgent<String, String>
}

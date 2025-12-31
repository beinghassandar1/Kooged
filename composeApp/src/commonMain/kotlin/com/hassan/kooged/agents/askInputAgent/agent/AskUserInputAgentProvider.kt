package com.hassan.kooged.agents.askInputAgent.agent

import ai.koog.agents.core.agent.AIAgent
import com.hassan.kooged.agents.askInputAgent.AskUserInputRequest
import com.hassan.kooged.agents.askInputAgent.AskUserInputResponse

/**
 * Interface for agent factory that uses AskUserInputTool
 */
interface AskUserInputAgentProvider {

    suspend fun provideAgent(
        onToolCallEvent: suspend (String) -> Unit,
        onErrorEvent: suspend (String) -> Unit,
        onUserInputRequested: suspend (AskUserInputRequest) -> AskUserInputResponse
    ): AIAgent<String, String>
}
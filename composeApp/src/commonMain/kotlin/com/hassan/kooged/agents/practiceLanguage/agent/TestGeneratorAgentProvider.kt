package com.hassan.kooged.agents.practiceLanguage.agent

import ai.koog.agents.core.agent.AIAgent
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentInput
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentOutput

interface TestGeneratorAgentProvider {

    val title: String
        get() = "Generate Test Agent"

    val description: String
        get() = "Agent that generates test from user's conversation history. The agent will analyse conversations and generate test based on the conversation"

    suspend fun provideAgent(
        onToolCallEvent: suspend (String) -> Unit,
        onErrorEvent: suspend (String) -> Unit,
        onAssistantMessage: suspend (String) -> String
    ): AIAgent<TestGeneratorAgentInput, TestGeneratorAgentOutput>

    suspend fun executeTest(
        inputData: TestGeneratorAgentInput,
    ): TestGeneratorResult
}
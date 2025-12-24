package com.hassan.kooged.agents.textModeration.agent

import ai.koog.prompt.dsl.ModerationResult

interface TextModerationAgentProvider {

    val title: String
        get() = "Text Moderation Agent"

    val description: String
        get() = "Agent that analyzes text and the gives the report its moderation result by using Moderation Model"


    suspend fun executeAgent(
        input: String,
    ): ModerationResult
}
package com.hassan.kooged.agents.imageModeration.agent

import ai.koog.prompt.dsl.ModerationResult

interface ImageModerationAgentProvider {

    val title: String
        get() = "Image Moderation Agent"

    val description: String
        get() = "Agent that analyzes Image and the gives the report its moderation result by using Moderation Model"


    suspend fun executeAgent(
        imageUrl: String,
    ): ModerationResult
}
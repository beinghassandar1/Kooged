package com.hassan.kooged.agents.practiceLanguage.entities

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName

@LLMDescription("Use's conversation history for practice language with partner")
@SerialName("TestGeneratorAgentInput")
data class TestGeneratorAgentInput(
    @SerialName("messages")
    @property:LLMDescription("List of messages in the conversation")
    val messages: List<Message>
)

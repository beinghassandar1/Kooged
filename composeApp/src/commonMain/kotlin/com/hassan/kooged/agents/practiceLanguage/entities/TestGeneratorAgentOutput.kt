package com.hassan.kooged.agents.practiceLanguage.entities

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("practiceTestOut")
@LLMDescription("Output for the agent that generates exercises for language practice")
data class TestGeneratorAgentOutput(
    @property:LLMDescription("List of question and answers for language practice")
    @SerialName("exercises")
    val exercises: List<TestGeneratorQuestionsEntity>
)
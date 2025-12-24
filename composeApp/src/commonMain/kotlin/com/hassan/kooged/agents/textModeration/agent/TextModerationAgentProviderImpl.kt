package com.hassan.kooged.agents.textModeration.agent

import ai.koog.prompt.dsl.ModerationResult
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import com.hassan.kooged.models.LlmContext

internal class TextModerationAgentProviderImpl(
    private val llmContext: LlmContext,
) : TextModerationAgentProvider {

    override suspend fun executeAgent(input: String): ModerationResult {

        val executor = SingleLLMPromptExecutor(llmClient = llmContext.llmClient)

        val result = executor.moderate(
            prompt = prompt("moderation-message-prompt") {
                system {
                    text("Moderate the given user input")
                }
                user {
                    text("Input: $input")
                }
            },
            model = llmContext.llmModel,
        )
        return result
    }
}
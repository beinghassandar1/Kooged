package com.hassan.kooged.agents.imageModeration.agent

import ai.koog.prompt.dsl.ModerationResult
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import com.hassan.kooged.models.LlmContext

internal class ImageModerationAgentProviderImpl(
    private val llmContext: LlmContext,
) : ImageModerationAgentProvider {

    override suspend fun executeAgent(imageUrl: String): ModerationResult {

        val executor = SingleLLMPromptExecutor(llmClient = llmContext.llmClient)

        val result = executor.moderate(
            prompt = prompt("moderation-message-prompt") {
                system {
                    text("Moderate the given user image")
                }
                user {
                    text("Input image by user is")
                    image(url = imageUrl)
                }
            },
            model = llmContext.llmModel,
        )
        return result
    }
}
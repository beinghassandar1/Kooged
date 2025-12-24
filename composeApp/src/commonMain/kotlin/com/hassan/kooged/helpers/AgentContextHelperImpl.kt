package com.hassan.kooged.helpers

import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.ollama.client.OllamaClient
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.llm.OllamaModels
import com.hassan.kooged.models.LlmContext

class AgentContextHelperImpl : AgentContextHelper {

    override fun getOllamaQwen06Client(): LlmContext {
        return LlmContext(
            llmClient = OllamaClient(),
            llmModel = OllamaModels.Alibaba.QWEN_3_06B
        )
    }

    override fun getOllamaQwen05Client(): LlmContext {
        return LlmContext(
            llmClient = OllamaClient(),
            llmModel = OllamaModels.Alibaba.QWEN_2_5_05B
        )
    }

    override fun getLlamaGuard3ModerationClient(): LlmContext {
        return LlmContext(
            llmClient = OllamaClient(),
            llmModel = OllamaModels.Meta.LLAMA_GUARD_3
        )
    }

    override fun getGeminiFlash205Client(): LlmContext {
        /**
         * Basic capabilities shared across all Gemini models
         */
        val standardCapabilities: List<LLMCapability> = listOf(
            LLMCapability.Temperature,
            LLMCapability.Completion,
            LLMCapability.MultipleChoices,
        )

        /**
         * Capabilities for models that support tools/function calling
         */
        val toolCapabilities: List<LLMCapability> = listOf(
            LLMCapability.Tools,
            LLMCapability.ToolChoice,
        )

        /**
         * Multimodal capabilities including vision (without tools)
         */
        val multimodalCapabilities: List<LLMCapability> =
            listOf(LLMCapability.Vision.Image, LLMCapability.Vision.Video, LLMCapability.Audio)

        /**
         * Native structured output capabilities
         */
        val structuredOutputCapabilities: List<LLMCapability.Schema.JSON> = listOf(
            LLMCapability.Schema.JSON.Basic,
            LLMCapability.Schema.JSON.Standard,
        )

        /**
         * Full capabilities including standard, multimodal, tools and native structured output
         */
        val fullCapabilities: List<LLMCapability> =
            standardCapabilities + multimodalCapabilities + toolCapabilities + structuredOutputCapabilities

        val Gemini3_Flash: LLModel = LLModel(
            provider = LLMProvider.Google,
            id = "gemini-3-flash-preview",
            capabilities = fullCapabilities,
            contextLength = 1_048_576,
            maxOutputTokens = 65_536,
        )

        return LlmContext(
            llmClient = GoogleLLMClient(apiKey = ""),
            llmModel = Gemini3_Flash
        )
    }


}

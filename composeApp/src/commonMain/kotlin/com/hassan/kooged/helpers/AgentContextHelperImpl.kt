package com.hassan.kooged.helpers

import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.ollama.client.OllamaClient
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.llm.OllamaModels
import com.hassan.kooged.AppConfig
import com.hassan.kooged.models.LlmContext

class AgentContextHelperImpl : AgentContextHelper {

    override fun getOllamaQwen06Client(): LlmContext {
        return LlmContext(llmClient = OllamaClient(), llmModel = OllamaModels.Alibaba.QWEN_3_06B)
    }

    override fun getOllamaQwen05Client(): LlmContext {
        return LlmContext(llmClient = OllamaClient(), llmModel = OllamaModels.Alibaba.QWEN_2_5_05B)
    }

    override fun getOllamaLlama3_2Client(): LlmContext {
        return LlmContext(llmClient = OllamaClient(), llmModel = OllamaModels.Meta.LLAMA_3_2)
    }

    override fun getLlamaGuard3ModerationClient(): LlmContext {
        return LlmContext(llmClient = OllamaClient(), llmModel = OllamaModels.Meta.LLAMA_GUARD_3)
    }

    override fun getOpenAiOmniModerationClient(): LlmContext {
        return LlmContext(
            llmClient = OpenAILLMClient(apiKey = AppConfig.OPEN_AI_API_KEY),
            llmModel = OpenAIModels.Moderation.Omni
        )
    }

    override fun getMedGemmaClient(): LlmContext {
        val standardCapabilities: List<LLMCapability> =
            listOf(
                LLMCapability.Temperature,
                LLMCapability.Completion,
                LLMCapability.MultipleChoices,
            )

        /** Capabilities for models that support tools/function calling */
        val toolCapabilities: List<LLMCapability> =
            listOf(
                LLMCapability.Tools,
                LLMCapability.ToolChoice,
            )

        /** Multimodal capabilities including vision (without tools) */
        val multimodalCapabilities: List<LLMCapability> =
            listOf(LLMCapability.Vision.Image, LLMCapability.Vision.Video, LLMCapability.Audio)

        /** Native structured output capabilities */
        val structuredOutputCapabilities: List<LLMCapability.Schema.JSON> =
            listOf(
                LLMCapability.Schema.JSON.Basic,
                LLMCapability.Schema.JSON.Standard,
            )

        /** Full capabilities including standard, multimodal, tools and native structured output */
        val fullCapabilities: List<LLMCapability> =
            standardCapabilities +
                    multimodalCapabilities +
                    toolCapabilities +
                    structuredOutputCapabilities
        val Med_Gemma_1_5_4b: LLModel =
            LLModel(
                provider = LLMProvider.Google,
                id = "medgemma-1.5-4b-it",
                capabilities = fullCapabilities,
                contextLength = 131_072,
//                maxOutputTokens = 65_536,
            )

        return LlmContext(
            llmClient = OllamaClient(baseUrl = "http://127.0.0.1:1234"),
            llmModel = Med_Gemma_1_5_4b
        )
    }

    override fun getGemini3FlashClient(): LlmContext {
        /** Basic capabilities shared across all Gemini models */
        val standardCapabilities: List<LLMCapability> =
            listOf(
                LLMCapability.Temperature,
                LLMCapability.Completion,
                LLMCapability.MultipleChoices,
            )

        /** Capabilities for models that support tools/function calling */
        val toolCapabilities: List<LLMCapability> =
            listOf(
                LLMCapability.Tools,
                LLMCapability.ToolChoice,
            )

        /** Multimodal capabilities including vision (without tools) */
        val multimodalCapabilities: List<LLMCapability> =
            listOf(LLMCapability.Vision.Image, LLMCapability.Vision.Video, LLMCapability.Audio)

        /** Native structured output capabilities */
        val structuredOutputCapabilities: List<LLMCapability.Schema.JSON> =
            listOf(
                LLMCapability.Schema.JSON.Basic,
                LLMCapability.Schema.JSON.Standard,
            )

        /** Full capabilities including standard, multimodal, tools and native structured output */
        val fullCapabilities: List<LLMCapability> =
            standardCapabilities +
                    multimodalCapabilities +
                    toolCapabilities +
                    structuredOutputCapabilities

        val Gemini3_Flash: LLModel =
            LLModel(
                provider = LLMProvider.Google,
                id = "gemini-3-flash-preview",
                capabilities = fullCapabilities,
                contextLength = 1_048_576,
                maxOutputTokens = 65_536,
            )
        return LlmContext(
            llmClient = GoogleLLMClient(apiKey = AppConfig.GOOGLE_API_KEY),
            llmModel = Gemini3_Flash
        )
    }
}

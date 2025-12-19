package com.hassan.kooged.helpers

import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.ollama.client.OllamaClient
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

    override fun getGeminiFlash205Client(): LlmContext {
        return LlmContext(
            llmClient = GoogleLLMClient(apiKey = ""),
            llmModel = GoogleModels.Gemini2_5Flash
        )
    }
}
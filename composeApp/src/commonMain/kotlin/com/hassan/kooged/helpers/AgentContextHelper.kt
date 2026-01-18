package com.hassan.kooged.helpers

import com.hassan.kooged.models.LlmContext

interface AgentContextHelper {
    fun getOllamaQwen06Client(): LlmContext
    fun getOllamaQwen05Client(): LlmContext
    fun getOllamaLlama3_2Client(): LlmContext
    fun getGemini3FlashClient(): LlmContext
    fun getLlamaGuard3ModerationClient(): LlmContext
    fun getOpenAiOmniModerationClient(): LlmContext
    fun getMedGemmaClient(): LlmContext
}
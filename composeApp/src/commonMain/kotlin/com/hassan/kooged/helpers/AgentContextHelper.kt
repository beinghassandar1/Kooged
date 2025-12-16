package com.hassan.kooged.helpers

import com.hassan.kooged.models.LlmContext

interface AgentContextHelper {
    fun getOllamaQwen06Client(): LlmContext
    fun getOllamaQwen05Client(): LlmContext
}
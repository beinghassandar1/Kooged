package com.hassan.kooged.models

import ai.koog.prompt.executor.clients.LLMClient
import ai.koog.prompt.llm.LLModel

data class LlmContext(
    val llmModel: LLModel,
    val llmClient: LLMClient,
)

package com.hassan.kooged.agents

import org.koin.core.annotation.Named

object AgentAnnotations {

    @Named
    annotation class CalculatorAgent

    @Named
    annotation class CompleteSentenceAgent

    @Named
    annotation class SimpleChatAgent


}
package com.hassan.kooged.agents.askInputAgent.agent

import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeExecuteTool
import ai.koog.agents.core.dsl.extension.nodeLLMRequest
import ai.koog.agents.core.dsl.extension.nodeLLMSendToolResult
import ai.koog.agents.core.dsl.extension.onAssistantMessage
import ai.koog.agents.core.dsl.extension.onToolCall

/*

val agentStrategy = strategy<String, String>("ask-user-input-agent") {
    // First LLM node: Agent decides to collect user information using tools
    val nodeCollectUserInfo by nodeLLMRequest()

    // Node to check if we have collected all necessary information
    val nodeCheckCompletion by node<List<Message.Response>, String> { messages ->
        val lastMessage = messages.lastOrNull()?.content ?: ""
        // Pass the conversation context forward
        lastMessage
    }
    val callAskUserTool by nodeExecuteTool()


    // Second LLM node: Generate one-liners for the movies after collection is complete
    val nodeGenerateOneLiners by nodeLLMRequestMultiple()

    // Final node to extract the result
    val nodeExtractResult by node<List<Message.Response>, String> { messages ->
        messages.firstOrNull()?.content ?: "No response"
    }

    // Flow: Start -> Collect user info with tools -> Generate one-liners -> Extract result -> Finish
    edge(nodeStart forwardTo nodeCollectUserInfo)
    edge(nodeCheckCompletion forwardTo callAskUserTool onToolCall { true })

    edge(nodeCheckCompletion forwardTo nodeGenerateOneLiners)
    edge(nodeGenerateOneLiners forwardTo nodeExtractResult)
    edge(nodeExtractResult forwardTo nodeFinish)
}
*/

val strategy1 = strategy<String, String>("test-strategy") {
    val nodeCallLLM by nodeLLMRequest()
    val nodeExecuteToolMultiple by nodeExecuteTool()
    val nodeSendToolResultMultiple by nodeLLMSendToolResult()

    edge(nodeStart forwardTo nodeCallLLM)

    edge((nodeCallLLM forwardTo nodeFinish) transformed { it } onAssistantMessage { true })

    edge(
        (nodeCallLLM forwardTo nodeExecuteToolMultiple) onToolCall { true })

    edge(
        nodeExecuteToolMultiple forwardTo nodeSendToolResultMultiple
    )

    edge(
        (nodeSendToolResultMultiple forwardTo nodeExecuteToolMultiple) onToolCall { true })

    edge((nodeSendToolResultMultiple forwardTo nodeFinish) transformed { it } onAssistantMessage { true })
}
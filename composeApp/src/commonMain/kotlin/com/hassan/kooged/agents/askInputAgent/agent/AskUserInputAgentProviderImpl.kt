package com.hassan.kooged.agents.askInputAgent.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.features.eventHandler.feature.handleEvents
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import co.touchlab.kermit.Logger
import com.hassan.kooged.agents.askInputAgent.AskUserInputRequest
import com.hassan.kooged.agents.askInputAgent.AskUserInputResponse
import com.hassan.kooged.agents.askInputAgent.AskUserInputTool
import com.hassan.kooged.agents.core.AiLoggingFeature
import com.hassan.kooged.models.LlmContext

/**
 * Factory for creating agents that can ask for user input
 */
class AskUserInputAgentProviderImpl(
    private val llmContext: LlmContext,
) : AskUserInputAgentProvider {

    override suspend fun provideAgent(
        onToolCallEvent: suspend (String) -> Unit,
        onErrorEvent: suspend (String) -> Unit,
        onUserInputRequested: suspend (AskUserInputRequest) -> AskUserInputResponse
    ): AIAgent<String, String> {

        val executor = SingleLLMPromptExecutor(llmClient = llmContext.llmClient)

        // Create the AskUserInputTool
        val askUserInputTool = AskUserInputTool(onUserInputRequested)

        // Create tool registry and register the tool
        val toolRegistry = ToolRegistry.Companion {
            tool(askUserInputTool)
        }

        // Create agent strategy

        // Create agent config
        val agentConfig = AIAgentConfig(
            prompt = prompt("prompt") {
                system(
                    content = """
                        You are a helpful AI assistant that gives the famous quote from the movies.
                        
                        Your task has TWO phases:
                        
                        PHASE 1 - Information Collection:
                        Use the 'ask_user_input' tool to collect:
                        1. User's name (STRING type) - Ask: "What is your name?"
                        2. Their favorite movies (LIST type) - Ask: "What are your favorite movies?"
                        3. One-Liners Contain explicit language (BOOLEAN type) - Ask: "Should it contain explicit language?"
                        
                        Tool usage guide:
                        - STRING: For single text responses (like name)
                        - LIST: For multiple items (like movie list)
                        - BOOLEAN: For yes/no questions (if confirmation needed)
                        
                        PHASE 2 - Generate One-Liners:
                        Once you have collected BOTH the name and the movie list, write the most famous line from that movie
                        for EACH movie in their list. Format the response as:
                        
                        "Hi [Name]! Here are your movie one-liners:
                        - [Movie 1]: [famous one-liner]
                        - [Movie 2]: [famous one-liner]
                        - [Movie 3]: [famous one-liner]
                        ..."
                        
                        Important: Do NOT generate one-liners until you have collected BOTH pieces of information.
                    """.trimIndent()
                )
            },
            model = llmContext.llmModel,
            maxAgentIterations = 50
        )

        // Return the agent
        return AIAgent.Companion<String, String>(
            promptExecutor = executor,
            strategy = myStrategyBasic,
            agentConfig = agentConfig,
            toolRegistry = toolRegistry,
        ) {

            install(AiLoggingFeature) {
                loggerName = "get-input-agent"
            }

            handleEvents {
                onToolCallFailed { ctx ->
                    Logger.d("Tool call failed: $ctx")

                }

                onToolCallStarting { ctx ->
                    onToolCallEvent("Tool ${ctx.toolName}, args ${ctx.toolArgs}")
                }

                onAgentExecutionFailed { ctx ->
                    onErrorEvent("${ctx.throwable.message}")
                }

                onAgentCompleted { _ ->
                    // Skip finish event handling
                }
            }
        }
    }
}
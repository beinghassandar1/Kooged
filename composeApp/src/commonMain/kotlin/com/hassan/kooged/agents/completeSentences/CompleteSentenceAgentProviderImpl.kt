package com.hassan.kooged.agents.completeSentences

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeLLMRequestMultiple
import ai.koog.agents.core.dsl.extension.onAssistantMessage
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.features.eventHandler.feature.handleEvents
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import com.hassan.kooged.agents.CompleteSentenceAgentProvider
import com.hassan.kooged.models.LlmContext
import com.hassan.kooged.utils.LanguageConstants
import kotlinx.serialization.json.Json
import org.koin.ext.getFullName


private fun buildPrompt(
    learningLanguage: String = LanguageConstants.LEARNING_LANGUAGE,
    nativeFluentLanguage: String = LanguageConstants.FLUENT_LANGUAGE
): String {
    val sampleOutput = CompleteSentenceAgentOutput(
        suggestions = listOf(
            SentenceSuggestionOutputEntity(
                suggestion = "example suggestion in learning language",
                translation = "example translation in fluent language"
            )
        )
    )

    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    val jsonFormat = json.encodeToString(
        CompleteSentenceAgentOutput.serializer(),
        sampleOutput
    )


    return """
    You are a helpful language assistant.
    User is a learning $learningLanguage language and is fluent in $nativeFluentLanguage. 
    You will be provided with the start of the sentence user is form in the learning language.
    Try to complete the sentence and provide at least 3 suggestions. 
    The provided suggestions should be in learning languages with translation in fluent Language.
    The only output should be in the following format:
    $jsonFormat
    """
}

/**
 * Factory for creating calculator agents
 */
internal class CompleteSentenceAgentProviderImpl(
    private val llmContext: LlmContext,
) : CompleteSentenceAgentProvider {

    override suspend fun provideAgent(
        onToolCallEvent: suspend (String) -> Unit,
        onErrorEvent: suspend (String) -> Unit,
        onAssistantMessage: suspend (String) -> String,
    ): AIAgent<String, CompleteSentenceAgentOutput> {
        val executor = SingleLLMPromptExecutor(llmClient = llmContext.llmClient)

        // Create empty tool registry - no tools needed for this agent
        val toolRegistry = ToolRegistry.Companion { }

        val strategy = strategy(CompleteSentenceAgentProvider::class.getFullName()) {
            // Node to request LLM response
            val nodeRequestLLM by nodeLLMRequestMultiple()

            // Node to parse the LLM response into CompleteSentenceAgentOutput
            val nodeParseOutput by node<String, CompleteSentenceAgentOutput> { jsonString ->
                // Parse the JSON string into CompleteSentenceAgentOutput
                val json = Json {
                    ignoreUnknownKeys = true
                }
                println(jsonString)
                json.decodeFromString<CompleteSentenceAgentOutput>(jsonString)
            }

            // Flow: Start -> Request LLM -> Parse output -> Finish
            // The initial user input (String) is automatically added as a user message
            edge(nodeStart forwardTo nodeRequestLLM)

            edge(
                nodeRequestLLM forwardTo nodeParseOutput
                        transformed { it.first() }
                        onAssistantMessage { true }
            )

            edge(nodeParseOutput forwardTo nodeFinish)
        }

        // Create agent config with proper prompt
        val agentConfig = AIAgentConfig(
            prompt = prompt("prompt") {
                system(
                    content = buildPrompt()
                )
            },
            model = llmContext.llmModel,
            maxAgentIterations = 50
        )

        // Return the agent
        return AIAgent<String, CompleteSentenceAgentOutput>(
            promptExecutor = executor,
            strategy = strategy,
            agentConfig = agentConfig,
            toolRegistry = toolRegistry,
        ) {
            handleEvents {
                onToolCallStarting { ctx ->
                    onToolCallEvent("Tool ${ctx.tool.name}, args ${ctx.toolArgs}")
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
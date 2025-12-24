package com.hassan.kooged.agents.practiceLanguage.agent

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
import ai.koog.prompt.structure.executeStructured
import ai.koog.prompt.text.text
import com.hassan.kooged.agents.practiceLanguage.entities.QuestionType
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentInput
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentOutput
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorQuestionsEntity
import com.hassan.kooged.models.LlmContext
import com.hassan.kooged.utils.LanguageConstants
import kotlinx.serialization.json.Json
import org.koin.ext.getFullName


private fun buildPrompt(
    learningLanguage: String = LanguageConstants.LEARNING_LANGUAGE,
    nativeFluentLanguage: String = LanguageConstants.FLUENT_LANGUAGE,
    examplesCount: Int = 20,
): String {

    return """
    You are a helpful language learning test generator.
    User is learning $learningLanguage language and is fluent in $nativeFluentLanguage. 
    You will be provided with conversation messages between the user and their language learning partner.
    Generate exactly $examplesCount diverse practice exercises based on the conversation to help the user practice and reinforce what they've learned.
    Create a variety of question types including: yes/no questions, multiple choice, fill in the blank, matching, translation, sentence ordering, and open-ended questions.
    The exercises should be in $learningLanguage with explanations/translations in $nativeFluentLanguage when appropriate.
    """
}

/**
 * Factory for creating test generator agents
 */
internal class TestGeneratorAgentProviderImpl(
    private val llmContext: LlmContext,
) : TestGeneratorAgentProvider {

    override suspend fun provideAgent(
        onToolCallEvent: suspend (String) -> Unit,
        onErrorEvent: suspend (String) -> Unit,
        onAssistantMessage: suspend (String) -> String
    ): AIAgent<TestGeneratorAgentInput, TestGeneratorAgentOutput> {

        val executor = SingleLLMPromptExecutor(llmClient = llmContext.llmClient)

        // Create empty tool registry - no tools needed for this agent
        val toolRegistry = ToolRegistry.Companion { }

        /*

                val agentStrategy =
                    strategy<TestGeneratorAgentInput, TestGeneratorAgentOutput>("test-generator") {
                        val prepareRequest by node<TestGeneratorAgentInput, String> { request ->
                            text {
                                +"Generating practice exercises based on conversation"
                                +"Number of messages: ${request.messageItems.size}"
                                +""
                                +"Conversation messages:"
                                request.messageItems.forEach { message ->
                                    +"${message.direction}: ${message.message}"
                                }
                            }

                        }

                        // Node to parse the LLM response into CompleteSentenceAgentOutput
                        val getStructuredOutput by node<String, TestGeneratorAgentOutput> { jsonString ->
                            // Parse the JSON string into CompleteSentenceAgentOutput
                            val json = Json {
                                ignoreUnknownKeys = true
                            }
                            println(jsonString)
                            json.decodeFromString<TestGeneratorAgentOutput>(jsonString)
                        }


        //             Simple API, let it figure out the optimal approach to get structured output itself.
        //             So only the structure has to be supplied.
                        val getStructuredOutput by nodeLLMRequestStructured<TestGeneratorAgentOutput>(
        //                 Optional: If the model you are using does not support native structured output, you can provide examples to help
        //                 it better understand the format.
                            examples = listOf(),
        //                 Optional: If the model provides inaccurate structure leading to serialization exceptions, you can try fixing
        //                 parser to attempt to fix malformed output.
                            fixingParser = StructureFixingParser(
                                fixingModel = OllamaModels.Alibaba.QWEN_3_06B,
                                retries = 2,
                            )
                        )

                        nodeStart then prepareRequest then getStructuredOutput
                        edge(getStructuredOutput forwardTo nodeFinish transformed { it })
                    }
        */

        val agentStrategy =
            strategy<TestGeneratorAgentInput, TestGeneratorAgentOutput>(TestGeneratorAgentProvider::class.getFullName()) {
                val prepareRequest by node<TestGeneratorAgentInput, String> { request ->
                    text {
                        +"Generating practice exercises based on conversation"
                        +"Number of messages: ${request.messageItems.size}"
                        +""
                        +"Conversation messages:"
                        request.messageItems.forEach { message ->
                            +"${message.direction}: ${message.message}"
                        }
                    }

                }

                // Node to request LLM response
                val nodeRequestLLM by nodeLLMRequestMultiple()

                // Node to parse the LLM response into CompleteSentenceAgentOutput
                val nodeParseOutput by node<String, TestGeneratorAgentOutput> { jsonString ->
                    // Parse the JSON string into CompleteSentenceAgentOutput
                    val json = Json {
                        ignoreUnknownKeys = true
                    }
                    val cleanJson = jsonString
                        .trim()
                        .removePrefix("```json")
                        .removePrefix("```")
                        .removeSuffix("```")
                        .trim()
                    println(cleanJson)
                    json.decodeFromString<TestGeneratorAgentOutput>(cleanJson)
                }

                // Flow: Start -> Request LLM -> Parse output -> Finish
                // The initial user input (String) is automatically added as a user message
                edge(nodeStart forwardTo prepareRequest)
                edge(prepareRequest forwardTo nodeRequestLLM)

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
        val agent = AIAgent<TestGeneratorAgentInput, TestGeneratorAgentOutput>(
            promptExecutor = executor,
            strategy = agentStrategy,
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
        return agent
    }

    override suspend fun executeTest(
        inputData: TestGeneratorAgentInput,
    ): TestGeneratorResult {

        val executor = SingleLLMPromptExecutor(llmClient = llmContext.llmClient)

        val result = executor.executeStructured<TestGeneratorAgentOutput>(
            prompt = prompt("prompt") {
                system(
                    content = buildPrompt()
                )
                user {
                    +"Generating practice exercises based on conversation"
                    +"Number of messages: ${inputData.messageItems.size}"
                    +""
                    +"Conversation messages:"
                    inputData.messageItems.forEach { message ->
                        +"${message.direction}: ${message.message}"
                    }
                }

            },

            model = llmContext.llmModel,
        )

        if (result.isFailure) {
            return TestGeneratorResult.Error(result.exceptionOrNull()?.message ?: "Error occurred")
        } else if (result.isSuccess) {
            val output = result.getOrThrow()
            val mappedList = mapTestGeneratorAgentOutputToData(output.structure)
            return TestGeneratorResult.Success(mappedList)
        } else {
            return TestGeneratorResult.Error("Something happened")
        }
    }
}

sealed interface TestGeneratorResult {
    data class Error(val message: String) : TestGeneratorResult
    data class Success(val data: List<TestGeneratorQuestionsEntity>) : TestGeneratorResult
}

private fun mapTestGeneratorAgentOutputToData(output: TestGeneratorAgentOutput): List<TestGeneratorQuestionsEntity> {
    return output.exercises.mapNotNull { apiQuestion ->
        when (apiQuestion.type) {
            QuestionType.YES_NO -> {
                apiQuestion.isAnswerTrue?.let { answer ->
                    TestGeneratorQuestionsEntity.YesNoQuestion(
                        question = apiQuestion.question,
                        answer = answer
                    )
                }
            }

            QuestionType.MULTIPLE_CHOICE -> {
                if (apiQuestion.options != null && apiQuestion.correctOptionIndex != null) {
                    TestGeneratorQuestionsEntity.MultipleChoiceQuestion(
                        question = apiQuestion.question,
                        options = apiQuestion.options,
                        correctAnswerIndex = apiQuestion.correctOptionIndex
                    )
                } else null
            }

            QuestionType.FILL_IN_THE_BLANK -> {
                apiQuestion.correctAnswerText?.let { answer ->
                    TestGeneratorQuestionsEntity.FillInTheBlankQuestion(
                        question = apiQuestion.question,
                        correctAnswer = answer
                    )
                }
            }

            QuestionType.MATCHING -> {
                apiQuestion.matchingPairs?.let { pairs ->
                    TestGeneratorQuestionsEntity.MatchingQuestion(
                        question = apiQuestion.question,
                        pairs = pairs
                    )
                }
            }

            QuestionType.TRANSLATION -> {
                if (apiQuestion.fromLanguage != null &&
                    apiQuestion.toLanguage != null &&
                    apiQuestion.correctAnswerText != null
                ) {
                    TestGeneratorQuestionsEntity.TranslationQuestion(
                        question = apiQuestion.question,
                        fromLanguage = apiQuestion.fromLanguage,
                        toLanguage = apiQuestion.toLanguage,
                        correctAnswer = apiQuestion.correctAnswerText
                    )
                } else null
            }

            QuestionType.SENTENCE_ORDERING -> {
                if (apiQuestion.options != null && apiQuestion.correctOrderIndices != null) {
                    TestGeneratorQuestionsEntity.SentenceOrderingQuestion(
                        question = apiQuestion.question,
                        shuffledWords = apiQuestion.options,
                        correctOrder = apiQuestion.correctOrderIndices
                    )
                } else null
            }

            QuestionType.OPEN_ENDED -> {
                apiQuestion.sampleAnswer?.let { sample ->
                    TestGeneratorQuestionsEntity.OpenEndedQuestion(
                        question = apiQuestion.question,
                        sampleAnswer = sample
                    )
                }
            }
        }
    }
}
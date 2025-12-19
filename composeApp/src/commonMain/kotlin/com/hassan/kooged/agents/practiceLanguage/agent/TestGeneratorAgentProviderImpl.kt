package com.hassan.kooged.agents.practiceLanguage.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeLLMRequestStructured
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.features.eventHandler.feature.handleEvents
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.structure.StructureFixingParser
import ai.koog.prompt.text.text
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentInput
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentOutput
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorQuestionsEntity
import com.hassan.kooged.models.LlmContext
import com.hassan.kooged.utils.LanguageConstants
import kotlinx.serialization.json.Json


private fun buildPrompt(
    learningLanguage: String = LanguageConstants.LEARNING_LANGUAGE,
    nativeFluentLanguage: String = LanguageConstants.FLUENT_LANGUAGE
): String {
    val sampleOutput = TestGeneratorAgentOutput(
        exercises = listOf(
            TestGeneratorQuestionsEntity.YesNoQuestion(
                question = "Is 'Hola' a greeting in Spanish?",
                answer = true
            ),
            TestGeneratorQuestionsEntity.MultipleChoiceQuestion(
                question = "What does 'Guten Morgen' mean in English?",
                options = listOf("Good evening", "Good morning", "Good night", "Good afternoon"),
                correctAnswerIndex = 1
            ),
            TestGeneratorQuestionsEntity.FillInTheBlankQuestion(
                question = "Je _____ français. (I speak French)",
                correctAnswer = "parle"
            ),
            TestGeneratorQuestionsEntity.MatchingQuestion(
                question = "Match the Spanish greetings with their English translations",
                pairs = mapOf(
                    "Buenos días" to "Good morning",
                    "Buenas tardes" to "Good afternoon",
                    "Buenas noches" to "Good night"
                )
            ),
            TestGeneratorQuestionsEntity.TranslationQuestion(
                question = "How are you?",
                fromLanguage = "English",
                toLanguage = "German",
                correctAnswer = "Wie geht es dir?"
            ),
            TestGeneratorQuestionsEntity.SentenceOrderingQuestion(
                question = "Put the words in the correct order to form a sentence",
                shuffledWords = listOf("ich", "Deutsch", "spreche"),
                correctOrder = listOf(0, 2, 1)
            ),
            TestGeneratorQuestionsEntity.OpenEndedQuestion(
                question = "Write a short paragraph introducing yourself in Spanish",
                sampleAnswer = "Hola, me llamo Juan. Tengo veinte años y vivo en Madrid. Me gusta aprender idiomas."
            )
        )
    )

    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    val jsonFormat = json.encodeToString(
        TestGeneratorAgentOutput.serializer(),
        sampleOutput
    )


    return """
    You are a helpful language learning test generator.
    User is learning $learningLanguage language and is fluent in $nativeFluentLanguage. 
    You will be provided with conversation messages between the user and their language learning partner.
    Generate diverse practice exercises based on the conversation to help the user practice and reinforce what they've learned.
    Create a variety of question types including: yes/no questions, multiple choice, fill in the blank, matching, translation, sentence ordering, and open-ended questions.
    The exercises should be in $learningLanguage with explanations/translations in $nativeFluentLanguage when appropriate.
    Only provide the exercises in JSON format following this structure:
    $jsonFormat
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


        val agentStrategy =
            strategy<TestGeneratorAgentInput, TestGeneratorAgentOutput>("test-generator") {
                val prepareRequest by node<TestGeneratorAgentInput, String> { request ->
                    text {
                        +"Generating practice exercises based on conversation"
                        +"Number of messages: ${request.messages.size}"
                        +""
                        +"Conversation messages:"
                        request.messages.forEach { message ->
                            +"${message.direction}: ${message.message}"
                        }
                    }

                }

                /*
             Simple API, let it figure out the optimal approach to get structured output itself.
             So only the structure has to be supplied.
                 */
                val getStructuredOutput by nodeLLMRequestStructured<TestGeneratorAgentOutput>(
                    /*
                 Optional: If the model you are using does not support native structured output, you can provide examples to help
                 it better understand the format.
                     */
                    examples = listOf(),
                    /*
                 Optional: If the model provides inaccurate structure leading to serialization exceptions, you can try fixing
                 parser to attempt to fix malformed output.
                     */
                    fixingParser = StructureFixingParser(
                        fixingModel = OllamaModels.Alibaba.QWEN_3_06B,
                        retries = 2
                    )
                )

                nodeStart then prepareRequest then getStructuredOutput
                edge(getStructuredOutput forwardTo nodeFinish transformed { it.getOrThrow().structure })
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
}
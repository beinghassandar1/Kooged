package com.hassan.kooged.agents.practiceLanguage.entities

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class TestGeneratorQuestionsApi {
}


/**
 * Enum representing the specific type of question.
 * This guides the LLM on which fields to populate in the main class.
 */
@Serializable
enum class QuestionType {
    @SerialName("yes_no")
    YES_NO,

    @SerialName("multiple_choice")
    MULTIPLE_CHOICE,

    @SerialName("fill_in_the_blank")
    FILL_IN_THE_BLANK,

    @SerialName("matching")
    MATCHING,

    @SerialName("translation")
    TRANSLATION,

    @SerialName("sentence_ordering")
    SENTENCE_ORDERING,

    @SerialName("open_ended")
    OPEN_ENDED
}

/**
 * A unified class representing any type of test question.
 * Fields are nullable because they are only populated if relevant to the [type].
 */
@Serializable
@SerialName("testQuestion")
@LLMDescription("A single class representing various types of test questions. Populate fields based on the selected 'type'.")
data class TestGeneratorQuestionApi(

    // --- Common Field ---
    @property:LLMDescription("The type of the question being asked")
    val type: QuestionType,

    @property:LLMDescription("The main text of the question")
    val question: String,

    // --- Specific Fields (Nullable) ---

    // For: YesNoQuestion
    @property:LLMDescription("Required for YES_NO. True if the statement is correct, False otherwise.")
    val isAnswerTrue: Boolean? = null,

    // For: MultipleChoiceQuestion & SentenceOrderingQuestion (shuffledWords)
    @property:LLMDescription("Required for MULTIPLE_CHOICE (choices) or SENTENCE_ORDERING (words to order).")
    val options: List<String>? = null,

    // For: MultipleChoiceQuestion
    @property:LLMDescription("Required for MULTIPLE_CHOICE. The index of the correct string in the 'options' list.")
    val correctOptionIndex: Int? = null,

    // For: FillInTheBlankQuestion & TranslationQuestion
    @property:LLMDescription("Required for FILL_IN_THE_BLANK and TRANSLATION. The exact text string expected as the answer.")
    val correctAnswerText: String? = null,

    // For: MatchingQuestion
    @property:LLMDescription("Required for MATCHING. A map of key-value pairs that need to be matched.")
    val matchingPairs: Map<String, String>? = null,

    // For: TranslationQuestion
    @property:LLMDescription("Required for TRANSLATION. The language code or name of the source text.")
    val fromLanguage: String? = null,

    @property:LLMDescription("Required for TRANSLATION. The language code or name of the target text.")
    val toLanguage: String? = null,

    // For: SentenceOrderingQuestion
    @property:LLMDescription("Required for SENTENCE_ORDERING. A list of indices representing the correct order of the 'options'.")
    val correctOrderIndices: List<Int>? = null,

    // For: OpenEndedQuestion
    @property:LLMDescription("Required for OPEN_ENDED. An example of a correct or high-quality response.")
    val sampleAnswer: String? = null
)
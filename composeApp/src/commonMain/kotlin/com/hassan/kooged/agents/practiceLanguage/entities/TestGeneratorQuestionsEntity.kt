package com.hassan.kooged.agents.practiceLanguage.entities

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("practiceQuestion")
@LLMDescription("question to practice with")
sealed interface TestGeneratorQuestionsEntity {

    @Serializable
    @SerialName("yesNoQuestion")
    @LLMDescription("True/False question")
    data class YesNoQuestion(
        val question: String,
        val answer: Boolean,
    ) : TestGeneratorQuestionsEntity

    @Serializable
    @SerialName("multipleChoiceQuestion")
    @LLMDescription("Multiple choice question with one correct answer")
    data class MultipleChoiceQuestion(
        val question: String,
        val options: List<String>,
        val correctAnswerIndex: Int,
    ) : TestGeneratorQuestionsEntity

    @Serializable
    @SerialName("fillInTheBlankQuestion")
    @LLMDescription("Fill in the blank question")
    data class FillInTheBlankQuestion(
        val question: String,
        val correctAnswer: String,
    ) : TestGeneratorQuestionsEntity

    @Serializable
    @SerialName("matchingQuestion")
    @LLMDescription("Match pairs of items")
    data class MatchingQuestion(
        val question: String,
        val pairs: Map<String, String>,
    ) : TestGeneratorQuestionsEntity

    @Serializable
    @SerialName("translationQuestion")
    @LLMDescription("Translate from one language to another")
    data class TranslationQuestion(
        val question: String,
        val fromLanguage: String,
        val toLanguage: String,
        val correctAnswer: String,
    ) : TestGeneratorQuestionsEntity

    @Serializable
    @SerialName("sentenceOrderingQuestion")
    @LLMDescription("Put words or phrases in correct order")
    data class SentenceOrderingQuestion(
        val question: String,
        val shuffledWords: List<String>,
        val correctOrder: List<Int>,
    ) : TestGeneratorQuestionsEntity

    @Serializable
    @SerialName("openEndedQuestion")
    @LLMDescription("Open-ended question requiring a text response")
    data class OpenEndedQuestion(
        val question: String,
        val sampleAnswer: String,
    ) : TestGeneratorQuestionsEntity

}
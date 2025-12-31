package com.hassan.kooged.agents.askInputAgent

import ai.koog.agents.core.tools.Tool
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Input type for the user input request
 */
@Serializable
enum class UserInputType {
    @SerialName("string")
    STRING,

    @SerialName("list")
    LIST,

    @SerialName("boolean")
    BOOLEAN
}

/**
 * Request parameters for asking user input
 */
@Serializable
data class AskUserInputRequest(
    val question: String,
    val inputType: UserInputType,
    val options: List<String>? = null // Optional, for LIST type to suggest options
)

/**
 * Response from user input
 */
@Serializable
data class AskUserInputResponse(
    val stringValue: String? = null,
    val listValue: List<String>? = null,
    val booleanValue: Boolean? = null,
    val inputType: UserInputType
)

/**
 * Tool for asking user input with support for String, List<String>, and Boolean values
 */
class AskUserInputTool(
    private val onUserInputRequested: suspend (AskUserInputRequest) -> AskUserInputResponse
) : Tool<AskUserInputRequest, AskUserInputResponse>(
    argsSerializer = AskUserInputRequest.serializer(),
    resultSerializer = AskUserInputResponse.serializer(),
    name = "ask_user_input",
    description = """
        Ask the user for input during conversation.
        Supports three input types:
        - STRING: For text input from user
        - LIST: For multiple text items (can provide options to choose from)
        - BOOLEAN: For yes/no questions
        
        Use this tool when you need to get information from the user to proceed with the task.
        Always provide a clear question that explains what you need from the user.
    """.trimIndent()
) {

    override suspend fun execute(args: AskUserInputRequest): AskUserInputResponse {
        // Validate request
        when (args.inputType) {
            UserInputType.STRING -> require(args.options == null) {
                "Options should not be provided for STRING input type"
            }

            UserInputType.LIST -> {
                // Options are optional for LIST type
            }

            UserInputType.BOOLEAN -> require(args.options == null) {
                "Options should not be provided for BOOLEAN input type"
            }
        }

        // Request user input through the callback
        val response = onUserInputRequested(args)

        // Validate response matches the requested type
        when (args.inputType) {
            UserInputType.STRING -> {
                require(response.stringValue != null && response.booleanValue == null && response.listValue == null) {
                    "Response must contain stringValue for STRING input type"
                }
            }

            UserInputType.LIST -> {
                require(response.listValue != null && response.stringValue == null && response.booleanValue == null) {
                    "Response must contain listValue for LIST input type"
                }
            }

            UserInputType.BOOLEAN -> {
                require(response.booleanValue != null && response.stringValue == null && response.listValue == null) {
                    "Response must contain booleanValue for BOOLEAN input type"
                }
            }
        }

        return response
    }
}


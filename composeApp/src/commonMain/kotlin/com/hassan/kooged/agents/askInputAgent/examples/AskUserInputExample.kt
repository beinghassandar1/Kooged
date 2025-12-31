package com.hassan.kooged.agents.askInputAgent.examples

import com.hassan.kooged.agents.askInputAgent.AskUserInputRequest
import com.hassan.kooged.agents.askInputAgent.AskUserInputResponse
import com.hassan.kooged.agents.askInputAgent.AskUserInputTool
import com.hassan.kooged.agents.askInputAgent.UserInputType
import com.hassan.kooged.agents.askInputAgent.agent.AskUserInputAgentProvider

/**
 * Example demonstrating how to use the AskUserInputTool
 *
 * This example shows how an agent can collect user profile information
 * using different input types.
 */
object AskUserInputExample {

    /**
     * Example: Collecting user profile information
     *
     * The agent will:
     * 1. Ask for the user's name (STRING)
     * 2. Ask for their favorite TV shows (LIST)
     * 3. Ask if they want to receive notifications (BOOLEAN)
     */
    suspend fun createUserProfile(
        agentProvider: AskUserInputAgentProvider
    ): String {
        // This would be called by the agent automatically through the tool
        // Here's what the requests would look like:

        // Step 1: Ask for name
        val nameRequest = AskUserInputRequest(
            question = "What is your name?",
            inputType = UserInputType.STRING
        )
        // Agent receives: AskUserInputResponse(stringValue = "John Doe", inputType = STRING)

        // Step 2: Ask for favorite TV shows
        val tvShowsRequest = AskUserInputRequest(
            question = "What are your favorite TV shows? Add as many as you like.",
            inputType = UserInputType.LIST,
            options = listOf(
                "Breaking Bad",
                "Game of Thrones",
                "The Office",
                "Stranger Things",
                "The Crown"
            )
        )
        // Agent receives: AskUserInputResponse(listValue = ["Breaking Bad", "Stranger Things"], inputType = LIST)

        // Step 3: Ask for notification preference
        val notificationRequest = AskUserInputRequest(
            question = "Would you like to receive notifications about new episodes?",
            inputType = UserInputType.BOOLEAN
        )
        // Agent receives: AskUserInputResponse(booleanValue = true, inputType = BOOLEAN)

        return """
            Profile created successfully!
            Name: John Doe
            Favorite Shows: Breaking Bad, Stranger Things
            Notifications: Enabled
        """.trimIndent()
    }

    /**
     * Example: Quiz or Survey
     *
     * The agent asks multiple questions to conduct a survey
     */
    suspend fun conductSurvey() {
        // Question 1: Multiple choice (using LIST)
        val favoriteColorRequest = AskUserInputRequest(
            question = "What is your favorite color?",
            inputType = UserInputType.STRING
        )

        // Question 2: Multi-select (using LIST)
        val hobbiesRequest = AskUserInputRequest(
            question = "Select your hobbies:",
            inputType = UserInputType.LIST,
            options = listOf("Reading", "Gaming", "Sports", "Cooking", "Travel")
        )

        // Question 3: Agreement (using BOOLEAN)
        val termsRequest = AskUserInputRequest(
            question = "Do you agree to the terms and conditions?",
            inputType = UserInputType.BOOLEAN
        )
    }

    /**
     * Example: Interactive Booking System
     *
     * The agent helps user book a service step by step
     */
    suspend fun bookService() {
        // Step 1: Get service type
        val serviceRequest = AskUserInputRequest(
            question = "What type of service do you need?",
            inputType = UserInputType.STRING
        )

        // Step 2: Select preferred dates
        val datesRequest = AskUserInputRequest(
            question = "What dates work for you? (Add all available dates)",
            inputType = UserInputType.LIST
        )

        // Step 3: Confirm booking
        val confirmRequest = AskUserInputRequest(
            question = "Would you like to confirm this booking?",
            inputType = UserInputType.BOOLEAN
        )
    }

    /**
     * Example: Validation Scenario
     *
     * Shows how to validate responses
     */
    fun validateResponse(response: AskUserInputResponse, request: AskUserInputRequest): Boolean {
        // Check if response type matches request type
        if (response.inputType != request.inputType) {
            return false
        }

        // Type-specific validation
        return when (request.inputType) {
            UserInputType.STRING -> {
                response.stringValue != null &&
                        response.stringValue.isNotBlank() &&
                        response.booleanValue == null &&
                        response.listValue == null
            }

            UserInputType.LIST -> {
                response.listValue != null &&
                        response.listValue.isNotEmpty() &&
                        response.stringValue == null &&
                        response.booleanValue == null
            }

            UserInputType.BOOLEAN -> {
                response.booleanValue != null &&
                        response.stringValue == null &&
                        response.listValue == null
            }
        }
    }

    /**
     * Example: Complex Workflow
     *
     * Shows how to chain multiple inputs for a complex task
     */
    suspend fun complexWorkflow() {
        // 1. Get user email
        val emailRequest = AskUserInputRequest(
            question = "What is your email address?",
            inputType = UserInputType.STRING
        )

        // 2. Get interests (with suggestions)
        val interestsRequest = AskUserInputRequest(
            question = "What are you interested in? (Select multiple)",
            inputType = UserInputType.LIST,
            options = listOf(
                "Technology",
                "Business",
                "Science",
                "Arts",
                "Sports",
                "Entertainment"
            )
        )

        // 3. Opt-in to newsletter
        val newsletterRequest = AskUserInputRequest(
            question = "Would you like to subscribe to our newsletter?",
            inputType = UserInputType.BOOLEAN
        )

        // 4. Get additional comments
        val commentsRequest = AskUserInputRequest(
            question = "Any additional comments or feedback?",
            inputType = UserInputType.STRING
        )
    }

    /**
     * Example: Error Handling
     *
     * Shows how to handle errors gracefully
     */
    suspend fun handleErrors(tool: AskUserInputTool) {
        try {
            val request = AskUserInputRequest(
                question = "What is your name?",
                inputType = UserInputType.STRING
            )

            val response = tool.execute(request)

            // Process the response
            when (response.inputType) {
                UserInputType.STRING -> {
                    val name = response.stringValue ?: throw IllegalStateException("Name is null")
                    println("Hello, $name!")
                }

                UserInputType.LIST -> {
                    val items = response.listValue ?: throw IllegalStateException("List is null")
                    println("Selected items: ${items.joinToString(", ")}")
                }

                UserInputType.BOOLEAN -> {
                    val choice =
                        response.booleanValue ?: throw IllegalStateException("Boolean is null")
                    println("User selected: ${if (choice) "Yes" else "No"}")
                }
            }
        } catch (e: IllegalArgumentException) {
            println("Validation error: ${e.message}")
        } catch (e: IllegalStateException) {
            println("State error: ${e.message}")
        } catch (e: Exception) {
            println("Unexpected error: ${e.message}")
        }
    }
}


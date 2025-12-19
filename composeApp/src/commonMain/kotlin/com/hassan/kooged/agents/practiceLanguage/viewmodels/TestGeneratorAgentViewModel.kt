package com.hassan.kooged.agents.practiceLanguage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassan.kooged.agents.practiceLanguage.agent.TestGeneratorAgentProvider
import com.hassan.kooged.agents.practiceLanguage.entities.MessageItem
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentInput
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentOutput
import com.hassan.kooged.agents.practiceLanguage.utils.UserMessagesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// Define message types for the chat
sealed class UiMessage {
    data class UserUiMessage(val text: String) : UiMessage()
    data class AgentUiMessage(val text: String) : UiMessage()
    data class SystemUiMessage(val text: String) : UiMessage()
    data class ErrorUiMessage(val text: String) : UiMessage()
    data class ToolCallUiMessage(val text: String) : UiMessage()
    data class ResultMessage(val result: TestGeneratorAgentOutput) : UiMessage()
}

// Define UI state for the agent demo screen
data class AgentDemoUiState(
    val title: String = "Agent Demo",
    val uiMessages: List<UiMessage> = listOf(UiMessage.SystemUiMessage("Hi, please start writing text")),
    val inputText: String = "Hallo, ich bin",
    val isInputEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val isChatEnded: Boolean = false,

    // For handling user responses when agent asks a question
    val userResponseRequested: Boolean = false,
    val currentUserResponse: String? = null,
)


class TestGeneratorAgentViewModel(
    private val agentProvider: TestGeneratorAgentProvider
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(
        AgentDemoUiState(
            title = agentProvider.title,
            uiMessages = listOf(UiMessage.SystemUiMessage(agentProvider.description))
        )
    )
    val uiState: StateFlow<AgentDemoUiState> = _uiState.asStateFlow()

    // Update input text
    fun updateInputText(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    // Send user message and start agent processing
    fun sendMessage() {
        val userInput = _uiState.value.inputText.trim()
        if (userInput.isEmpty()) return

        // If agent is waiting for a response to a question
        if (_uiState.value.userResponseRequested) {
            // Add user message to chat and update current response
            _uiState.update {
                it.copy(
                    uiMessages = it.uiMessages + UiMessage.UserUiMessage(userInput),
                    inputText = "",
                    isLoading = true,
                    userResponseRequested = false,
                    currentUserResponse = userInput
                )
            }
        } else { // Initial message flow - add user message and start agent
            _uiState.update {
                it.copy(
                    uiMessages = it.uiMessages + UiMessage.UserUiMessage(userInput),
                    inputText = "",
                    isInputEnabled = false,
                    isLoading = true
                )
            }

            // Start the agent processing
            viewModelScope.launch {
                runAgent(UserMessagesUtils.getMessages())
            }
        }
    }


    // Run the agent
    private suspend fun runAgent(messages: List<MessageItem>) {
        withContext(Dispatchers.Default) {
            try {
                // Create and run the agent using the factory
                val agent = agentProvider.provideAgent(
                    onToolCallEvent = { message ->
                        // Add tool call messages to the chat
                        viewModelScope.launch {
                            _uiState.update {
                                it.copy(
                                    uiMessages = it.uiMessages + UiMessage.ToolCallUiMessage(message)
                                )
                            }
                        }
                    },
                    onErrorEvent = { errorMessage ->
                        // Handle agent errors
                        viewModelScope.launch {
                            _uiState.update {
                                it.copy(
                                    uiMessages = it.uiMessages + UiMessage.ErrorUiMessage(
                                        errorMessage
                                    ),
                                    isInputEnabled = true,
                                    isLoading = false
                                )
                            }
                        }
                    },
                    onAssistantMessage = { message ->
                        // Handle agent asking user a question
                        _uiState.update {
                            it.copy(
                                uiMessages = it.uiMessages + UiMessage.AgentUiMessage(message),
                                isInputEnabled = true,
                                isLoading = false,
                                userResponseRequested = true
                            )
                        }

                        // Wait for user response
                        val userResponse = _uiState
                            .first { it.currentUserResponse != null }
                            .currentUserResponse
                            ?: throw IllegalArgumentException("User response is null")

                        // Update the state to reset current response
                        _uiState.update {
                            it.copy(
                                currentUserResponse = null
                            )
                        }

                        // Return it to the agent
                        userResponse
                    },
                )

                // Run the agent
                val result = agent.run(
                    TestGeneratorAgentInput(
                        messageItems = messages
                    )
                )

                println(result)

                // Update UI with final state and mark chat as ended
                _uiState.update {
                    it.copy(
                        uiMessages = it.uiMessages +
                                UiMessage.ResultMessage(result) +
                                UiMessage.SystemUiMessage("The agent has stopped."),
                        isInputEnabled = false,
                        isLoading = false,
                        isChatEnded = true
                    )
                }
            } catch (e: Exception) {
                // Handle errors
                _uiState.update {
                    it.copy(
                        uiMessages = it.uiMessages + UiMessage.ErrorUiMessage("Error: ${e.message}"),
                        isInputEnabled = true,
                        isLoading = false
                    )
                }
            }
        }
    }


    // Restart the chat
    fun restartChat() {
        _uiState.update {
            AgentDemoUiState(
                title = agentProvider.title,
                uiMessages = listOf(UiMessage.SystemUiMessage(agentProvider.description))
            )
        }
    }
}
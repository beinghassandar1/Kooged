package com.hassan.kooged.agents.askInputAgent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassan.kooged.agents.askInputAgent.AskUserInputRequest
import com.hassan.kooged.agents.askInputAgent.AskUserInputResponse
import com.hassan.kooged.agents.askInputAgent.agent.AskUserInputAgentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * UI State for the AskUserInput agent
 */
data class AskUserInputUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val pendingUserInputRequest: AskUserInputRequest? = null,
    val currentUserResponse: AskUserInputResponse? = null
)

/**
 * Sealed class representing different message types
 */
sealed class Message {
    data class UserMessage(val text: String) : Message()
    data class AgentMessage(val text: String) : Message()
    data class ToolCallMessage(val text: String) : Message()
    data class ErrorMessage(val text: String) : Message()
    data class UserInputRequestMessage(val request: AskUserInputRequest) : Message()
    data class UserInputResponseMessage(val response: AskUserInputResponse) : Message()
}

/**
 * ViewModel for the AskUserInput agent demo
 */
class AskUserInputAgentViewModel(
    private val agentProvider: AskUserInputAgentProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(AskUserInputUiState())
    val uiState: StateFlow<AskUserInputUiState> = _uiState

    init {
        viewModelScope.launch {
            sendMessage("Hello")
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            // Add user message to the chat
            _uiState.update {
                it.copy(
                    messages = it.messages + Message.UserMessage(text),
                    isLoading = true,
                )
            }

            // Run the agent
            runAgent(text)
        }
    }

    fun respondToUserInputRequest(response: AskUserInputResponse) {
        viewModelScope.launch {
            // Add the user's response to messages
            _uiState.update {
                it.copy(
                    messages = it.messages + Message.UserInputResponseMessage(response),
                    currentUserResponse = response,
                    pendingUserInputRequest = null
                )
            }
        }
    }

    private suspend fun runAgent(userInput: String) {
        withContext(Dispatchers.Default) {
            try {
                // Create and run the agent
                val agent = agentProvider.provideAgent(
                    onToolCallEvent = { message ->
                        // Add tool call messages to the chat
                        viewModelScope.launch {
                            _uiState.update {
                                it.copy(
                                    messages = it.messages + Message.ToolCallMessage(message)
                                )
                            }
                        }
                    },
                    onErrorEvent = { errorMessage ->
                        // Handle agent errors
                        viewModelScope.launch {
                            _uiState.update {
                                it.copy(
                                    messages = it.messages + Message.ErrorMessage(errorMessage),
                                    isLoading = false
                                )
                            }
                        }
                    },
                    onUserInputRequested = { request ->
                        // Show the input request UI to the user
                        _uiState.update {
                            it.copy(
                                messages = it.messages + Message.UserInputRequestMessage(request),
                                pendingUserInputRequest = request,
                                isLoading = false
                            )
                        }

                        // Wait for user response
                        val userResponse = _uiState
                            .first { it.currentUserResponse != null }
                            .currentUserResponse
                            ?: throw IllegalArgumentException("User response is null")

                        // Reset current response
                        _uiState.update {
                            it.copy(
                                currentUserResponse = null,
                                isLoading = true
                            )
                        }

                        userResponse
                    }
                )

                // Execute the agent
                val result = agent.run(userInput)

                // Add agent's final response to the chat
                _uiState.update {
                    it.copy(
                        messages = it.messages + Message.AgentMessage(result),
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                // Handle any exceptions
                _uiState.update {
                    it.copy(
                        messages = it.messages + Message.ErrorMessage("Error: ${e.message}"),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(messages = emptyList())
        }
    }
}


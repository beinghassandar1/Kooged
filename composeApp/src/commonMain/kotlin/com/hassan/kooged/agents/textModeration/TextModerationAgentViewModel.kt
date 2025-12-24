package com.hassan.kooged.agents.textModeration

import ai.koog.prompt.dsl.ModerationResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassan.kooged.agents.textModeration.agent.TextModerationAgentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface TextModerationState {
    data object Undefined : TextModerationState
    data object Loading : TextModerationState
    data class Error(val message: String) : TextModerationState
    data class Result(val result: ModerationResult) : TextModerationState
}

// Define UI state for the agent demo screen
data class TextModerationUiState(
    val title: String = "Text Moderation",
    val state: TextModerationState = TextModerationState.Undefined,
    val inputText: String = "",
)


class TextModerationAgentViewModel(
    private val agentProvider: TextModerationAgentProvider
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(
        TextModerationUiState(
            title = agentProvider.title,
        )
    )
    val uiState: StateFlow<TextModerationUiState> = _uiState.asStateFlow()

    // Send user message and start agent processing
    fun checkInput() {
        val input = _uiState.value.inputText
        if (input.isEmpty()) {
            return
        }

        _uiState.update {
            it.copy(
                state = TextModerationState.Loading,
            )
        }

        viewModelScope.launch {
            runAgent(input)
        }
    }

    // Run the agent
    private suspend fun runAgent(input: String) {
        withContext(Dispatchers.Default) {
            val result = agentProvider.executeAgent(
                input = input
            )
            _uiState.update {
                it.copy(
                    state = TextModerationState.Result(result)
                )
            }


        }
    }


    // Restart the chat
    fun restartChat() {
        _uiState.update {
            TextModerationUiState(
                title = agentProvider.title,
                state = TextModerationState.Undefined
            )
        }
    }

    fun onTextUpdate(text: String) {
        _uiState.update {
            it.copy(
                inputText = text
            )
        }
    }
}
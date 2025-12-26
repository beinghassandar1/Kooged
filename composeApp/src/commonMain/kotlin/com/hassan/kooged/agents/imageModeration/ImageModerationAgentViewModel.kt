package com.hassan.kooged.agents.imageModeration

import ai.koog.prompt.dsl.ModerationResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassan.kooged.agents.imageModeration.agent.ImageModerationAgentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface ImageModerationState {
    data object Undefined : ImageModerationState
    data object Loading : ImageModerationState
    data class Error(val message: String) : ImageModerationState
    data class Result(val result: ModerationResult) : ImageModerationState
}

// Define UI state for the agent demo screen
data class ImageModerationUiState(
    val title: String = "Image Moderation",
    val state: ImageModerationState = ImageModerationState.Undefined,
    val url: String = "",
)


class ImageModerationAgentViewModel(
    private val agentProvider: ImageModerationAgentProvider
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(
        ImageModerationUiState(
            title = agentProvider.title,
        )
    )
    val uiState: StateFlow<ImageModerationUiState> = _uiState.asStateFlow()

    // Send user message and start agent processing
    fun checkInput() {
        val input = _uiState.value.url
        if (input.isEmpty()) {
            return
        }

        _uiState.update {
            it.copy(
                state = ImageModerationState.Loading,
            )
        }

        viewModelScope.launch {
            runAgent(input)
        }
    }

    // Run the agent
    private suspend fun runAgent(imageUrl: String) {
        withContext(Dispatchers.Default) {

            try {
                val result = agentProvider.executeAgent(
                    imageUrl = imageUrl
                )
                _uiState.update {
                    it.copy(
                        state = ImageModerationState.Result(result)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        state = ImageModerationState.Error(e.message ?: "Unknown Error")
                    )
                }
            }


        }
    }


    // Restart the chat
    fun restartChat() {
        _uiState.update {
            ImageModerationUiState(
                title = agentProvider.title,
                state = ImageModerationState.Undefined
            )
        }
    }

    fun onTextUpdate(text: String) {
        _uiState.update {
            it.copy(
                url = text
            )
        }
    }
}
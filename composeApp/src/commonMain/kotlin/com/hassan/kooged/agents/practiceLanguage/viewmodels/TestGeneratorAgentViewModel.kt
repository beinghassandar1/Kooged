package com.hassan.kooged.agents.practiceLanguage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassan.kooged.agents.practiceLanguage.agent.TestGeneratorAgentProvider
import com.hassan.kooged.agents.practiceLanguage.agent.TestGeneratorResult
import com.hassan.kooged.agents.practiceLanguage.entities.MessageItem
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentInput
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorQuestionsEntity
import com.hassan.kooged.agents.practiceLanguage.utils.UserMessagesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface TestGeneratorState {
    data object Undefined : TestGeneratorState
    data object Loading : TestGeneratorState
    data class Error(val message: String) : TestGeneratorState
    data class Result(val result: List<TestGeneratorQuestionsEntity>) : TestGeneratorState
}

// Define UI state for the agent demo screen
data class AgentDemoUiState(
    val title: String = "Agent Demo",
    val state: TestGeneratorState = TestGeneratorState.Undefined,
)


class TestGeneratorAgentViewModel(
    private val agentProvider: TestGeneratorAgentProvider
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(
        AgentDemoUiState(
            title = agentProvider.title,
        )
    )
    val uiState: StateFlow<AgentDemoUiState> = _uiState.asStateFlow()

    // Send user message and start agent processing
    fun generateStuff() {
        _uiState.update {
            it.copy(
                state = TestGeneratorState.Loading,
            )
        }

        viewModelScope.launch {
            runAgentStructured(UserMessagesUtils.getMessages())
        }
    }

    // Run the agent
    private suspend fun runAgentStructured(messages: List<MessageItem>) {
        withContext(Dispatchers.Default) {
            val result = agentProvider.executeTest(
                inputData = TestGeneratorAgentInput(
                    messageItems = messages
                ),
            )
            _uiState.update {
                val newState = when (result) {
                    is TestGeneratorResult.Error -> TestGeneratorState.Error(result.message)
                    is TestGeneratorResult.Success -> TestGeneratorState.Result(result.data)
                }
                it.copy(
                    state = newState
                )
            }


        }
    }


    // Restart the chat
    fun restartChat() {
        _uiState.update {
            AgentDemoUiState(
                title = agentProvider.title,
                state = TestGeneratorState.Undefined
            )
        }
    }
}
package com.hassan.kooged.agents.practiceLanguage.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hassan.kooged.agents.practiceLanguage.composables.GeneratedTestResult
import com.hassan.kooged.agents.practiceLanguage.viewmodels.TestGeneratorAgentViewModel
import com.hassan.kooged.agents.practiceLanguage.viewmodels.TestGeneratorState
import com.hassan.kooged.agents.practiceLanguage.viewmodels.UiMessage
import kooged.composeapp.generated.resources.Res
import kooged.composeapp.generated.resources.outline_arrow_back_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun Preview() {
    Content(
        goBack = {},
        onGenerateTestClicked = {},
        messages = listOf(),
        state = TestGeneratorState.Undefined,
        onClearClicked = {}
    )
}

@Composable
fun TestGeneratorAgentsScreen(
    modifier: Modifier = Modifier,
    viewModel: TestGeneratorAgentViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val messages = uiState.uiMessages
    val state = uiState.state
    Content(
        messages = messages,
        state = state,
        onGenerateTestClicked = {
            viewModel.sendMessage()
        },
        onClearClicked = {
            viewModel.restartChat()
        },
        goBack = goBack,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    messages: List<UiMessage>,
    state: TestGeneratorState,
    onClearClicked: () -> Unit,
    onGenerateTestClicked: () -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Complete Sentence Agent") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            painter = painterResource(Res.drawable.outline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },

                )
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .imePadding()
                    .statusBarsPadding()
                    .padding(8.dp)
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                FlowRow {
                    Button(
                        content = {
                            Text("Generate stuff")
                        },
                        onClick = {
                            onGenerateTestClicked()
                        },
                        modifier = Modifier.wrapContentSize()
                    )
                    if (state is TestGeneratorState.Result || state is TestGeneratorState.Error) {
                        Button(
                            content = {
                                Text("Clear")
                            },
                            onClick = {
                                onClearClicked()
                            },
                            modifier = Modifier.wrapContentSize().padding(start = 16.dp)
                        )
                    }
                }


                when (state) {
                    is TestGeneratorState.Error -> {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    TestGeneratorState.Loading -> {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }

                    TestGeneratorState.Undefined -> {
                        Text(
                            text = "Undefined",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is TestGeneratorState.Result -> {
                        GeneratedTestResult(
                            data = state.result
                        )
                    }
                }

            }

        }
    )
}
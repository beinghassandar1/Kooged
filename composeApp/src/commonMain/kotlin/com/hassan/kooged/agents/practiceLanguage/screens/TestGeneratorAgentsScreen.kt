package com.hassan.kooged.agents.practiceLanguage.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hassan.kooged.agents.practiceLanguage.viewmodels.TestGeneratorAgentViewModel
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
        messages = listOf()
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
    Content(
        messages = messages,
        onGenerateTestClicked = {
            viewModel.sendMessage()
        },
        goBack = goBack,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    messages: List<UiMessage>,
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
                Button(
                    content = {
                        Text("Generate stuff")
                    },
                    onClick = {
                        onGenerateTestClicked()
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(messages) { message ->
                        Column {
                            Text(
                                text = message.toString(),
                                style = MaterialTheme.typography.labelSmall
                            )
                            when (message) {
                                is UiMessage.AgentUiMessage -> {
                                    Text(message.text)
                                }

                                is UiMessage.ErrorUiMessage -> {

                                    Text(message.text)

                                }

                                is UiMessage.ResultMessage -> {
                                    message.result.exercises.forEach { item ->
                                        Text(text = item.toString())

                                    }

                                }

                                is UiMessage.SystemUiMessage -> {

                                    Text(message.text)

                                }

                                is UiMessage.ToolCallUiMessage -> {

                                    Text(message.text)

                                }

                                is UiMessage.UserUiMessage -> {

                                    Text(message.text)

                                }
                            }

                        }
                    }

                }

            }

        }
    )
}
package com.hassan.kooged.agents.textModeration.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hassan.kooged.agents.textModeration.TextModerationAgentViewModel
import com.hassan.kooged.agents.textModeration.TextModerationState
import com.hassan.kooged.agents.textModeration.TextModerationUiState
import kooged.composeapp.generated.resources.Res
import kooged.composeapp.generated.resources.outline_arrow_back_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun Preview() {
    Content(
        uiState = TextModerationUiState(),
        goBack = {},
        checkInput = {},
        onClearClicked = {},
        onTextUpdate = {}

    )
}

@Composable
fun TextModerationAgentScreen(
    modifier: Modifier = Modifier,
    viewModel: TextModerationAgentViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    Content(
        uiState = uiState,
        checkInput = {
            viewModel.checkInput()
        },
        onClearClicked = {
            viewModel.restartChat()
        },
        onTextUpdate = { text ->
            viewModel.onTextUpdate(text)
        },
        goBack = goBack,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    uiState: TextModerationUiState,
    onClearClicked: () -> Unit,
    checkInput: () -> Unit,
    onTextUpdate: (String) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(uiState.title) },
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Input Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Enter text to moderate",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = uiState.inputText,
                            onValueChange = onTextUpdate,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Type your text here...") },
                            minLines = 4,
                            maxLines = 8
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = checkInput,
                                enabled = uiState.inputText.isNotBlank() && uiState.state !is TextModerationState.Loading,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Check Text")
                            }

                            OutlinedButton(
                                onClick = onClearClicked,
                                enabled = uiState.state !is TextModerationState.Loading,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Clear")
                            }
                        }
                    }
                }

                // Results Section
                when (val state = uiState.state) {
                    is TextModerationState.Loading -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                CircularProgressIndicator()
                                Text(
                                    text = "Analyzing text...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    is TextModerationState.Result -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Moderation Results",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                val result = state.result

                                // Flagged status
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Status:",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = if (result.isHarmful) "⚠️ Is Harmful" else "✅ Safe",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (result.isHarmful)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.primary
                                    )
                                }

                                // Categories
                                if (result.categories.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Categories:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    result.categories.forEach { (category, categoryResult) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth()
                                                .padding(start = 16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = category.name,
                                                style = MaterialTheme.typography.bodyMedium
                                            )

                                            val categoryResultStr by
                                            remember {
                                                derivedStateOf {
                                                    buildString {
                                                        append("Detected: ${if (categoryResult.detected) "✅" else "❌"}")
                                                        if (categoryResult.confidenceScore != null) {
                                                            append(", Confidence: ${categoryResult.confidenceScore}")
                                                        }
                                                        if (categoryResult.appliedInputTypes.isNotEmpty()) {
                                                            append(
                                                                ", Input Types: ${
                                                                    categoryResult.appliedInputTypes.joinToString(
                                                                        ", "
                                                                    )
                                                                }"
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            Text(
                                                text = categoryResultStr,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is TextModerationState.Error -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Error",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = state.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }

                    TextModerationState.Undefined -> {
                        // No results yet - show nothing or a placeholder
                    }
                }

            }

        }
    )
}
package com.hassan.kooged.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.hassan.kooged.agents.completeSentences.CompleteSentenceAgentOutput
import com.hassan.kooged.agents.completeSentences.SentenceSuggestionOutputEntity
import com.hassan.kooged.viewmodels.CompleteSentenceAgentViewModel
import com.hassan.kooged.viewmodels.Message
import com.jetbrains.example.koog.compose.theme.AppDimension
import kooged.composeapp.generated.resources.Res
import kooged.composeapp.generated.resources.outline_arrow_back_24
import kooged.composeapp.generated.resources.rounded_send_24
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun Preview() {
    Content(
        goBack = {},
        onTextChanged = {}
    )
}

@Composable
fun SimpleAgentScreen(
    modifier: Modifier = Modifier,
    viewModel: CompleteSentenceAgentViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    AgentDemoScreenContent(
        title = uiState.title,
        messages = uiState.messages,
        inputText = uiState.inputText,
        isInputEnabled = uiState.isInputEnabled,
        isLoading = uiState.isLoading,
        isChatEnded = uiState.isChatEnded,
        onInputTextChanged = viewModel::updateInputText,
        onSendClicked = viewModel::sendMessage,
        onRestartClicked = viewModel::restartChat,
        onNavigateBack = goBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    onTextChanged: (String) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    var debouncedText by remember { mutableStateOf("") }

    // Debounce the text input by 2 seconds
    LaunchedEffect(text) {
        delay(2000)
        debouncedText = text
    }

    // Call the function when debounced text changes
    LaunchedEffect(debouncedText) {
        if (debouncedText.isNotEmpty()) {
            onTextChanged(debouncedText)
        }
    }

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
                Text("Enter text...", style = MaterialTheme.typography.titleSmall)

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = { Text("Type here") },
                    placeholder = { Text("Start typing...") },
                    minLines = 3,
                )

            }

        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgentDemoScreenContent(
    title: String,
    messages: List<Message>,
    inputText: String,
    isInputEnabled: Boolean,
    isLoading: Boolean,
    isChatEnded: Boolean,
    onInputTextChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
    onRestartClicked: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Scroll to bottom when messages change
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(Res.drawable.outline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {
            // Messages list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = AppDimension.spacingMedium),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(AppDimension.spacingMedium)
            ) {
                items(messages) { message ->
                    when (message) {
                        is Message.UserMessage -> UserMessageBubble(message.text)
                        is Message.AgentMessage -> AgentMessageBubble(message.text)
                        is Message.SystemMessage -> SystemMessageItem(message.text)
                        is Message.ErrorMessage -> ErrorMessageItem(message.text)
                        is Message.ToolCallMessage -> ToolCallMessageItem(message.text)
                        is Message.ResultMessage -> ResultMessageItem(message.result)
                    }
                }

                // Add extra space at the bottom for better UX
                item {
                    Spacer(modifier = Modifier.height(AppDimension.spacingMedium))
                }
            }

            // Input area or restart button
            if (isChatEnded) {
                RestartButton(onRestartClicked = onRestartClicked)
            } else {
                InputArea(
                    text = inputText,
                    onTextChanged = onInputTextChanged,
                    onSendClicked = {
                        onSendClicked()
                        focusManager.clearFocus()
                    },
                    isEnabled = isInputEnabled,
                    isLoading = isLoading,
                    focusRequester = focusRequester
                )
            }
        }
    }
}

@Composable
private fun UserMessageBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(AppDimension.radiusExtraLarge))
                .background(MaterialTheme.colorScheme.primary)
                .padding(AppDimension.spacingMedium)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun AgentMessageBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(AppDimension.radiusExtraLarge))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(AppDimension.spacingMedium)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun SystemMessageItem(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppDimension.spacingMedium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ErrorMessageItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = "Error",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = AppDimension.spacingSmall)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(AppDimension.radiusExtraLarge))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(AppDimension.spacingMedium)
            ) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun ToolCallMessageItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = "Tool call",
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = AppDimension.spacingSmall)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(AppDimension.radiusExtraLarge))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(AppDimension.spacingMedium)
            ) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun ResultMessageItem(result: CompleteSentenceAgentOutput) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = "Result",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = AppDimension.spacingSmall)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(AppDimension.radiusExtraLarge))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(AppDimension.spacingMedium)
            ) {
                result.suggestions.forEachIndexed { index, entity ->
                    Text(
                        text = "Suggestion ${index + 1}: " + entity.suggestion,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Translation ${index + 1}: " + entity.translation,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun RestartButton(onRestartClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimension.spacingMedium, vertical = AppDimension.spacingSmall),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onRestartClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Start new chat")
        }
    }
}

@Composable
private fun InputArea(
    text: String,
    onTextChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
    isEnabled: Boolean,
    isLoading: Boolean,
    focusRequester: FocusRequester
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = AppDimension.elevationMedium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppDimension.spacingMedium,
                    vertical = AppDimension.spacingSmall
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text input field
            OutlinedTextField(
                value = text,
                onValueChange = onTextChanged,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                placeholder = { Text("Type a message...") },
                enabled = isEnabled,
                keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSendClicked() }),
                singleLine = true,
                shape = RoundedCornerShape(AppDimension.radiusRound)
            )

            Spacer(modifier = Modifier.width(AppDimension.spacingSmall))

            // Send button or loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(AppDimension.iconButtonSizeLarge)
                        .padding(AppDimension.spacingButtonPadding)
                )
            } else {
                IconButton(
                    onClick = onSendClicked,
                    enabled = isEnabled && text.isNotBlank(),
                    modifier = Modifier
                        .size(AppDimension.iconButtonSizeLarge)
                        .clip(CircleShape)
                        .background(
                            if (isEnabled && text.isNotBlank()) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.rounded_send_24),
                        contentDescription = "Send",
                        tint = if (isEnabled && text.isNotBlank()) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AgentDemoScreenPreview() {
    MaterialTheme {
        AgentDemoScreenContent(
            title = "Agent Demo",
            messages = listOf(
                Message.SystemMessage("Hi, I'm an agent that can help you"),
                Message.UserMessage("Hello!"),
                Message.ToolCallMessage("Tool example, args {a=2, b=2}"),
                Message.ResultMessage(
                    CompleteSentenceAgentOutput(
                        suggestions = listOf(
                            SentenceSuggestionOutputEntity(
                                suggestion = "Hallo, ich bin Hassan",
                                translation = "Hello, I am Hassan"
                            )
                        )
                    )
                ),
                Message.AgentMessage("Hello! How can I help you today?"),
                Message.ErrorMessage("Error: Something went wrong")
            ),
            inputText = "",
            isInputEnabled = true,
            isLoading = false,
            isChatEnded = false,
            onInputTextChanged = {},
            onSendClicked = {},
            onRestartClicked = {},
            onNavigateBack = {}
        )
    }
}

@Preview
@Composable
fun AgentDemoScreenEndedPreview() {
    MaterialTheme {
        AgentDemoScreenContent(
            title = "Agent Demo",
            messages = listOf(
                Message.SystemMessage("Hi, I'm an agent that can help you"),
                Message.UserMessage("Hello!"),
                Message.AgentMessage("Hello! How can I help you today?"),
                Message.SystemMessage("The agent has stopped.")
            ),
            inputText = "",
            isInputEnabled = false,
            isLoading = false,
            isChatEnded = true,
            onInputTextChanged = {},
            onSendClicked = {},
            onRestartClicked = {},
            onNavigateBack = {}
        )
    }
}

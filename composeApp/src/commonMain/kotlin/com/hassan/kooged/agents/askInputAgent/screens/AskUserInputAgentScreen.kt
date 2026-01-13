package com.hassan.kooged.agents.askInputAgent.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hassan.kooged.agents.askInputAgent.AskUserInputRequest
import com.hassan.kooged.agents.askInputAgent.AskUserInputResponse
import com.hassan.kooged.agents.askInputAgent.UserInputType
import com.hassan.kooged.agents.askInputAgent.viewmodel.AskUserInputAgentViewModel
import com.hassan.kooged.agents.askInputAgent.viewmodel.Message
import kooged.composeapp.generated.resources.Res
import kooged.composeapp.generated.resources.outline_arrow_back_24
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun Preview() {
    AskUserInputAgentScreen(
        goBack = {}
    )
}

@Composable
fun AskUserInputAgentScreen(
    modifier: Modifier = Modifier,
    viewModel: AskUserInputAgentViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    AskUserInputScreenContent(
        messages = uiState.messages,
        isLoading = uiState.isLoading,
        pendingUserInputRequest = uiState.pendingUserInputRequest,
        onSendMessage = viewModel::sendMessage,
        onUserInputResponse = viewModel::respondToUserInputRequest,
        onClearMessages = viewModel::clearMessages,
        onNavigateBack = goBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AskUserInputScreenContent(
    messages: List<Message>,
    isLoading: Boolean,
    pendingUserInputRequest: AskUserInputRequest?,
    onSendMessage: (String) -> Unit,
    onUserInputResponse: (AskUserInputResponse) -> Unit,
    onClearMessages: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ask User Input Agent Demo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(Res.drawable.outline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onClearMessages) {
                        Text("Clear")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Messages list
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                items(messages) { message ->
                    MessageItem(message)
                }

                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            // Show user input dialog if there's a pending request
            if (pendingUserInputRequest != null) {
                UserInputDialog(
                    request = pendingUserInputRequest,
                    onResponse = onUserInputResponse
                )
            }
        }
    }
}

@Composable
private fun MessageItem(message: Message) {
    when (message) {
        is Message.UserMessage -> UserMessageItem(message.text)
        is Message.AgentMessage -> AgentMessageItem(message.text)
        is Message.ToolCallMessage -> ToolCallMessageItem(message.text)
        is Message.ErrorMessage -> ErrorMessageItem(message.text)
        is Message.UserInputRequestMessage -> UserInputRequestMessageItem(message.request)
        is Message.UserInputResponseMessage -> UserInputResponseMessageItem(message.response)
    }
}

@Composable
private fun UserMessageItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = "UserMessageItem:$text",
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun AgentMessageItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = "AgentMessageItem:$text",
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun ToolCallMessageItem(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Tool Call",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun ErrorMessageItem(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Error: $text",
            modifier = Modifier.padding(12.dp),
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
private fun UserInputRequestMessageItem(request: AskUserInputRequest) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "🤖 Agent is asking:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = request.question,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Input type: ${request.inputType}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun UserInputResponseMessageItem(response: AskUserInputResponse) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Your response:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                when (response.inputType) {
                    UserInputType.STRING -> Text("\"${response.stringValue}\"")
                    UserInputType.LIST -> Text(response.listValue?.joinToString(", ") ?: "")
                    UserInputType.BOOLEAN -> Text(if (response.booleanValue == true) "Yes" else "No")
                }
            }
        }
    }
}

@Composable
private fun UserInputDialog(
    request: AskUserInputRequest,
    onResponse: (AskUserInputResponse) -> Unit
) {
    when (request.inputType) {
        UserInputType.STRING -> StringInputDialog(request, onResponse)
        UserInputType.LIST -> ListInputDialog(request, onResponse)
        UserInputType.BOOLEAN -> BooleanInputDialog(request, onResponse)
    }
}

@Composable
private fun StringInputDialog(
    request: AskUserInputRequest,
    onResponse: (AskUserInputResponse) -> Unit
) {
    var inputValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { },
        title = { Text("Input Required") },
        text = {
            Column {
                Text(request.question)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    placeholder = { Text("Enter text...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onResponse(
                        AskUserInputResponse(
                            stringValue = inputValue,
                            inputType = UserInputType.STRING
                        )
                    )
                },
                enabled = inputValue.isNotBlank()
            ) {
                Text("Submit")
            }
        }
    )
}

@Composable
private fun ListInputDialog(
    request: AskUserInputRequest,
    onResponse: (AskUserInputResponse) -> Unit
) {
    var inputValue by remember { mutableStateOf("") }
    var items by remember { mutableStateOf<List<String>>(emptyList()) }

    AlertDialog(
        onDismissRequest = { },
        title = { Text("Input Required") },
        text = {
            Column {
                Text(request.question)
                Spacer(modifier = Modifier.height(8.dp))
                if (request.options != null) {
                    Text(
                        "Suggestions: ${request.options.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Show current items
                if (items.isNotEmpty()) {
                    Text("Items:", style = MaterialTheme.typography.labelSmall)
                    items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item, modifier = Modifier.weight(1f))
                            TextButton(onClick = { items = items - item }) {
                                Text("Remove")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Input field for new item
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        placeholder = { Text("Add item...") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (inputValue.isNotBlank()) {
                                items = items + inputValue
                                inputValue = ""
                            }
                        },
                        enabled = inputValue.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onResponse(
                        AskUserInputResponse(
                            listValue = items,
                            inputType = UserInputType.LIST
                        )
                    )
                },
                enabled = items.isNotEmpty()
            ) {
                Text("Submit")
            }
        }
    )
}

@Composable
private fun BooleanInputDialog(
    request: AskUserInputRequest,
    onResponse: (AskUserInputResponse) -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Confirmation Required") },
        text = { Text(request.question) },
        confirmButton = {
            Button(
                onClick = {
                    onResponse(
                        AskUserInputResponse(
                            booleanValue = true,
                            inputType = UserInputType.BOOLEAN
                        )
                    )
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onResponse(
                        AskUserInputResponse(
                            booleanValue = false,
                            inputType = UserInputType.BOOLEAN
                        )
                    )
                }
            ) {
                Text("No")
            }
        }
    )
}


# Ask User Input Tool

A powerful tool for Koog agents that enables them to request different types of user input during
conversation flow. This tool allows agents to interactively gather information from users through a
Composable UI.

## Overview

The `AskUserInputTool` allows Koog agents to pause execution and request input from users. The tool
supports three types of input:

- **STRING**: Single text input from user
- **LIST**: Multiple text items or selections
- **BOOLEAN**: Yes/no questions

## Features

- ✅ **Type-safe input handling** with sealed data classes
- ✅ **Validation** for both requests and responses
- ✅ **Composable UI** with modern Material Design 3
- ✅ **Async/suspend support** for seamless integration
- ✅ **Flexible LIST input** with optional suggestions
- ✅ **Error handling** and user feedback

## Components

### 1. AskUserInputTool

The core tool that integrates with Koog's agent system.

**Key Features:**

- Serializable request/response models
- Type validation
- Callback-based user interaction

### 2. AskUserInputAgentProvider

Factory interface for creating agents with the AskUserInputTool.

### 3. AskUserInputAgentViewModel

ViewModel managing the UI state and agent interaction.

**Responsibilities:**

- Message management
- Agent lifecycle
- User input coordination
- Error handling

### 4. AskUserInputAgentScreen

Composable UI screen that displays the conversation and handles user input.

**Features:**

- Chat-like message display
- Dynamic input dialogs based on input type
- Auto-scrolling
- Loading states
- Error messages

## Usage

### Basic Example

```kotlin
// Create an agent that asks for user's profile information
val agent = agentProvider.provideAgent(
    onToolCallEvent = { message ->
        // Handle tool call events
        println("Tool called: $message")
    },
    onErrorEvent = { error ->
        // Handle errors
        println("Error: $error")
    },
    onUserInputRequested = { request ->
        // This callback is triggered when the agent needs user input
        // Display UI and wait for user response
        showInputDialog(request)
        // Return the user's response
        waitForUserResponse()
    }
)

// Run the agent
val result = agent.run("Create my profile")
```

### Input Types

#### STRING Input

Ask for single text values like name, email, etc.

```kotlin
AskUserInputRequest(
    question = "What is your name?",
    inputType = UserInputType.STRING
)

// Response
AskUserInputResponse(
    stringValue = "John Doe",
    inputType = UserInputType.STRING
)
```

#### LIST Input

Ask for multiple items or selections.

```kotlin
AskUserInputRequest(
    question = "What are your favorite TV shows?",
    inputType = UserInputType.LIST,
    options = listOf("Breaking Bad", "Game of Thrones", "The Office") // Optional suggestions
)

// Response
AskUserInputResponse(
    listValue = listOf("Breaking Bad", "Stranger Things"),
    inputType = UserInputType.LIST
)
```

#### BOOLEAN Input

Ask yes/no questions.

```kotlin
AskUserInputRequest(
    question = "Do you want to continue?",
    inputType = UserInputType.BOOLEAN
)

// Response
AskUserInputResponse(
    booleanValue = true,
    inputType = UserInputType.BOOLEAN
)
```

## Integration with Koog Agents

The tool is registered with the agent's ToolRegistry:

```kotlin
val askUserInputTool = AskUserInputTool(onUserInputRequested)

val toolRegistry = ToolRegistry {
    register(askUserInputTool)
}
```

The agent can then use the tool by calling it through the LLM's function calling capability. The LLM
will automatically invoke the tool when it needs user input.

## Agent Configuration

```kotlin
val agentConfig = AIAgentConfig(
    prompt = prompt("prompt") {
        system(
            content = """
                You are a helpful AI assistant that can ask users for information when needed.
                You have access to a tool called 'ask_user_input' that allows you to request different types of input:
                - STRING: For single text responses
                - LIST: For multiple items or selections
                - BOOLEAN: For yes/no questions
                
                Use this tool strategically to gather information needed to help the user.
            """.trimIndent()
        )
    },
    model = llmContext.llmModel,
    maxAgentIterations = 50
)
```

## UI Components

### Message Types

The screen displays different types of messages:

- **UserMessage**: Messages from the user
- **AgentMessage**: Responses from the agent
- **ToolCallMessage**: Debug information about tool calls
- **ErrorMessage**: Error messages
- **UserInputRequestMessage**: Displays what the agent is asking
- **UserInputResponseMessage**: Shows the user's response

### Input Dialogs

#### String Input Dialog

- Simple text field
- Submit button enabled when text is not blank

#### List Input Dialog

- Add/remove items dynamically
- Optional suggestions display
- Submit button enabled when list has at least one item

#### Boolean Input Dialog

- Yes/No buttons
- Clear and simple confirmation dialog

## Example Scenario

Here's a complete example of how the agent uses the tool:

1. **User**: "Create a profile for me"
2. **Agent**: *calls ask_user_input tool with STRING type*
3. **System**: Shows dialog "What is your name?"
4. **User**: Enters "John Doe"
5. **Agent**: *receives response, calls ask_user_input tool with LIST type*
6. **System**: Shows dialog "What are your favorite TV shows?"
7. **User**: Adds "Breaking Bad", "Stranger Things"
8. **Agent**: *receives response, calls ask_user_input tool with BOOLEAN type*
9. **System**: Shows dialog "Do you want to save this profile?"
10. **User**: Clicks "Yes"
11. **Agent**: "Profile created successfully for John Doe with favorite shows: Breaking Bad,
    Stranger Things"

## Architecture

```
┌─────────────────────────────────────┐
│   AskUserInputAgentScreen (UI)     │
│   - Displays messages               │
│   - Shows input dialogs             │
└──────────────┬──────────────────────┘
               │
               │ observes state
               │ calls functions
               │
┌──────────────▼──────────────────────┐
│   AskUserInputAgentViewModel       │
│   - Manages UI state                │
│   - Coordinates agent execution     │
│   - Handles user responses          │
└──────────────┬──────────────────────┘
               │
               │ uses
               │
┌──────────────▼──────────────────────┐
│   AskUserInputAgentProvider         │
│   - Creates configured agent        │
└──────────────┬──────────────────────┘
               │
               │ provides
               │
┌──────────────▼──────────────────────┐
│   AIAgent<String, String>           │
│   - Runs strategy                   │
│   - Executes tools                  │
└──────────────┬──────────────────────┘
               │
               │ uses
               │
┌──────────────▼──────────────────────┐
│   AskUserInputTool                  │
│   - Validates requests              │
│   - Invokes callback                │
│   - Returns response                │
└─────────────────────────────────────┘
```

## Data Models

### AskUserInputRequest

```kotlin
@Serializable
data class AskUserInputRequest(
    val question: String,           // The question to ask the user
    val inputType: UserInputType,   // Type of input expected
    val options: List<String>? = null  // Optional suggestions for LIST type
)
```

### AskUserInputResponse

```kotlin
@Serializable
data class AskUserInputResponse(
    val stringValue: String? = null,      // For STRING type
    val listValue: List<String>? = null,  // For LIST type
    val booleanValue: Boolean? = null,    // For BOOLEAN type
    val inputType: UserInputType          // Must match request type
)
```

### UserInputType

```kotlin
@Serializable
enum class UserInputType {
    @SerialName("string")
    STRING,
    @SerialName("list")
    LIST,
    @SerialName("boolean")
    BOOLEAN
}
```

## Error Handling

The tool includes comprehensive error handling:

1. **Request Validation**: Ensures options are only provided for appropriate types
2. **Response Validation**: Verifies the response matches the requested input type
3. **Exception Handling**: Catches and displays errors in the UI
4. **Null Safety**: All nullable fields are properly validated

## Best Practices

1. **Clear Questions**: Always provide clear, specific questions to the user
2. **Type Selection**: Choose the appropriate input type for your needs
3. **Error Messages**: Handle errors gracefully and inform the user
4. **User Experience**: Keep the conversation flow natural and intuitive
5. **Validation**: Validate user input before processing

## Testing

You can test the tool by:

1. Running the AskUserInputAgentScreen
2. Sending a message like "Create my profile"
3. Responding to the agent's input requests
4. Observing how the agent uses the information

## Future Enhancements

Potential improvements:

- Support for numeric input types
- Date/time pickers
- File upload support
- Multi-select for LIST type
- Input validation rules
- Custom input UI components

## Dependencies

- Koog Agents Core
- Kotlinx Serialization
- Compose Multiplatform
- Material 3
- Coroutines

## License

Part of the Kooged project.


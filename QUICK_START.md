# Quick Start Guide - Ask User Input Tool

## What is this?

The Ask User Input Tool allows Koog agents to interactively request information from users during a
conversation. The agent can ask for:

- **Text** (like names, emails, descriptions)
- **Lists** (like favorite foods, interests, multiple selections)
- **Yes/No** questions (like confirmations, preferences)

## How to Use

### 1. Navigate to the Demo Screen

The tool comes with a complete demo screen that you can access by navigating to:

```kotlin
AskUserInputAgentScreen(goBack = { /* your navigation */ })
```

### 2. Start a Conversation

Simply type a message like:

- "Create my user profile"
- "Help me plan a trip"
- "Generate a survey for me"

### 3. Respond to Agent's Questions

When the agent needs information, a dialog will appear asking for:

- **Text Input**: Type your answer and click Submit
- **List Input**: Add multiple items, then click Submit
- **Yes/No**: Click Yes or No

### 4. See the Result

The agent will use your inputs to complete the task!

## Example Conversation

**You:** "Create a profile for me"

**Agent:** (Shows text input dialog)
> "What is your name?"

**You:** "John Doe"

**Agent:** (Shows list input dialog)
> "What are your favorite TV shows?"

**You:** Adds "Breaking Bad", "Stranger Things"

**Agent:** (Shows yes/no dialog)
> "Do you want to save this profile?"

**You:** Clicks "Yes"

**Agent:** "Profile created successfully for John Doe with favorite shows: Breaking Bad, Stranger
Things"

## Integration into Your App

### Step 1: Add to Koin Module

```kotlin
val appModule = module {
    viewModel { AskUserInputAgentViewModel(get()) }
}
```

### Step 2: Add Navigation Route

```kotlin
composable("askUserInput") {
    AskUserInputAgentScreen(
        goBack = { navController.popBackStack() }
    )
}
```

### Step 3: Navigate to Screen

```kotlin
Button(onClick = { navController.navigate("askUserInput") }) {
    Text("Try Ask User Input Agent")
}
```

## Customizing the Agent

To customize the agent's behavior, modify `AskUserInputAgentProviderImpl.kt`:

### Change the System Prompt

```kotlin
system(
    content = """
        You are a specialized assistant for [YOUR USE CASE].
        Use the ask_user_input tool to gather [SPECIFIC INFORMATION].
    """.trimIndent()
)
```

### Adjust Max Iterations

```kotlin
maxAgentIterations = 50  // Change this number
```

### Change the Model

```kotlin
model = llmContext.llmModel  // Use different model from your context
```

## Common Use Cases

### 1. User Onboarding

```
User: "Set up my account"
Agent asks for: name, email, preferences
```

### 2. Data Collection

```
User: "Conduct a survey"
Agent asks: multiple questions with different types
```

### 3. Interactive Forms

```
User: "Book an appointment"
Agent asks: service, date, confirmation
```

### 4. Preferences Setup

```
User: "Configure my settings"
Agent asks: various preferences with yes/no and selections
```

## Tips for Best Results

1. **Be Clear**: Tell the agent what you want to do
2. **Be Specific**: More details = better questions from the agent
3. **Be Patient**: Let the agent ask all necessary questions
4. **Be Thorough**: Provide complete answers

## Troubleshooting

### Agent doesn't ask questions

- Make sure your prompt clearly indicates you need information
- Try being more explicit: "I need to provide information"

### Dialog doesn't appear

- Check that the ViewModel is properly injected
- Verify the agent provider is configured correctly

### Responses not working

- Ensure you're clicking Submit after entering data
- Check that you've entered valid data (non-empty for text, at least one item for list)

## What's Next?

- Explore the examples in `examples/AskUserInputExample.kt`
- Read the full documentation in `README.md`
- Customize the UI in `screens/AskUserInputAgentScreen.kt`
- Create your own agents that use this tool

## Need Help?

Check the following files:

- `README.md` - Complete documentation
- `examples/AskUserInputExample.kt` - Code examples
- `IMPLEMENTATION_SUMMARY.md` - Technical details

Happy coding! 🚀


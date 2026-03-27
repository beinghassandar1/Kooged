# Kooged 🤖

A hands-on research project exploring [Koog](https://github.com/JetBrains/koog) — JetBrains' official Kotlin AI Agent framework — through real-world, working agent prototypes. Built with **Kotlin Multiplatform** and **Compose Multiplatform**, running on Android, iOS, Desktop (JVM), and Web.

---

## 🎯 What is this?

Kooged is a collection of AI agent demos that validate Koog in production-style workflows. Each agent is a fully functional prototype that can be explored, run, and extended.

---

## 🧠 Agents

### 1. Complete Sentence Agent
> *Finish what you started.*

- **Input:** A partial sentence
- **Output:** The completed sentence (think Google Translate auto-suggest, but smarter)
- Demonstrates a simple single-turn LLM agent flow

---

### 2. Text Moderation Agent
> *Keep conversations safe.*

- Uses **LLAMA_GUARD_3** to classify whether text is safe or unsafe
- Returns violation categories and a confidence score for unsafe content
- Ideal for content filtering pipelines

---

### 3. Test Generator Agent
> *Turn any conversation into a quiz.*

- **Input:** A chat conversation or lesson transcript
- **Output:** A full practice test based on the discussed content
- Supports **7 question types:**
  - ✅ Yes / No
  - 🔘 Multiple Choice
  - ✏️ Fill in the Blanks
  - 🔗 Matching
  - 🌍 Translate
  - 🔀 Sentence Ordering
  - 💬 Open-Ended

---

### 4. Movies One-Liners Agent
> *Every movie, in one line.*

- Takes a movie title or description as input
- Returns a witty, memorable one-liner summary
- Showcases creative LLM generation with a focused prompt

---

### 5. Ask User Input Agent
> *Agent-driven interactive forms.*

- The agent drives a multi-turn conversation, asking users targeted questions
- Supports three structured input types:
  - 📝 **Text** — names, emails, descriptions
  - 📋 **List** — multiple selections or items
  - ✔️ **Boolean** — Yes/No confirmations
- Perfect for onboarding flows, surveys, bookings, and data collection
- See the [dedicated docs](./composeApp/src/commonMain/kotlin/com/hassan/kooged/agents/askInputAgent/README.md) for full details

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin Multiplatform |
| UI | Compose Multiplatform + Material 3 |
| AI Agents | [Koog](https://github.com/JetBrains/koog) (JetBrains) |
| LLM Providers | Google Gemini, OpenAI |
| DI | Koin |
| Serialization | Kotlinx Serialization |
| Async | Kotlin Coroutines |
| Logging | Kermit |

---

## 📱 Supported Platforms

| Platform | Status |
|---|---|
| Android | ✅ |
| iOS | ✅ |
| Desktop (JVM) | ✅ |
| Web (Wasm) | ✅ |
| Web (JS) | ✅ |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (Hedgehog or newer) or IntelliJ IDEA
- JDK 17+
- Xcode (for iOS)
- API keys for your LLM provider (Google Gemini or OpenAI)

### Configuration

Create a `local.properties` file in the root directory and add your API keys:

```properties
GOOGLE_API_KEY=your_google_api_key_here
OPEN_AI_API_KEY=your_openai_api_key_here
```

---

## 🔨 Build & Run

### Android
```shell
# macOS/Linux
./gradlew :composeApp:assembleDebug

# Windows
.\gradlew.bat :composeApp:assembleDebug
```

### Desktop (JVM)
```shell
# macOS/Linux
./gradlew :composeApp:run

# Windows
.\gradlew.bat :composeApp:run
```

### Web (Wasm — modern browsers)
```shell
# macOS/Linux
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Windows
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```

### Web (JS — wider compatibility)
```shell
# macOS/Linux
./gradlew :composeApp:jsBrowserDevelopmentRun

# Windows
.\gradlew.bat :composeApp:jsBrowserDevelopmentRun
```

### iOS

Open the [`/iosApp`](./iosApp) directory in Xcode and run from there, or use the run configuration in Android Studio / IntelliJ IDEA.

---

## 📂 Project Structure

```
Kooged/
├── composeApp/
│   └── src/
│       ├── commonMain/          # Shared code (all platforms)
│       │   └── kotlin/com/hassan/kooged/
│       │       ├── agents/      # All AI agent implementations
│       │       │   ├── completeSentences/
│       │       │   ├── textModeration/
│       │       │   ├── practiceLanguage/   # Test Generator
│       │       │   ├── imageModeration/
│       │       │   └── askInputAgent/
│       │       ├── screens/     # Compose UI screens
│       │       ├── navigation/  # Navigation routes
│       │       ├── di/          # Koin DI modules
│       │       └── theme/       # App theme
│       ├── androidMain/         # Android-specific code
│       ├── iosMain/             # iOS-specific code
│       └── jvmMain/             # Desktop-specific code
└── iosApp/                      # iOS application entry point
```

---

## 📖 Further Reading

- [Ask User Input Agent — Full Docs](./composeApp/src/commonMain/kotlin/com/hassan/kooged/agents/askInputAgent/README.md)
- [Quick Start Guide](./QUICK_START.md)
- [Implementation Summary](./IMPLEMENTATION_SUMMARY.md)
- [Koog Framework](https://github.com/JetBrains/koog)
- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform)

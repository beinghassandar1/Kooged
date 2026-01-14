This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop (JVM).

What I explored

I researched Koog, the official Kotlin AI Agent framework, and evaluated why it’s useful for building production AI workflows. The framework focuses on:

Predictability: replacing “guessing” with a clear action map

Reliability: resuming exactly where it left off after crashes

Efficiency: one logic system across cloud + mobile (Android/iOS)

Control: easily swapping AI providers (OpenAI / Google) without rewriting logic 

Koog’s differentiator is that it gives structured control over agent behavior, helping avoid unpredictable outputs and making it stable enough for real product usage.

What makes Koog special

From the research, Koog stands out for:

Logic-based control: better alignment with product/business rules

Production-ready stability: moving beyond “AI prototype” into reliable software

Universal integration: supports many use cases with consistent architecture across platforms



Strategy + Graph-based execution

Koog supports predefined strategies for different interaction types:

Single Run Strategy
For non-interactive tasks: run once → return final output.

Chat Agent Strategy
Designed for multi-step chat conversations and interactive flows.

What I built (Hands-on prototypes)

During the research, I built multiple working agents to validate Koog in real workflows:

1) Complete Sentence Agent

Input: partial sentence

Output: completed sentence (similar to Google Translate suggestion behavior) 

2) Text Moderation Agent

Uses LLAMA_GUARD_3 to classify whether text is safe

Returns violation categories + confidence score (if unsafe) 

3) Test Generator from Chat Conversation

Input: a chat conversation

Output: practice tests based on what was discussed

Supports multiple question types:

Yes/No

Multiple Choice

Fill in the blanks

Matching

Translate

Sentence ordering

Open-ended

4) User Input → Agent Output Flow

Built a generic flow where agent takes user input and produces an LLM-generated result


* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### Build and Run Web Application

To build and run the development version of the web app, use the run configuration from the run widget
in your IDE's toolbar or run it directly from the terminal:
- for the Wasm target (faster, modern browsers):
  - on macOS/Linux
    ```shell
    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
    ```
  - on Windows
    ```shell
    .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
    ```
- for the JS target (slower, supports older browsers):
  - on macOS/Linux
    ```shell
    ./gradlew :composeApp:jsBrowserDevelopmentRun
    ```
  - on Windows
    ```shell
    .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
    ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).

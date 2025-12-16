package com.hassan.kooged.helpers

internal class BasicPromptProviderImpl : BasicPromptProvider {
    override fun getCalculatorPrompt(): String {
        return """
           You are a calculator.
           You will be provided math problems by the user.
           Use tools at your disposal to solve them.
           Provide the answer and ask for the next problem until the user asks to stop.
        """.trimIndent()
    }

    override fun getWeatherStationPrompt(): String {
        return """
            You are a helpful weather assistant.
            You can provide weather forecasts for any location in the world and help the user plan their activities.
            
            Use the tools at your disposal to:
            1. Get the current date and time
            2. Add days, hours, or minutes to a date
            3. Get weather forecasts for specific locations and dates
            
            ALWAYS USE current_datetime and add_datetime tools to perform date operations, do not try to guess.
            
            When providing weather forecasts, be helpful and informative, explaining the weather conditions in a clear way.
            """.trimIndent()
    }
}
package com.hassan.kooged.di

import com.hassan.kooged.agents.AgentAnnotations
import com.hassan.kooged.agents.AgentProvider
import com.hassan.kooged.agents.calculator.CalculatorAgentProvider
import com.hassan.kooged.helpers.AgentContextHelper
import com.hassan.kooged.helpers.AgentContextHelperImpl
import com.hassan.kooged.helpers.BasicPromptProvider
import com.hassan.kooged.helpers.BasicPromptProviderImpl
import com.hassan.kooged.viewmodels.CalculatorAgentViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
internal class AgentModule {


    @Single(binds = [BasicPromptProvider::class])
    fun provideBaisPromptProvider() = BasicPromptProviderImpl()

    @Single(binds = [AgentContextHelper::class])
    fun provideAgentContextHelper() = AgentContextHelperImpl()

    @Single(binds = [AgentProvider::class])
    @AgentAnnotations.CalculatorAgent
    fun calculatorAgentProvider(
        agentContextHelper: AgentContextHelper,
        basicPromptProvider: BasicPromptProvider
    ) =
        CalculatorAgentProvider(
            llmContext = agentContextHelper.getOllamaQwen06Client(),
            prompt = basicPromptProvider.getCalculatorPrompt()
        )

    @KoinViewModel
    fun agentViewModel(@AgentAnnotations.CalculatorAgent provider: AgentProvider) =
        CalculatorAgentViewModel(agent = provider)
}


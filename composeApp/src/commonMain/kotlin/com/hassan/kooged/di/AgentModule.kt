package com.hassan.kooged.di

import com.hassan.kooged.agents.AgentAnnotations
import com.hassan.kooged.agents.AgentProvider
import com.hassan.kooged.agents.calculator.CalculatorAgentProvider
import com.hassan.kooged.agents.completeSentences.agent.CompleteSentenceAgentProvider
import com.hassan.kooged.agents.completeSentences.agent.CompleteSentenceAgentProviderImpl
import com.hassan.kooged.agents.practiceLanguage.agent.TestGeneratorAgentProvider
import com.hassan.kooged.agents.practiceLanguage.agent.TestGeneratorAgentProviderImpl
import com.hassan.kooged.agents.practiceLanguage.viewmodels.TestGeneratorAgentViewModel
import com.hassan.kooged.helpers.AgentContextHelper
import com.hassan.kooged.helpers.AgentContextHelperImpl
import com.hassan.kooged.helpers.BasicPromptProvider
import com.hassan.kooged.helpers.BasicPromptProviderImpl
import com.hassan.kooged.viewmodels.CalculatorAgentViewModel
import com.hassan.kooged.viewmodels.CompleteSentenceAgentViewModel
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
    ) = CalculatorAgentProvider(
        llmContext = agentContextHelper.getOllamaQwen06Client(),
    )

    @KoinViewModel
    fun agentViewModel(@AgentAnnotations.CalculatorAgent provider: AgentProvider) =
        CalculatorAgentViewModel(agent = provider)


    @Single(binds = [CompleteSentenceAgentProvider::class])
    fun completeSentenceAgentProvider(
        agentContextHelper: AgentContextHelper,
    ) =
        CompleteSentenceAgentProviderImpl(
            llmContext = agentContextHelper.getOllamaQwen06Client(),
        )


    @KoinViewModel
    fun completeSentenceAgentViewModel(provider: CompleteSentenceAgentProvider) =
        CompleteSentenceAgentViewModel(agentProvider = provider)


    @Single(binds = [TestGeneratorAgentProvider::class])
    fun testGeneratorAgentProvider(
        agentContextHelper: AgentContextHelper,
    ) =
        TestGeneratorAgentProviderImpl(
            llmContext = agentContextHelper.getOllamaQwen06Client(),
        )


    @KoinViewModel
    fun testGeneratorAgentProviderViewModel(provider: TestGeneratorAgentProvider) =
        TestGeneratorAgentViewModel(agentProvider = provider)

}


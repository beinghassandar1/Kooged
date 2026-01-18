package com.hassan.kooged.di

import com.hassan.kooged.agents.practiceLanguage.agent.TestGeneratorAgentProvider
import com.hassan.kooged.agents.practiceLanguage.agent.TestGeneratorAgentProviderImpl
import com.hassan.kooged.agents.practiceLanguage.viewmodels.TestGeneratorAgentViewModel
import com.hassan.kooged.helpers.AgentContextHelper
import com.hassan.kooged.helpers.AgentContextHelperImpl
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
internal class AgentModule {

    @Single(binds = [AgentContextHelper::class])
    fun provideAgentContextHelper() = AgentContextHelperImpl()


    @Single(binds = [TestGeneratorAgentProvider::class])
    fun testGeneratorAgentProvider(
        agentContextHelper: AgentContextHelper,
    ) =
        TestGeneratorAgentProviderImpl(
            llmContext = agentContextHelper.getGemini3FlashClient(),
        )


    @KoinViewModel
    fun testGeneratorAgentProviderViewModel(provider: TestGeneratorAgentProvider) =
        TestGeneratorAgentViewModel(agentProvider = provider)

}


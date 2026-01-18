package com.hassan.kooged.agents.completeSentences.di

import com.hassan.kooged.agents.completeSentences.agent.CompleteSentenceAgentProvider
import com.hassan.kooged.agents.completeSentences.agent.CompleteSentenceAgentProviderImpl
import com.hassan.kooged.helpers.AgentContextHelper
import com.hassan.kooged.viewmodels.CompleteSentenceAgentViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val completeSentenceModule = module {

    single<CompleteSentenceAgentProvider> {
        CompleteSentenceAgentProviderImpl(
            llmContext = get<AgentContextHelper>().getGemini3FlashClient(),
        )
    }
    viewModel {
        CompleteSentenceAgentViewModel(agentProvider = get<CompleteSentenceAgentProvider>())
    }

}


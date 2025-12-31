package com.hassan.kooged.agents.askInputAgent.di

import com.hassan.kooged.agents.askInputAgent.agent.AskUserInputAgentProvider
import com.hassan.kooged.agents.askInputAgent.agent.AskUserInputAgentProviderImpl
import com.hassan.kooged.agents.askInputAgent.viewmodel.AskUserInputAgentViewModel
import com.hassan.kooged.helpers.AgentContextHelper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val movieOneLinersModule = module {

    single<AskUserInputAgentProvider> {
        AskUserInputAgentProviderImpl(
            llmContext = get<AgentContextHelper>().getOllamaQwen06Client(),
        )
    }
    viewModel {
        AskUserInputAgentViewModel(agentProvider = get<AskUserInputAgentProvider>())
    }
}


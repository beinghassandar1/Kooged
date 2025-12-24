package com.hassan.kooged.agents.textModeration.di

import com.hassan.kooged.agents.textModeration.TextModerationAgentViewModel
import com.hassan.kooged.agents.textModeration.agent.TextModerationAgentProvider
import com.hassan.kooged.agents.textModeration.agent.TextModerationAgentProviderImpl
import com.hassan.kooged.helpers.AgentContextHelper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val textModerationModule = module {

    single<TextModerationAgentProvider> {
        TextModerationAgentProviderImpl(
            llmContext = get<AgentContextHelper>().getLlamaGuard3ModerationClient(),
        )
    }
    viewModel {
        TextModerationAgentViewModel(agentProvider = get<TextModerationAgentProvider>())
    }
}


package com.hassan.kooged.agents.imageModeration.di

import com.hassan.kooged.agents.imageModeration.ImageModerationAgentViewModel
import com.hassan.kooged.agents.imageModeration.agent.ImageModerationAgentProvider
import com.hassan.kooged.agents.imageModeration.agent.ImageModerationAgentProviderImpl
import com.hassan.kooged.helpers.AgentContextHelper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val imageModerationModule = module {

    single<ImageModerationAgentProvider> {
        ImageModerationAgentProviderImpl(
            llmContext = get<AgentContextHelper>().getLlamaGuard3ModerationClient(),
        )
    }
    viewModel {
        ImageModerationAgentViewModel(agentProvider = get<ImageModerationAgentProvider>())
    }
}


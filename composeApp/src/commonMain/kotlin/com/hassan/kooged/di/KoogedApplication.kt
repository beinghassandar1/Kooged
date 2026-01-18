package com.hassan.kooged.di

import com.hassan.kooged.agents.askInputAgent.di.movieOneLinersModule
import com.hassan.kooged.agents.completeSentences.di.completeSentenceModule
import com.hassan.kooged.agents.imageModeration.di.imageModerationModule
import com.hassan.kooged.agents.textModeration.di.textModerationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)

        modules(
            AgentModule().module,
            textModerationModule,
            imageModerationModule,
            movieOneLinersModule,
            completeSentenceModule
        )
    }
}
package com.hassan.kooged.agents.core

import ai.koog.agents.core.agent.entity.AIAgentStorageKey
import ai.koog.agents.core.agent.entity.createStorageKey
import ai.koog.agents.core.feature.AIAgentGraphFeature
import ai.koog.agents.core.feature.config.FeatureConfig
import ai.koog.agents.core.feature.pipeline.AIAgentGraphPipeline
import co.touchlab.kermit.Logger

/**
 * An example of a feature that provides logging capabilities for the agent to trace a particular event
 * during the agent run.
 *
 * @property logger The logger instance used to perform logging operations.
 */
class AiLoggingFeature(val logger: Logger) {

    class Config : FeatureConfig() {
        var loggerName: String = "agent-logs"
    }

    /**
     * A Logging Feature implementation.
     *
     * This feature supports configuration via the [Config] class,
     * which allows specifying custom logger names.
     */
    companion object Feature : AIAgentGraphFeature<Config, Logger> {
        override val key: AIAgentStorageKey<Logger> = createStorageKey("logging-feature")

        override fun createInitialConfig(): Config = Config()

        /**
         * Installs the Logging Feature into the provided pipeline.
         *
         * The method integrates the feature capabilities into the agent pipeline by setting up interceptors
         * to log information during agent creation, before processing nodes, and after processing nodes by a predefined
         * hook.
         *
         * @param config The configuration for the LoggingFeature, providing logger details.
         * @param pipeline The agent pipeline where logging functionality will be installed.
         */
        override fun install(
            config: Config,
            pipeline: AIAgentGraphPipeline,
        ): Logger {
            val agentLogger = Logger.withTag(config.loggerName)

            pipeline.interceptAgentStarting(this) { eventContext ->
                agentLogger.i("Agent is going to be started (id: ${eventContext.agent.id})")
            }

            pipeline.interceptStrategyStarting(this) { eventContext ->
                agentLogger.i("Strategy ${eventContext.strategy.name} started")
            }

            pipeline.interceptNodeExecutionStarting(this) { eventContext ->
                agentLogger.i("Node ${eventContext.node.name} received input: ${eventContext.input}")
            }

            pipeline.interceptNodeExecutionCompleted(this) { eventContext ->
                agentLogger.i(
                    "Node ${eventContext.node.name} with input: ${eventContext.input} produced output: ${eventContext.output}"
                )
            }

            pipeline.interceptNodeExecutionFailed(this) { eventContext ->
                agentLogger.i(
                    "Node ${eventContext.node.name} with input: ${eventContext.input} produced error: ${eventContext.throwable.message}"
                )
            }

            pipeline.interceptLLMCallStarting(this) { eventContext ->
                agentLogger.i(
                    "Before LLM call with prompt: ${eventContext.prompt}, tools: [${
                        eventContext.tools.joinToString {
                            it.name
                        }
                    }]"
                )
            }

            pipeline.interceptLLMCallCompleted(this) { eventContext ->
                agentLogger.i("After LLM call with response: ${eventContext.responses}")
            }

            return agentLogger
        }
    }
}
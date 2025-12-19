package com.hassan.kooged.agents.practiceLanguage.entities

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a single message in a conversation
 */
@Serializable
@SerialName("message")
@LLMDescription("A message in a conversation with direction (incoming/outgoing), text content, and timestamp")
data class Message(
    @SerialName("direction")
    @property:LLMDescription("Direction of the message: 'incoming' for received messages or 'outgoing' for sent messages")
    val direction: MessageDirection,

    @SerialName("message")
    @property:LLMDescription("The text content of the message")
    val message: String,

    @SerialName("timestamp")
    @property:LLMDescription("ISO 8601 timestamp indicating when the message was sent (e.g., '2023-10-27T18:00:00Z')")
    val timestamp: String
)
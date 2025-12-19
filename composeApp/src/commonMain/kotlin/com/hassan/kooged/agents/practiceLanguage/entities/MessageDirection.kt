package com.hassan.kooged.agents.practiceLanguage.entities

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@LLMDescription("Represents the direction of a message in a conversation")
enum class MessageDirection {
    @SerialName("incoming")
    @LLMDescription("Message received from the conversation partner")
    INCOMING,

    @SerialName("outgoing")
    @LLMDescription("Message sent by the user")
    OUTGOING
}
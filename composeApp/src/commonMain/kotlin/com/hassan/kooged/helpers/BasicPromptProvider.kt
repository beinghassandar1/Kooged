package com.hassan.kooged.helpers

interface BasicPromptProvider {

    fun getSimpleSingleShotPrompt(): String

    fun getWeatherStationPrompt(): String
}
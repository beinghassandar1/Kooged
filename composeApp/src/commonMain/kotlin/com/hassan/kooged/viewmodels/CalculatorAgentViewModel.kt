package com.hassan.kooged.viewmodels

import androidx.lifecycle.ViewModel
import com.hassan.kooged.agents.AgentProvider

class CalculatorAgentViewModel(
    private val agent: AgentProvider
) : ViewModel() {

    init {
        println("HELLOOOOOOOOO ${agent.toString()}")
    }

}
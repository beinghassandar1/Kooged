package com.hassan.kooged.agents.practiceLanguage.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorAgentOutput

@Composable
internal fun GeneratedTestResult(data: TestGeneratorAgentOutput) {
    Text(data.toString())
}
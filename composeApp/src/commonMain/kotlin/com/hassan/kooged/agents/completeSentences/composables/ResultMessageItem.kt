package com.hassan.kooged.agents.completeSentences.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hassan.kooged.agents.completeSentences.entities.CompleteSentenceAgentOutput
import com.jetbrains.example.koog.compose.theme.AppDimension


@Composable
internal fun ResultMessageItem(result: CompleteSentenceAgentOutput) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = "Result",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = AppDimension.spacingSmall)
            )
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(AppDimension.radiusExtraLarge))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(AppDimension.spacingMedium)
            ) {
                result.suggestions.forEachIndexed { index, entity ->
                    Text(
                        text = "Suggestion ${index + 1}: " + entity.suggestion,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Translation ${index + 1}: " + entity.translation,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    HorizontalDivider()
                }
            }
        }
    }
}
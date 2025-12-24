package com.hassan.kooged.agents.practiceLanguage.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hassan.kooged.agents.practiceLanguage.entities.TestGeneratorQuestionsEntity
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview(
    backgroundColor = 0xffffff
)
@Composable
private fun Preview() {
    GeneratedTestResult(
        data = listOf()
    )
}

@Composable
internal fun GeneratedTestResult(
    data: List<TestGeneratorQuestionsEntity>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        data.forEachIndexed { index, question ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Question ${index + 1}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    when (question) {
                        is TestGeneratorQuestionsEntity.YesNoQuestion -> {
                            YesNoQuestionUI(question)
                        }

                        is TestGeneratorQuestionsEntity.MultipleChoiceQuestion -> {
                            MultipleChoiceQuestionUI(question)
                        }

                        is TestGeneratorQuestionsEntity.FillInTheBlankQuestion -> {
                            FillInTheBlankQuestionUI(question)
                        }

                        is TestGeneratorQuestionsEntity.MatchingQuestion -> {
                            MatchingQuestionUI(question)
                        }

                        is TestGeneratorQuestionsEntity.TranslationQuestion -> {
                            TranslationQuestionUI(question)
                        }

                        is TestGeneratorQuestionsEntity.SentenceOrderingQuestion -> {
                            SentenceOrderingQuestionUI(question)
                        }

                        is TestGeneratorQuestionsEntity.OpenEndedQuestion -> {
                            OpenEndedQuestionUI(question)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun YesNoQuestionUI(question: TestGeneratorQuestionsEntity.YesNoQuestion) {
    var selectedAnswer by remember { mutableStateOf<Boolean?>(null) }
    var showResult by remember { mutableStateOf(false) }

    Column {
        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(true to "True", false to "False").forEach { (value, label) ->
                val isSelected = selectedAnswer == value
                val isCorrect = value == question.answer
                val backgroundColor = when {
                    showResult && isCorrect -> Color(0xFF4CAF50)
                    showResult && isSelected && !isCorrect -> Color(0xFFF44336)
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }

                OutlinedButton(
                    onClick = {
                        selectedAnswer = value
                        showResult = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(backgroundColor, RoundedCornerShape(8.dp))
                ) {
                    Text(label)
                }
            }
        }

        AnimatedVisibility(visible = showResult) {
            val isCorrect = selectedAnswer == question.answer
            Text(
                text = if (isCorrect) "✓ Correct!" else "✗ Incorrect. The answer is ${if (question.answer) "True" else "False"}",
                color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun MultipleChoiceQuestionUI(question: TestGeneratorQuestionsEntity.MultipleChoiceQuestion) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }

    Column {
        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        question.options.forEachIndexed { index, option ->
            val isSelected = selectedIndex == index
            val isCorrect = index == question.correctAnswerIndex
            val borderColor = when {
                showResult && isCorrect -> Color(0xFF4CAF50)
                showResult && isSelected && !isCorrect -> Color(0xFFF44336)
                isSelected -> MaterialTheme.colorScheme.primary
                else -> Color.Gray
            }
            val backgroundColor = when {
                showResult && isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                showResult && isSelected && !isCorrect -> Color(0xFFF44336).copy(alpha = 0.1f)
                isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                else -> Color.Transparent
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                    .background(backgroundColor, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        selectedIndex = index
                        showResult = true
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${('A' + index)}. $option",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        AnimatedVisibility(visible = showResult) {
            val isCorrect = selectedIndex == question.correctAnswerIndex
            Text(
                text = if (isCorrect) "✓ Correct!" else "✗ Incorrect. The correct answer is ${('A' + question.correctAnswerIndex)}",
                color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun FillInTheBlankQuestionUI(question: TestGeneratorQuestionsEntity.FillInTheBlankQuestion) {
    var userAnswer by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    Column {
        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userAnswer,
            onValueChange = {
                userAnswer = it
                showResult = false
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type your answer here...") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showResult = true },
            enabled = userAnswer.isNotBlank()
        ) {
            Text("Check Answer")
        }

        AnimatedVisibility(visible = showResult) {
            val isCorrect =
                userAnswer.trim().equals(question.correctAnswer.trim(), ignoreCase = true)
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = if (isCorrect) "✓ Correct!" else "✗ Incorrect",
                    color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!isCorrect) {
                    Text(
                        text = "Correct answer: ${question.correctAnswer}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchingQuestionUI(question: TestGeneratorQuestionsEntity.MatchingQuestion) {
    var selectedLeft by remember { mutableStateOf<String?>(null) }
    var selectedRight by remember { mutableStateOf<String?>(null) }
    var matches by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val leftItems = question.pairs.keys.toList()
    val rightItems = question.pairs.values.toList().shuffled()

    Column {
        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                leftItems.forEach { leftItem ->
                    val isMatched = matches.containsKey(leftItem)
                    val isSelected = selectedLeft == leftItem
                    val borderColor = when {
                        isMatched -> Color(0xFF4CAF50)
                        isSelected -> MaterialTheme.colorScheme.primary
                        else -> Color.Gray
                    }

                    Text(
                        text = leftItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(enabled = !isMatched) {
                                selectedLeft = if (isSelected) null else leftItem
                                if (selectedLeft != null && selectedRight != null) {
                                    matches = matches + (selectedLeft!! to selectedRight!!)
                                    selectedLeft = null
                                    selectedRight = null
                                }
                            }
                            .padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rightItems.forEach { rightItem ->
                    val isMatched = matches.containsValue(rightItem)
                    val isSelected = selectedRight == rightItem
                    val borderColor = when {
                        isMatched -> Color(0xFF4CAF50)
                        isSelected -> MaterialTheme.colorScheme.primary
                        else -> Color.Gray
                    }

                    Text(
                        text = rightItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(enabled = !isMatched) {
                                selectedRight = if (isSelected) null else rightItem
                                if (selectedLeft != null && selectedRight != null) {
                                    matches = matches + (selectedLeft!! to selectedRight!!)
                                    selectedLeft = null
                                    selectedRight = null
                                }
                            }
                            .padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        AnimatedVisibility(visible = matches.size == question.pairs.size) {
            val correctMatches = matches.count { (key, value) -> question.pairs[key] == value }
            val isAllCorrect = correctMatches == question.pairs.size

            Column(modifier = Modifier.padding(top = 12.dp)) {
                Text(
                    text = if (isAllCorrect) "✓ All correct!" else "✗ $correctMatches/${question.pairs.size} correct",
                    color = if (isAllCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!isAllCorrect) {
                    Button(
                        onClick = {
                            matches = emptyMap()
                            selectedLeft = null
                            selectedRight = null
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Try Again")
                    }
                }
            }
        }
    }
}

@Composable
private fun TranslationQuestionUI(question: TestGeneratorQuestionsEntity.TranslationQuestion) {
    var userAnswer by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Translate from ${question.fromLanguage} to ${question.toLanguage}:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userAnswer,
            onValueChange = {
                userAnswer = it
                showResult = false
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type your translation here...") },
            minLines = 2
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showResult = true },
            enabled = userAnswer.isNotBlank()
        ) {
            Text("Check Answer")
        }

        AnimatedVisibility(visible = showResult) {
            val isCorrect =
                userAnswer.trim().equals(question.correctAnswer.trim(), ignoreCase = true)
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = if (isCorrect) "✓ Correct!" else "✗ Not quite",
                    color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!isCorrect) {
                    Text(
                        text = "Correct translation: ${question.correctAnswer}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SentenceOrderingQuestionUI(question: TestGeneratorQuestionsEntity.SentenceOrderingQuestion) {
    var orderedWords by remember { mutableStateOf<List<String>>(emptyList()) }
    var availableWords by remember { mutableStateOf(question.shuffledWords) }
    var showResult by remember { mutableStateOf(false) }

    Column {
        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Ordered words area
        Text(
            text = "Your sentence:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(12.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            orderedWords.forEach { word ->
                Text(
                    text = word,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(6.dp)
                        )
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            orderedWords = orderedWords - word
                            availableWords = availableWords + word
                            showResult = false
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Available words
        Text(
            text = "Available words:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableWords.forEach { word ->
                Text(
                    text = word,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            RoundedCornerShape(6.dp)
                        )
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            orderedWords = orderedWords + word
                            availableWords = availableWords - word
                            showResult = false
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { showResult = true },
                enabled = orderedWords.size == question.shuffledWords.size
            ) {
                Text("Check Answer")
            }

            OutlinedButton(
                onClick = {
                    orderedWords = emptyList()
                    availableWords = question.shuffledWords
                    showResult = false
                }
            ) {
                Text("Reset")
            }
        }

        AnimatedVisibility(visible = showResult) {
            val correctSentence = question.correctOrder.map { question.shuffledWords[it] }
            val isCorrect = orderedWords == correctSentence

            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = if (isCorrect) "✓ Correct!" else "✗ Incorrect",
                    color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!isCorrect) {
                    Text(
                        text = "Correct order: ${correctSentence.joinToString(" ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OpenEndedQuestionUI(question: TestGeneratorQuestionsEntity.OpenEndedQuestion) {
    var userAnswer by remember { mutableStateOf("") }
    var showSample by remember { mutableStateOf(false) }

    Column {
        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type your answer here...") },
            minLines = 4
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showSample = true },
            enabled = userAnswer.isNotBlank()
        ) {
            Text("Show Sample Answer")
        }

        AnimatedVisibility(visible = showSample) {
            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "Sample Answer:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = question.sampleAnswer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
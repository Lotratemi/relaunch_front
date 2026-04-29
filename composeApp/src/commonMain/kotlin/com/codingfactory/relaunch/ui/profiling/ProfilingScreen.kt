package com.codingfactory.relaunch.ui.profiling

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.codingfactory.relaunch.ui.onboarding.BlobsDecoration

@Composable
fun ProfilingScreen(
    viewModel: ProfilingViewModel,
    userId: Long,
    isLoading: Boolean = false,
    error: String? = null
) {
    val state by viewModel.uiState.collectAsState()
    val question = PROFILING_QUESTIONS[state.currentQuestion]
    val progress = (state.currentQuestion + 1).toFloat() / PROFILING_QUESTIONS.size
    val isLastQuestion = state.currentQuestion == PROFILING_QUESTIONS.lastIndex
    val currentAnswer = state.answers[state.currentQuestion]

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {

                // ── Progression ───────────────────────────────────────────
                Text(
                    text = "Question ${state.currentQuestion + 1} / ${PROFILING_QUESTIONS.size}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Color.Black,
                    trackColor = Color(0xFFEEEEEE)
                )

                Spacer(Modifier.height(28.dp))

                // ── Question ──────────────────────────────────────────────
                Text(
                    text = question.text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 26.sp
                )

                Spacer(Modifier.height(20.dp))

                // ── Réponses ──────────────────────────────────────────────
                when (question.type) {
                    QuestionType.CHOICE -> {
                        question.options.forEachIndexed { index, option ->
                            val isSelected = currentAnswer == index.toString()
                            OutlinedButton(
                                onClick = { viewModel.answer(state.currentQuestion, index.toString()) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) Color.Black else Color(0xFFBBBBBB)
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) Color.Black else Color.Transparent,
                                    contentColor   = if (isSelected) Color.White else Color.Black
                                )
                            ) {
                                Text(
                                    text = option,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    QuestionType.SCALE -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(question.scaleLeft ?: "", fontSize = 11.sp, color = Color.Gray,
                                modifier = Modifier.weight(1f))
                            Text(question.scaleRight ?: "", fontSize = 11.sp, color = Color.Gray,
                                textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                        }

                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            (1..5).forEach { value ->
                                val isSelected = currentAnswer == value.toString()
                                OutlinedButton(
                                    onClick = { viewModel.answer(state.currentQuestion, value.toString()) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) Color.Black else Color(0xFFBBBBBB)
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) Color.Black else Color.Transparent,
                                        contentColor   = if (isSelected) Color.White else Color.Black
                                    ),
                                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 0.dp)
                                ) {
                                    Text(value.toString(), fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(28.dp))

                // ── Bouton suivant / soumettre ────────────────────────────
                OutlinedButton(
                    onClick = {
                        if (isLastQuestion) viewModel.submitProfiling(userId)
                        else viewModel.nextQuestion()
                    },
                    enabled = currentAnswer != null && !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.Black
                        )
                    } else {
                        Text(
                            text = if (isLastQuestion) "Analyser mon profil" else "Suivant",
                            fontSize = 16.sp
                        )
                    }
                }

                if (error != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(text = error, fontSize = 12.sp, color = Color(0xFFFF3B30))
                }
            }

            BlobsDecoration(modifier = Modifier.fillMaxWidth().height(220.dp))
        }
    }
}
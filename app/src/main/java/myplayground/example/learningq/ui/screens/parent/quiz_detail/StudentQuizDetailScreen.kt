package myplayground.example.learningq.ui.screens.parent.quiz_detail

import android.app.Application
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.DatastoreSettings
import myplayground.example.learningq.local_storage.dataStore
import myplayground.example.learningq.model.QuizQuestionType
import myplayground.example.learningq.ui.components.CustomOutlinedTextField
import myplayground.example.learningq.ui.components.CustomRadioBadge
import myplayground.example.learningq.ui.theme.LearningQTheme
import myplayground.example.learningq.ui.utils.ViewModelFactory
import myplayground.example.learningq.ui.utils.intToAlphabet

@Composable
fun ParentStudentQuizDetailScreen(
    modifier: Modifier = Modifier,
    vm: ParentStudentQuizDetailViewModel = viewModel(
        factory = ViewModelFactory(
            LocalContext.current.applicationContext as Application,
            Injection.provideRepository(LocalContext.current),
            DatastoreSettings.getInstance(LocalContext.current.dataStore),
        ),
    ),
    quizId: String,
) {
    val inputData by vm.uiState

    LaunchedEffect(quizId) {
        vm.onEvent(ParentStudentQuizDetailEvent.FetchQuestions(quizId))
    }

    ParentStudentQuizDetailContent(
        modifier = modifier,
        inputData,
        vm::onEvent,
    )
}

@Composable
fun ParentStudentQuizDetailContent(
    modifier: Modifier = Modifier,
    inputData: ParentStudentQuizDetailData = ParentStudentQuizDetailData(),
    onEvent: (ParentStudentQuizDetailEvent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        if (!inputData.isLoadingQuestionList && inputData.questionList != null) {
            val idx = inputData.currentQuestionIndex
            val currentQuestion = inputData.questionList[idx]

            Text(
                text = "Question ${idx + 1} of ${inputData.questionList.size}",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentQuestion.title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (currentQuestion.quizType is QuizQuestionType.Essay) {
                CustomOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = inputData.listEssayAnswer[idx],
                    onValueChange = {
                        onEvent(ParentStudentQuizDetailEvent.EssayAnswerChanged(it))
                    },
                    minLines = 4,
                    maxLines = 6,
                )
            } else {
                currentQuestion.multipleChoiceList?.forEachIndexed { i, multipleChoice ->
                    CustomRadioBadge(
                        modifier = Modifier.widthIn(100.dp, Dp.Unspecified),
                        selected = inputData.listSelectedMultipleChoice[idx] == i + 1,
                        onClick = {
                            onEvent(ParentStudentQuizDetailEvent.SelectedMultipleChoiceChanged(i + 1))
                        },
                        text = "${intToAlphabet(i + 1)}. $multipleChoice",
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.weight(1F))

            Row {
                if (idx > 0) {
                    Button(onClick = {
                        onEvent(ParentStudentQuizDetailEvent.PrevQuestion)
                    }) {
                        Text(
                            text = "Previous",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    Spacer(
                        modifier = Modifier.weight(1F)
                    )

                }
                Spacer(modifier = Modifier.weight(1F))

                if (inputData.questionList.size - 1 > idx) {
                    Button(
                        onClick = {
                            onEvent(ParentStudentQuizDetailEvent.NextQuestion)
                        },
                    ) {
                        Text(
                            text = "Next",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                } else {
                    OutlinedButton(onClick = {
                        onEvent(ParentStudentQuizDetailEvent.Submit)
                    }) {
                        Text(
                            text = "Submit",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ParentStudentQuizDetailContentMultipleChoicePreview() {
    LearningQTheme {
        ParentStudentQuizDetailContent()
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ParentStudentQuizDetailContentEssayPreview() {
    LearningQTheme {
        ParentStudentQuizDetailContent()
    }
}


package myplayground.example.learningq.ui.screens.parent.feedback

import android.app.Application
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.DatastoreSettings
import myplayground.example.learningq.local_storage.dataStore
import myplayground.example.learningq.ui.components.CustomButton
import myplayground.example.learningq.ui.components.CustomOutlinedTextField
import myplayground.example.learningq.ui.theme.LearningQTheme
import myplayground.example.learningq.ui.utils.ViewModelFactory

@Composable
fun ParentStudentFeedbackScreen(
    modifier: Modifier = Modifier, vm: ParentStudentFeedbackViewModel = viewModel(
        factory = ViewModelFactory(
            LocalContext.current.applicationContext as Application,
            Injection.provideRepository(LocalContext.current),
            DatastoreSettings.getInstance(LocalContext.current.dataStore),
        )
    )
) {
    val inputData by vm.uiState
    val isLoading by vm.isLoading

    ParentStudentFeedbackContent(
        modifier = modifier,
        inputData = inputData,
        isLoading = isLoading,
        vm::onEvent,
    )
}

@Composable
fun ParentStudentFeedbackContent(
    modifier: Modifier = Modifier,
    inputData: ParentStudentFeedbackInputData = ParentStudentFeedbackInputData(),
    isLoading: Boolean = false,
    onEvent: (ParentStudentFeedbackEvent) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Feedback Form",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Give feedback on the learning activities that have been carried out, if there are suggesstions or input please write them down!",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
        )


        Spacer(modifier = Modifier.height(24.dp))

        CustomOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputData.feedbackAnswer,
            onValueChange = {
                onEvent(ParentStudentFeedbackEvent.FeedbackAnswerChanged(it))
            },
            minLines = 4,
            maxLines = 6,
            enabled = !isLoading,
            label = {
                Text(
                    "Feedback",
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
//            isError = inputData.hasUsernameError,
//            supportingText = {
//                if (inputData.hasUsernameError) {
//                    Text(
//                        "Temporary Input Error",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.error,
//                    )
//                }
//            },
        )


        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.weight(1F))

        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraSmall),
            onClick = {
                onEvent(ParentStudentFeedbackEvent.Submit)
            },
            enabled = !isLoading,
            isLoading = isLoading,
        ) {
            Text(
                text = "Submit",
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

    }
}


@Preview(showBackground = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ParentStudentFeedbackContentPreview() {
    LearningQTheme {
        ParentStudentFeedbackContent()
    }
}

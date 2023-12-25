package myplayground.example.learningq.ui.screens.parent.report_detail

import android.app.Application
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.DatastoreSettings
import myplayground.example.learningq.local_storage.dataStore
import myplayground.example.learningq.ui.navigation.Screen
import myplayground.example.learningq.ui.theme.LearningQTheme
import myplayground.example.learningq.ui.utils.TableCell
import myplayground.example.learningq.ui.utils.TableCellText
import myplayground.example.learningq.ui.utils.ViewModelFactory

@Composable
fun ParentStudentReportDetailScreen(
    modifier: Modifier = Modifier, vm: ParentStudentReportDetailViewModel = viewModel(
        factory = ViewModelFactory(
            LocalContext.current.applicationContext as Application,
            Injection.provideRepository(LocalContext.current),
            DatastoreSettings.getInstance(LocalContext.current.dataStore),
        )
    ),
    navController: NavHostController = rememberNavController(),
) {
    ParentStudentReportDetailContent(
        modifier = modifier
    ) {
        navController.navigate(Screen.ParentFeedback.route)
    }
}

@Composable
fun ParentStudentReportDetailContent(
    modifier: Modifier = Modifier,
    navigateToFeedback: () -> Unit = {},
) {
    val col1Weight = 0.2F
    val col2Weight = 0.5F
    val col3Weight = 0.3F
    val textColor = MaterialTheme.colorScheme.onBackground
    val cellModifier = Modifier.fillMaxHeight()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
        ) {
            TableCellText(
                modifier = cellModifier,
                textColor = textColor,
                text = "No",
                weight = col1Weight,
            )
            TableCellText(
                modifier = cellModifier,
                textColor = textColor,
                text = "Mata Pelajaran",
                weight = col2Weight,
            )
            TableCellText(
                modifier = cellModifier,
                textColor = textColor,
                text = "Nilai",
                weight = col3Weight,
            )
        }

        repeat(1) {
            Row {
                TableCellText(
                    modifier = cellModifier,
                    textColor = textColor,
                    text = "1",
                    weight = col1Weight,
                )
                TableCellText(
                    modifier = cellModifier,
                    textColor = textColor,
                    text = "Agama",
                    weight = col2Weight,
                )
                TableCell(
                    modifier = cellModifier,
                    weight = col3Weight,
                ) { modifier ->
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                navigateToFeedback()
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Feedback",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }

            Row {
                TableCellText(
                    modifier = cellModifier,
                    textColor = textColor,
                    text = "2",
                    weight = col1Weight,
                )
                TableCellText(
                    modifier = cellModifier,
                    textColor = textColor,
                    text = "PPKN",
                    weight = col2Weight,
                )
                TableCellText(
                    modifier = cellModifier,
                    textColor = textColor,
                    text = "100",
                    weight = col3Weight,
                )
            }

            Row {
                TableCellText(
                    modifier = cellModifier,
                    textColor = textColor,
                    text = "3",
                    weight = col1Weight,
                )
                TableCellText(
                    modifier = cellModifier,
                    textColor = textColor,
                    text = "Matematika",
                    weight = col2Weight,
                )
                TableCellText(
                    modifier = cellModifier,
                    textColor = textColor,
                    text = "90",
                    weight = col3Weight,
                )
            }

        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ParentStudentReportDetailContentPreview() {
    LearningQTheme {
        ParentStudentReportDetailContent()
    }
}

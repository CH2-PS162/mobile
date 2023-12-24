package myplayground.example.learningq.ui.screens.admin.course_add

import android.app.Application
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import myplayground.example.learningq.ui.components.PasswordOutlinedTextField
import myplayground.example.learningq.ui.screens.teacher.quiz_add.TeacherQuizAddEvent
import myplayground.example.learningq.ui.theme.LearningQTheme
import myplayground.example.learningq.ui.utils.ViewModelFactory
import myplayground.example.learningq.utils.CustomDayOfWeek

@Composable
fun AdminCourseAddScreen(
    modifier: Modifier = Modifier, vm: AdminCourseAddViewModel = viewModel(
        factory = ViewModelFactory(
            LocalContext.current.applicationContext as Application,
            Injection.provideRepository(LocalContext.current),
            DatastoreSettings.getInstance(LocalContext.current.dataStore),
        )
    )
) {
    val inputData by vm.uiState
    val isLoading by vm.isLoading

    UserAddContent(
        modifier = modifier,
        inputData = inputData,
        isLoading = isLoading,
        vm::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAddContent(
    modifier: Modifier = Modifier,
    inputData: AdminCourseAddInputData = AdminCourseAddInputData(),
    isLoading: Boolean = false,
    onEvent: (AdminCourseAddEvent) -> Unit = {},
) {
    val isClassMenuExpanded = remember {
        mutableStateOf(false)
    }
    val isTeacherMenuExpanded = remember {
        mutableStateOf(false)
    }
    val isDayOfWeekMenuExpanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Create Course",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(24.dp))

        ExposedDropdownMenuBox(
            expanded = isClassMenuExpanded.value,
            onExpandedChange = {
                isClassMenuExpanded.value = !isClassMenuExpanded.value
            },
        ) {
            CustomOutlinedTextField(
                value = inputData.selectedClass?.name ?: "",
                onValueChange = {
                },
                label = { Text("Class") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable {}

            )

            ExposedDropdownMenu(
                expanded = isClassMenuExpanded.value, onDismissRequest = {
                    isClassMenuExpanded.value = false
                }, modifier = Modifier.fillMaxWidth()
            ) { ->
                inputData
                    .classList
                    ?.toList()
                    ?.forEach { item ->
                        DropdownMenuItem(onClick = {
                            onEvent(AdminCourseAddEvent.ClassChanged(item))
                            isClassMenuExpanded.value = false
                        }, text = {
                            Text(text = item.name)
                        })
                    }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))


        ExposedDropdownMenuBox(
            expanded = isTeacherMenuExpanded.value,
            onExpandedChange = {
                isTeacherMenuExpanded.value = !isTeacherMenuExpanded.value
            },
        ) {
            CustomOutlinedTextField(
                value = inputData.selectedTeacher?.name ?: "",
                onValueChange = {
                },
                label = { Text("Teacher") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable {}

            )

            ExposedDropdownMenu(
                expanded = isTeacherMenuExpanded.value, onDismissRequest = {
                    isTeacherMenuExpanded.value = false
                }, modifier = Modifier.fillMaxWidth()
            ) { ->
                inputData
                    .teacherList
                    ?.toList()
                    ?.forEach { item ->
                        DropdownMenuItem(onClick = {
                            onEvent(AdminCourseAddEvent.TeacherChanged(item))
                            isTeacherMenuExpanded.value = false
                        }, text = {
                            Text(text = item.name)
                        })
                    }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputData.name,
            onValueChange = {
                onEvent(AdminCourseAddEvent.NameChanged(it))
            },
            enabled = !isLoading,
            label = {
                Text(
                    "Name",
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

        Spacer(modifier = Modifier.height(12.dp))


        ExposedDropdownMenuBox(
            expanded = isDayOfWeekMenuExpanded.value,
            onExpandedChange = {
                isDayOfWeekMenuExpanded.value = !isDayOfWeekMenuExpanded.value
            },
        ) {
            CustomOutlinedTextField(
                value = inputData.dayOfWeek.toString() ?: "",
                onValueChange = {
                },
                label = { Text("Day of Week") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable {}

            )

            ExposedDropdownMenu(
                expanded = isDayOfWeekMenuExpanded.value, onDismissRequest = {
                    isDayOfWeekMenuExpanded.value = false
                }, modifier = Modifier.fillMaxWidth()
            ) { ->
                CustomDayOfWeek.list()
                    .forEach { item ->
                        DropdownMenuItem(onClick = {
                            onEvent(AdminCourseAddEvent.DayOfWeekChanged(item))
                            isDayOfWeekMenuExpanded.value = false
                        }, text = {
                            Text(text = item.name)
                        })
                    }
            }
        }

//        Spacer(modifier = Modifier.height(12.dp))


        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.weight(1F))

        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraSmall),
            onClick = {
                onEvent(AdminCourseAddEvent.Submit)
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
fun UserAddContentPreview() {
    LearningQTheme {
        UserAddContent()
    }
}

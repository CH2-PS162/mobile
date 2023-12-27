package myplayground.example.learningq.ui.screens.admin.user_add

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
import myplayground.example.learningq.model.Role
import myplayground.example.learningq.ui.components.CustomButton
import myplayground.example.learningq.ui.components.CustomOutlinedTextField
import myplayground.example.learningq.ui.components.PasswordOutlinedTextField
import myplayground.example.learningq.ui.screens.admin.course_add.AdminCourseAddEvent
import myplayground.example.learningq.ui.theme.LearningQTheme
import myplayground.example.learningq.ui.utils.ViewModelFactory
import myplayground.example.learningq.utils.CustomDayOfWeek

@Composable
fun AdminUserAddScreen(
    modifier: Modifier = Modifier, vm: AdminUserAddViewModel = viewModel(
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
    inputData: AdminUserAddInputData = AdminUserAddInputData(),
    isLoading: Boolean = false,
    onEvent: (AdminUserAddEvent) -> Unit = {},
) {
    val isRoleMenuExpanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Create User",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputData.name,
            onValueChange = {
                onEvent(AdminUserAddEvent.NameChanged(it))
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

        CustomOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputData.email,
            onValueChange = {
                onEvent(AdminUserAddEvent.EmailChanged(it))
            },
            enabled = !isLoading,
            label = {
                Text(
                    "Email",
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

        PasswordOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputData.password,
            onValueChange = {
                onEvent(AdminUserAddEvent.PasswordChanged(it))
            },
            enabled = !isLoading,
            label = {
                Text(
                    "Password",
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
            expanded = isRoleMenuExpanded.value,
            onExpandedChange = {
                isRoleMenuExpanded.value = !isRoleMenuExpanded.value
            },
        ) {
            CustomOutlinedTextField(
                value = inputData.role?.toString() ?: "",
                onValueChange = {
                },
                label = { Text("Role") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable {}

            )

            ExposedDropdownMenu(
                expanded = isRoleMenuExpanded.value, onDismissRequest = {
                    isRoleMenuExpanded.value = false
                }, modifier = Modifier.fillMaxWidth()
            ) { ->
                Role.list()
                    .forEach { item ->
                        DropdownMenuItem(onClick = {
                            onEvent(AdminUserAddEvent.RoleChanged(item))
                            isRoleMenuExpanded.value = false
                        }, text = {
                            Text(text = item.toString())
                        })
                    }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.weight(1F))

        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraSmall),
            onClick = {
                onEvent(AdminUserAddEvent.Submit)
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

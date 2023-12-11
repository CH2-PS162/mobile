package myplayground.example.learningq.ui.screens.sign_in

import android.app.Application
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import myplayground.example.learningq.ui.components.CustomButton
import myplayground.example.learningq.ui.components.CustomOutlinedTextField
import myplayground.example.learningq.ui.components.PasswordOutlinedTextField
import myplayground.example.learningq.ui.navigation.Screen
import myplayground.example.learningq.ui.theme.LearningQTheme
import myplayground.example.learningq.ui.utils.ViewModelFactory

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    vm: SignInViewModel = viewModel(
        factory = ViewModelFactory(
            LocalContext.current.applicationContext as Application,
            Injection.provideRepository(LocalContext.current),
            DatastoreSettings.getInstance(LocalContext.current.dataStore),
        )
    ),
    navController: NavHostController = rememberNavController(),
) {

    val inputData by vm.uiState
    val event by vm.validationEvent.collectAsState(initial = SignInUIEvent.ValidationEvent.None())
    val isLoading by vm.isLoading

    LaunchedEffect(event) {
        if (event is SignInUIEvent.ValidationEvent.Success) {
            navController.navigate(Screen.StudentDashboard.route) {
                popUpTo(0)
            }
        }
    }

    SignInContent(
        modifier = modifier,
        inputData,
        isLoading,
        vm::onEvent,
        event,
    ) {
        navController.navigate(Screen.SignUp.route) {
            popUpTo(Screen.Landing.route) {
                inclusive = false
            }
        }
    }
}

@Composable
fun SignInContent(
    modifier: Modifier = Modifier,
    inputData: SignInInputData = SignInInputData(),
    isLoading: Boolean = false,
    onEvent: (SignInUIEvent) -> Unit = {},
    event: SignInUIEvent.ValidationEvent = SignInUIEvent.ValidationEvent.None(),
    navigateToSignUp: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp, 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputData.username,
            onValueChange = {
                onEvent(SignInUIEvent.UsernameChanged(it))
            },
            enabled = !isLoading,
            label = {
                Text(
                    "Email",
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            isError = inputData.hasUsernameError,
            supportingText = {
                if (inputData.hasUsernameError) {
                    Text(
                        "Temporary Input Error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordOutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = inputData.password,
            onValueChange = {
                onEvent(SignInUIEvent.PasswordChanged(it))
            },
            enabled = !isLoading,
            label = {
                Text(
                    "Password",
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            isError = inputData.hasPasswordError,
            supportingText = {
                if (inputData.hasPasswordError) {
                    Text(
                        "Temporary Input Error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            })

        Spacer(modifier = Modifier.height(4.dp))


        Text(
            modifier = Modifier
                .align(Alignment.End)
                .clickable { }
                .padding(10.dp, 4.dp, 0.dp, 8.dp),
            text = "Forgot Password",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomButton(
            onClick = {
                onEvent(SignInUIEvent.Submit)
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isLoading = isLoading,
        ) {
            Text(
                "Login",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier
            .align(Alignment.Start)
            .clickable {
                navigateToSignUp()
            }
            .padding(0.dp, 4.dp, 10.dp, 8.dp)) {
            Text(
                "Don't have an Account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                "Sign Up",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignInContentPreview() {
    LearningQTheme {
        SignInContent()
    }
}


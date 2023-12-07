package myplayground.example.learningq.ui.screens.student.profile

import android.app.Application
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import myplayground.example.learningq.R
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.DatastoreSettings
import myplayground.example.learningq.local_storage.dataStore
import myplayground.example.learningq.model.Role
import myplayground.example.learningq.model.User
import myplayground.example.learningq.ui.theme.LearningQTheme
import myplayground.example.learningq.ui.utils.ViewModelFactory
import myplayground.example.learningq.ui.utils.debugPlaceholder

@Composable
fun StudentProfileScreen(
    modifier: Modifier = Modifier,
    vm: StudentProfileViewModel = viewModel(
        factory = ViewModelFactory(
            LocalContext.current.applicationContext as Application,
            Injection.provideRepository(LocalContext.current),
            DatastoreSettings.getInstance(LocalContext.current.dataStore),
        )
    )
) {
    val isLoading by vm.authManager.isLoading.collectAsState()
    val user by vm.authManager.user.collectAsState()

    StudentProfileContent(
        modifier = modifier,
        isLoading = isLoading,
        user = user,
    )
}

@Composable
fun StudentProfileContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    user: User? = null,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = user?.image_url,
                contentDescription = "Profile Photo",
                placeholder = debugPlaceholder(debugPreview = R.drawable.avatar_placeholder),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(160.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = user?.name ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.background)
                .padding(0.dp, 8.dp),
        ) {
            if (isLoading) {

            } else if (user != null) {
                StudentProfileInformationRow(
                    title = "Nomor Induk / NISN",
                    content = user.id,
                )
            }
        }
    }
}

@Composable
fun StudentProfileInformationRow(
    title: String = "",
    content: String = "",
) {
    val strokeColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {

                val strokeWidth = 4 * density
                val y = size.height - strokeWidth / 2

                drawLine(
                    strokeColor,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }
            .padding(16.dp, 6.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = content,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun StudentProfileContentPreview() {
    LearningQTheme {
        StudentProfileContent(
            isLoading = false,
            user = User(
                id = "1",
                name = "Student",
                image_url = "https://miro.medium.com/v2/resize:fill:110:110/1*x1I-A7aVdqWFelvJakKWBg.jpeg",
                role = Role.Student,
            )
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun StudentProfileInformationRowPreview() {
    LearningQTheme {
        StudentProfileInformationRow(
            "Title",
            "Lorem ipsum"
        )
    }
}


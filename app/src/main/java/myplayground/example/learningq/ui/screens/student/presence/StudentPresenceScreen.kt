package myplayground.example.learningq.ui.screens.student.presence

import android.app.Application
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.local_storage.DatastoreSettings
import myplayground.example.learningq.local_storage.dataStore
import myplayground.example.learningq.model.Class
import myplayground.example.learningq.ui.navigation.Screen
import myplayground.example.learningq.ui.screens.student.quiz.StudentQuizCardSkeletonView
import myplayground.example.learningq.ui.theme.LearningQTheme
import myplayground.example.learningq.ui.utils.ViewModelFactory

@Composable
fun StudentPresenceScreen(
    modifier: Modifier = Modifier,
    vm: StudentPresenceViewModel = viewModel(
        factory = ViewModelFactory(
            LocalContext.current.applicationContext as Application,
            Injection.provideRepository(LocalContext.current),
            DatastoreSettings.getInstance(LocalContext.current.dataStore),
        )
    ),
    navController: NavHostController = rememberNavController(),
) {
    val classesPagingItem = vm.classState.collectAsLazyPagingItems()

    StudentPresenceContent(
        modifier = modifier,
        classesPagingItem = classesPagingItem
    ) { classId ->
        navController.navigate(Screen.StudentPresenceDetail.createRoute(classId))
    }
}

@Composable
fun StudentPresenceContent(
    modifier: Modifier = Modifier,
    classesPagingItem: LazyPagingItems<Class>? = null,
    navigateToPresenceDetail: (classId: String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        classesPagingItem?.let { classesPagingItem ->

            items(classesPagingItem.itemCount) { index ->
                val currentClass = classesPagingItem[index]!!

                StudentPresenceCard(
                    `class` = currentClass,
                    navigateToPresenceDetail = navigateToPresenceDetail,
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            classesPagingItem.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        items(10) {
                            StudentQuizCardSkeletonView()
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = classesPagingItem.loadState.refresh as LoadState.Error
                        item {
                            Text(error.toString())
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val error = classesPagingItem.loadState.append as LoadState.Error
                        item {
                            Text(error.toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentPresenceCard(
    `class`: Class,
    navigateToPresenceDetail: (classId: String) -> Unit = {},
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
        ),
    ) {
        Row(
            modifier = Modifier.padding(8.dp, 8.dp, 12.dp, 8.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = `class`.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.weight(1F))
            Button(
                modifier = Modifier.align(Alignment.CenterVertically),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    navigateToPresenceDetail(`class`.id)
                },
            ) {
                Text(
                    text = "Detail",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun StudentPresenceContentPreview() {
    LearningQTheme {
        StudentPresenceContent()
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun StudentPresenceCardPreview() {
    LearningQTheme {
        StudentPresenceCard(
            `class` = Class(
                id = "1",
                courseId = "",
                name = "Matematika",
                instructorId = 0,
                schedule = "",
                description = "",
            )
        )
    }
}


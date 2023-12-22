package myplayground.example.learningq.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import myplayground.example.learningq.R
import myplayground.example.learningq.di.Injection
import myplayground.example.learningq.model.User
import myplayground.example.learningq.ui.navigation.Screen
import myplayground.example.learningq.ui.theme.LearningQTheme
import myplayground.example.learningq.ui.utils.debugPlaceholder
import myplayground.example.learningq.utils.AuthManager

@Composable
fun DrawerHeader(
    modifier: Modifier = Modifier,
    user: User? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (user != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (user.imageUrl != null) {
                    AsyncImage(
                        modifier = Modifier
                            .width(120.dp)
                            .clip(CircleShape),
                        model = user.imageUrl,
                        contentDescription = "Profile Photo",
                        placeholder = debugPlaceholder(debugPreview = R.drawable.avatar_placeholder),
                        contentScale = ContentScale.FillWidth,
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .clip(CircleShape),
                        painter = painterResource(R.drawable.avatar_placeholder),
                        contentDescription = "Profile Photo",
                        contentScale = ContentScale.FillWidth,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleSmall,
                )

                Text(
                    text = user.email,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    currentRoute: String = "",
    closeDrawer: () -> Unit = {},
    items: List<MenuItem> = listOf(),
    onItemClick: (menuItems: MenuItem) -> Unit = {}
) {
    val maxVisibleItems = 6.5F // Calculate item height based on the density
    val itemHeight = with(LocalDensity.current) {
        (10.dp.toPx() * maxVisibleItems).toDp()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp, 4.dp),
    ) {

        for (item in items) {
            val isActiveRoute = (item.activeRoute != null && item.activeRoute == currentRoute)

            if (item.isSpacing) { // how to make this work
                Spacer(modifier = Modifier.height(itemHeight))
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .clip(MaterialTheme.shapes.medium)
                        .background(if (isActiveRoute) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.background)
                        .clickable {
                            if (!isActiveRoute) {
                                onItemClick(item)
                            }
                            closeDrawer()
                        }
                        .padding(12.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (item.icon != null) {
                        Icon(
                            tint = item.color,
                            imageVector = item.icon,
                            contentDescription = item.contentDescription,
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.title,
                        color = item.color,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DrawerBodyStudent(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    currentRoute: String = "",
    authManager: AuthManager = Injection.provideAuthManager(LocalContext.current),
    closeDrawer: () -> Unit = {},
) {
    val isLogoutModalOpen = remember {
        mutableStateOf(false)
    }

    DrawerBody(modifier = modifier,
        currentRoute = currentRoute,
        closeDrawer = closeDrawer,
        items = listOf(
            MenuItem(
                id = "home",
                title = "Home",
                contentDescription = "Home",
                activeRoute = Screen.StudentDashboard.route,
                icon = Icons.Default.Home,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "presence",
                title = "Presence",
                contentDescription = "Presence",
                icon = Icons.Default.CalendarMonth,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "quiz",
                title = "Quiz",
                contentDescription = "Quiz",
                activeRoute = Screen.StudentQuiz.route,
                icon = Icons.Default.Quiz,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "report",
                title = "Report",
                contentDescription = "Report",
                activeRoute = Screen.StudentReport.route,
                icon = Icons.Default.Dataset,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(isSpacing = true),
            MenuItem(
                id = "profile",
                title = "Profile",
                contentDescription = "Profile",
                icon = Icons.Default.Person,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "setting",
                title = "Settings",
                contentDescription = "Settings",
                icon = Icons.Default.Settings,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "logout",
                title = "Logout",
                contentDescription = "Logout",
                icon = Icons.Default.Logout,
                color = MaterialTheme.colorScheme.error,
            ),
        ),
        onItemClick = { menuItem ->
            when (menuItem.id) {
                "home" -> {
                    navController.navigate(Screen.StudentDashboard.route) {
                        popUpTo(0)
                    }
                }

                "presence" -> {
                    navController.navigate(Screen.StudentPresence.route) {
                        popUpTo(0)
                    }
                }

                "quiz" -> {
                    navController.navigate(Screen.StudentQuiz.route) {
                        popUpTo(0)
                    }
                }

                "report" -> {
                    navController.navigate(Screen.StudentReport.route) {
                        popUpTo(0)
                    }
                }

                "profile" -> {
                    navController.navigate(Screen.StudentProfile.route)
                }

                "setting" -> {
                    navController.navigate(Screen.Setting.route)
                }

                "logout" -> {
                    isLogoutModalOpen.value = true
                }
            }
        })

    if (isLogoutModalOpen.value) {
        DialogLogout(onDismissRequest = {
            isLogoutModalOpen.value = false
        }, logout = {
            authManager.logout()

            navController.navigate(Screen.SignIn.route) {
                popUpTo(0)
            }
        })
    }
}

@Composable
fun DrawerBodyTeacher(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    currentRoute: String = "",
    authManager: AuthManager = Injection.provideAuthManager(LocalContext.current),
    closeDrawer: () -> Unit = {},
) {
    val isLogoutModalOpen = remember {
        mutableStateOf(false)
    }

    DrawerBody(
        modifier = modifier,
        currentRoute = currentRoute,
        closeDrawer = closeDrawer,
        items = listOf(
            MenuItem(
                id = "feedback",
                title = "Feedback",
                contentDescription = "Feedback",
                activeRoute = Screen.TeacherDashboard.route,
                icon = Icons.Default.ShortText,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "quiz",
                title = "Quiz",
                contentDescription = "Quiz",
                activeRoute = Screen.TeacherQuiz.route,
                icon = Icons.Default.Quiz,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(isSpacing = true),
            MenuItem(
                id = "profile",
                title = "Profile",
                contentDescription = "Profile",
                icon = Icons.Default.Person,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "setting",
                title = "Settings",
                contentDescription = "Settings",
                icon = Icons.Default.Settings,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "logout",
                title = "Logout",
                contentDescription = "Logout",
                icon = Icons.Default.Logout,
                color = MaterialTheme.colorScheme.error,
            ),
        ),
        onItemClick = { menuItem ->
            when (menuItem.id) {
                "feedback" -> {
                    navController.navigate(Screen.TeacherDashboard.route) {
                        popUpTo(0)
                    }
                }

                "quiz" -> {
                    navController.navigate(Screen.TeacherQuiz.route) {
                        popUpTo(0)
                    }
                }

                "profile" -> {
                    navController.navigate(Screen.TeacherProfile.route)
                }

                "setting" -> {
                    navController.navigate(Screen.Setting.route)
                }

                "logout" -> {
                    isLogoutModalOpen.value = true
                }
            }
        })

    if (isLogoutModalOpen.value) {
        DialogLogout(
            onDismissRequest = {
                isLogoutModalOpen.value = false
            }, logout = {
                authManager.logout()

                navController.navigate(Screen.SignIn.route) {
                    popUpTo(0)
                }
            })
    }
}


@Composable
fun DrawerBodyAdmin(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    currentRoute: String = "",
    authManager: AuthManager = Injection.provideAuthManager(LocalContext.current),
    closeDrawer: () -> Unit = {},
) {
    val isLogoutModalOpen = remember {
        mutableStateOf(false)
    }

    DrawerBody(
        modifier = modifier,
        currentRoute = currentRoute,
        closeDrawer = closeDrawer,
        items = listOf(
            MenuItem(
                id = "class",
                title = "Class",
                contentDescription = "Class",
                activeRoute = Screen.AdminClass.route,
                icon = Icons.Default.School,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "course",
                title = "Course",
                contentDescription = "Course",
                activeRoute = Screen.AdminCourse.route,
                icon = Icons.Default.LocalLibrary,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "user",
                title = "User",
                contentDescription = "User",
                activeRoute = Screen.AdminUser.route,
                icon = Icons.Default.SupervisedUserCircle,
                color = MaterialTheme.colorScheme.onBackground,
            ),


            MenuItem(isSpacing = true),
            MenuItem(
                id = "profile",
                title = "Profile",
                contentDescription = "Profile",
                icon = Icons.Default.Person,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "setting",
                title = "Settings",
                contentDescription = "Settings",
                icon = Icons.Default.Settings,
                color = MaterialTheme.colorScheme.onBackground,
            ),
            MenuItem(
                id = "logout",
                title = "Logout",
                contentDescription = "Logout",
                icon = Icons.Default.Logout,
                color = MaterialTheme.colorScheme.error,
            ),
        ),
        onItemClick = { menuItem ->
            when (menuItem.id) {
                "class" -> {
                    navController.navigate(Screen.AdminClass.route) {
                        popUpTo(0)
                    }
                }

                "course" -> {

                }

                "user" -> {
                    navController.navigate(Screen.AdminUser.route) {
                        popUpTo(0)
                    }
                }


                "profile" -> {
                    navController.navigate(Screen.AdminProfile.route)
                }

                "setting" -> {
                    navController.navigate(Screen.Setting.route)
                }

                "logout" -> {
                    isLogoutModalOpen.value = true
                }
            }
        })

    if (isLogoutModalOpen.value) {
        DialogLogout(
            onDismissRequest = {
                isLogoutModalOpen.value = false
            }, logout = {
                authManager.logout()

                navController.navigate(Screen.SignIn.route) {
                    popUpTo(0)
                }
            })
    }
}


@Composable
fun DialogLogout(
    onDismissRequest: () -> Unit = {},
    logout: () -> Unit = {},
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismissRequest,
        icon = { //                        Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(
                text = "Logout",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        text = {
            Text(
                text = "Apakah anda ingin keluar?",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        confirmButton = {
            Button(onClick = {
                logout()
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismissRequest()
            }) {
                Text("Cancel")
            }
        })
}

data class MenuItem(
    val id: String = "",
    val title: String = "",
    val contentDescription: String? = null,
    val icon: ImageVector? = null,
    val activeRoute: String? = null,
    val color: Color = Color.Unspecified,
    val isSpacing: Boolean = false,
)

@Composable
@Preview(showBackground = true)
fun DrawerHeaderPreview() {
    LearningQTheme {
        DrawerHeader()
    }
}

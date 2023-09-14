package myplayground.example.jakpost

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import myplayground.example.jakpost.ui.layout.Appbar
import myplayground.example.jakpost.ui.layout.BottomBar
import myplayground.example.jakpost.ui.navigation.Screen
import myplayground.example.jakpost.ui.screens.home.HomeScreen
import myplayground.example.jakpost.ui.screens.search.SearchScreen
import myplayground.example.jakpost.ui.screens.setting.SettingScreen

@Composable
fun JakPostApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        topBar = {
            if (currentRoute != Screen.Search.route)
                Appbar(navController = navController)
        },
        bottomBar = {
            if (currentRoute != Screen.Search.route)
                BottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Favourite.route) {
                HomeScreen()
            }
            composable(Screen.Setting.route) {
                SettingScreen()
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}



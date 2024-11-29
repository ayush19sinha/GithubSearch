package my.android.githubsearch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.ui.screens.HomeScreen
import my.android.githubsearch.ui.screens.RepositoryDetailScreen
import my.android.githubsearch.ui.screens.WebViewScreen
import my.android.githubsearch.ui.viewmodel.HomeViewModel
import my.android.githubsearch.ui.viewmodel.RepositoryDetailViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun GitHubSearchNavigation(
    homeViewModel: HomeViewModel,
    repositoryDetailViewModel: RepositoryDetailViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Home.route,
        modifier = modifier
    ) {
        composable(route = AppRoutes.Home.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                onRepositoryClick = { repository ->
                    val repositoryJson = Gson().toJson(repository)
                    val encodedJson = URLEncoder.encode(repositoryJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("${AppRoutes.RepositoryDetail.route}/$encodedJson")
                }
            )
        }

        composable(
            route = "${AppRoutes.RepositoryDetail.route}/{repositoryJson}",
            arguments = listOf(
                navArgument("repositoryJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val repositoryJson = backStackEntry.arguments?.getString("repositoryJson")
            val decodedJson = URLDecoder.decode(repositoryJson, StandardCharsets.UTF_8.toString())
            val repository = Gson().fromJson(decodedJson, Repository::class.java)
            RepositoryDetailScreen(
                repository = repository,
                repositoryDetailViewModel = repositoryDetailViewModel,
                onBackClick = { navController.navigateUp() },
                onViewOnGithubClick = { url ->
                    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                    navController.navigate("${AppRoutes.WebView.route}/$encodedUrl")
                }
            )
        }

        composable(
            route = "${AppRoutes.WebView.route}/{url}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url")
            val decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
            WebViewScreen(url = decodedUrl, onBackClick = { navController.navigateUp() })
        }
    }
}
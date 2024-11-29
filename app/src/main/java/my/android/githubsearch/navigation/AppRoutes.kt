package my.android.githubsearch.navigation

sealed class AppRoutes(val route: String) {
    object Home : AppRoutes("home")
    object RepositoryDetail : AppRoutes("repository_detail")
    object WebView : AppRoutes("web_view")
}
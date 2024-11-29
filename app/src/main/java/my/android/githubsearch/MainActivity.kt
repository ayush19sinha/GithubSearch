package my.android.githubsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import my.android.githubsearch.data.local.AppDatabase
import my.android.githubsearch.data.network.createGitHubApiService
import my.android.githubsearch.data.repository.GitHubRepository
import my.android.githubsearch.navigation.GitHubSearchNavigation
import my.android.githubsearch.ui.theme.GithubSearchTheme
import my.android.githubsearch.ui.viewmodel.HomeViewModel
import my.android.githubsearch.ui.viewmodel.RepositoryDetailViewModel

class MainActivity : ComponentActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var repositoryDetailViewModel: RepositoryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext
        val apiService = createGitHubApiService(context)
        val database = AppDatabase.getDatabase(context)
        val repositoryDao = database.repositoryDao()
        val repository = GitHubRepository(context, apiService, repositoryDao)

        homeViewModel = HomeViewModel(repository)
        repositoryDetailViewModel = RepositoryDetailViewModel(repository)

        setContent {
            GithubSearchTheme {
                GitHubSearchNavigation(
                    homeViewModel = homeViewModel,
                    repositoryDetailViewModel = repositoryDetailViewModel
                )
            }
        }
    }
}
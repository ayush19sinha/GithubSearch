package my.android.githubsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import my.android.githubsearch.navigation.GitHubSearchNavigation
import my.android.githubsearch.ui.theme.GithubSearchTheme
import my.android.githubsearch.ui.viewmodel.HomeViewModel
import my.android.githubsearch.ui.viewmodel.RepositoryDetailViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val repositoryDetailViewModel: RepositoryDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GithubSearchTheme(dynamicColor = false) {
                GitHubSearchNavigation(
                    homeViewModel = homeViewModel,
                    repositoryDetailViewModel = repositoryDetailViewModel
                )
            }
        }
    }
}
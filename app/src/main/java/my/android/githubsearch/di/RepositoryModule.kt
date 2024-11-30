package my.android.githubsearch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import my.android.githubsearch.data.local.RepositoryDao
import my.android.githubsearch.data.network.GitHubApiService
import my.android.githubsearch.data.repository.GitHubRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        apiService: GitHubApiService,
        repositoryDao: RepositoryDao
    ): GitHubRepository {
        return GitHubRepository(apiService, repositoryDao)
    }
}
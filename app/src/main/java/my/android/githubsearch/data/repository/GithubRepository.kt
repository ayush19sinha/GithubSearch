package my.android.githubsearch.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import my.android.githubsearch.data.local.RepositoryDao
import my.android.githubsearch.data.model.Contributor
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.data.network.GitHubApiService
import my.android.githubsearch.data.network.createGitHubApiService

class GitHubRepository(
    private val context: Context,
    private val apiService: GitHubApiService = createGitHubApiService(context = context),
    private val repositoryDao: RepositoryDao
) {
    fun searchRepositories(query: String, page: Int): Flow<List<Repository>> = flow {
        try {
            val searchResult = apiService.searchRepositories(query, page)

            if (page == 1) {
                repositoryDao.clearCachedRepositories()
                val cachedRepos = searchResult.items.take(15).map {
                    it.copy(isOfflineCache = true)
                }
                repositoryDao.insertRepositories(cachedRepos)
            }

            emit(searchResult.items)
        } catch (e: Exception) {
            emitAll(
                repositoryDao.getCachedRepositories()
            )
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getRepositoryContributors(owner: String, repo: String): List<Contributor> {
        return apiService.getRepositoryContributors(owner, repo)
    }
}
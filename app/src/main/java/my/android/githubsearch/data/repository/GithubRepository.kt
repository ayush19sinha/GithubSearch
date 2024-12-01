package my.android.githubsearch.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import my.android.githubsearch.data.local.RepositoryDao
import my.android.githubsearch.data.model.Contributor
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.data.network.GitHubApiService
import javax.inject.Inject

class GitHubRepository @Inject constructor(
    private val apiService: GitHubApiService,
    private val repositoryDao: RepositoryDao
) {
    fun searchRepositories(query: String, page: Int): Flow<List<Repository>> = flow {
        try {
            if (page == 1) {
                repositoryDao.clearCachedRepositories()
                val cachedRepos = mutableListOf<Repository>()
                var currentPage = 1
                var totalCached = 0

                while (totalCached < 15) {
                    val result = apiService.searchRepositories(query, currentPage)
                    val itemsToCache = result.items.take(15 - totalCached)

                    if (itemsToCache.isEmpty()) break

                    cachedRepos.addAll(itemsToCache.map { it.copy(isOfflineCache = true) })
                    totalCached += itemsToCache.size
                    currentPage++
                }
                repositoryDao.insertRepositories(cachedRepos)
            }

            val searchResult = apiService.searchRepositories(query, page)
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
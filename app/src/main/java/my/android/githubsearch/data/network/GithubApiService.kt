package my.android.githubsearch.data.network

import my.android.githubsearch.data.model.Contributor
import my.android.githubsearch.data.model.GitHubSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("/search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): GitHubSearchResponse

    @GET("/repos/{owner}/{repo}/contributors")
    suspend fun getRepositoryContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): List<Contributor>
}
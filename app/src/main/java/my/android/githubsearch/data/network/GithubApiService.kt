package my.android.githubsearch.data.network

import android.content.Context
import my.android.githubsearch.data.model.Contributor
import my.android.githubsearch.data.model.GitHubSearchResponse
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit

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


fun createGitHubApiService(context: Context): GitHubApiService {
    val cache = Cache(
        directory = File(context.cacheDir, "http_cache"),
        maxSize = 10 * 1024 * 1024
    )

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor { chain ->
            val response = chain.proceed(chain.request())
            response.newBuilder()
                .header("Cache-Control", "public, max-age=${60 * 60}")
                .build()
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GitHubApiService::class.java)
}
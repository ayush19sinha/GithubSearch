package my.android.githubsearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class GitHubSearchResponse(
    val total_count: Int,
    val items: List<Repository>
)

@Entity(tableName = "repositories")
data class Repository(
    @PrimaryKey
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String?,
    val htmlUrl: String,
    val owner: RepositoryOwner,
    val stargazersCount: Int,
    val language: String?,
    val forksCount: Int,
    val updated_at: String,
    val isOfflineCache: Boolean = false
)

data class RepositoryOwner(
    val avatar_url: String,
    val html_url: String,
    val login: String,
)
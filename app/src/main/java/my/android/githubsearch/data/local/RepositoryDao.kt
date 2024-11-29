package my.android.githubsearch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import my.android.githubsearch.data.model.Repository

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<Repository>)

    @Query("SELECT * FROM repositories WHERE isOfflineCache = 1 LIMIT 15")
    fun getCachedRepositories(): Flow<List<Repository>>

    @Query("DELETE FROM repositories WHERE isOfflineCache = 1")
    suspend fun clearCachedRepositories()
}
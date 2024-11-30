package my.android.githubsearch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.utils.Converters

@Database(entities = [Repository::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}